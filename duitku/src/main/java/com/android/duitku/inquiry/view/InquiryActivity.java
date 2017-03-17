package com.android.duitku.inquiry.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.duitku.R;
import com.android.duitku.base.BaseActivity;
import com.android.duitku.base.PresenterFactory;
import com.android.duitku.inquiry.presenter.InquiryPresenter;
import com.android.duitku.model.Inquiry;
import com.android.duitku.paymentmethod.view.PaymentMethodActivity;
import com.android.duitku.utils.DuitkuPreferences;
import com.android.duitku.utils.Util;
import com.android.duitku.utils.constant;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Created by latifalbar on 11/17/2015.
 */
public class InquiryActivity extends BaseActivity implements InquiryActivityView {

    private InquiryPresenter mInquiryPresenter;
    private Spinner mSpinnerNamaDanCodeBarang;
    private TextView mTextDeskripsi;
    private TextView mTextorderId;
    private TextView mTextHargaBarang;
    private TextView mTextProduct;
    private TextView mMerchantCode;
    private Button mButtonKePembayaran;
    private Inquiry inquiry;
    private ProgressBar mProgressBar;
    private String[] namaBarang;
    private String[] hargaBarang;
    private DuitkuPreferences duitkuPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.inquiry_activity);
        duitkuPreferences = new DuitkuPreferences(InquiryActivity.this);
        mInquiryPresenter = PresenterFactory.createInquiryFactory(this);
        clearPreferences(getApplicationContext());

        mInquiryPresenter.onInitView();

    }

    @Override
    public void initView()
    {
        Typeface fontRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        Typeface fontSemibold = Typeface.createFromAsset(getAssets(), "OpenSans-Semibold.ttf");
        mSpinnerNamaDanCodeBarang = (Spinner) findViewById(R.id.spinnerNameDanCodeBarang);
        mTextProduct = (TextView) findViewById(R.id.productValue);
        mTextDeskripsi = (TextView) findViewById(R.id.descriptionValue);
        mTextorderId = (TextView) findViewById(R.id.orderId);
        mTextHargaBarang = (TextView) findViewById(R.id.priceValue);
        mMerchantCode = (TextView) findViewById(R.id.merchantCodeValue);
        mButtonKePembayaran = (Button) findViewById(R.id.btn_pilih_pembayaran);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        View decorView = getWindow().getDecorView();

        //region NotificationBarSetting
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                getWindow().setStatusBarColor(getResources().getColor(R.color.primaryBlue));
        }
        else
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 14) {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 18) {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        //endregion

        Random random = new Random();

        mMerchantCode.setText(constant.merchantCode);
        mTextProduct.setText(getIntent().getStringExtra("order_detail"));
        mTextDeskripsi.setText(getIntent().getStringExtra("order_detail"));
        mTextHargaBarang.setText(Util.formatNumbering(getIntent().getStringExtra("nominal")));
        mTextorderId.setText(random.nextInt(1000000) + "");

        mMerchantCode.setTypeface(fontRegular);
        mTextProduct.setTypeface(fontRegular);
        mTextDeskripsi.setTypeface(fontRegular);
        mTextHargaBarang.setTypeface(fontRegular);
        mTextorderId.setTypeface(fontRegular);
        mButtonKePembayaran.setTypeface(fontSemibold);

        TextView productLabel = (TextView) findViewById(R.id.productLabel);
        TextView priceLabel = (TextView) findViewById(R.id.priceLabel);
        productLabel.setTypeface(fontRegular);
        priceLabel.setTypeface(fontRegular);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("token", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("token",getIntent().getStringExtra("token"));
        editor.commit();

        mButtonKePembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTextorderId.getText().toString().isEmpty()) {
                    Toast.makeText(InquiryActivity.this, "Order id belum diisi.", Toast.LENGTH_SHORT).show();

                } else {

                    mProgressBar.setVisibility(View.VISIBLE);
                    mButtonKePembayaran.setEnabled(false);

                    //region Inquiry
                    inquiry = new Inquiry();
                    inquiry.setAction(constant.statusInquiry);
                    inquiry.setMerchantCode(constant.merchantCode);
                    inquiry.setOrderId(mTextorderId.getText().toString());
                    inquiry.setAmount(getIntent().getStringExtra("nominal").toString());
                    inquiry.setProductDetail(getIntent().getStringExtra("order_detail").toString());
                    inquiry.setAdditionalParam("");
                    String sign = null;
                    try {
                        sign = Util.getSHA1(constant.statusInquiry + constant.apiKey + mTextorderId.getText().toString() + constant.merchantCode);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    inquiry.setSign(sign);

                    duitkuPreferences.saveInquiry(new Gson().toJson(inquiry));

                    Intent paymentMethodActivity = new Intent(InquiryActivity.this, PaymentMethodActivity.class);
//                    paymentMethodActivity.putExtra("reference", reference);
//                    paymentMethodActivity.putExtra("cp", getIntent().getStringExtra("cp"));
                    startActivity(paymentMethodActivity);
                    finish();
                }
            }
        });
    }

    public String getReference(String jsonData)throws JSONException
    {
        JSONObject data = new JSONObject(jsonData);
        String reference = data.getString("iPayGoReference");
        return reference;
    }

    @Override
    public void populateSpinner() {
        namaBarang = getResources().getStringArray(R.array.arr_nama_barang);
        hargaBarang = getResources().getStringArray(R.array.arr_harga);
//        deskripsi = getResources().getStringArray(R.array.arr_deskripsi_barang);
        InquiryBaseAdapter baseAdapter = new InquiryBaseAdapter(InquiryActivity.this, namaBarang, hargaBarang);
        mSpinnerNamaDanCodeBarang.setAdapter(baseAdapter);

        mTextDeskripsi.setText(namaBarang[mSpinnerNamaDanCodeBarang.getSelectedItemPosition()]);
        mTextHargaBarang.setText("Rp " + hargaBarang[mSpinnerNamaDanCodeBarang.getSelectedItemPosition()]);

        mSpinnerNamaDanCodeBarang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                inquiry = new Inquiry();
                inquiry.setAction(constant.statusInquiry);
                inquiry.setMerchantCode(constant.merchantCode);
                inquiry.setOrderId(mTextorderId.getText().toString());
                inquiry.setAmount(hargaBarang[mSpinnerNamaDanCodeBarang.getSelectedItemPosition()]);
                inquiry.setProductDetail(mTextDeskripsi.getText().toString());
                inquiry.setAdditionalParam("");
                String sign = null;
                try {
                    sign = Util.getMD5(constant.merchantCode + mTextorderId.getText().toString() + hargaBarang[mSpinnerNamaDanCodeBarang.getSelectedItemPosition()] + constant.apiKey);
                } catch (Exception e) {
                    Log.e("ERROR", "Response " + e);
                }

                inquiry.setSign(sign);

                duitkuPreferences.saveInquiry(new Gson().toJson(inquiry));

                mTextDeskripsi.setText(namaBarang[mSpinnerNamaDanCodeBarang.getSelectedItemPosition()]);
                mTextHargaBarang.setText("Rp " + hargaBarang[mSpinnerNamaDanCodeBarang.getSelectedItemPosition()]);

                Random random = new Random();
                mTextorderId.setText(random.nextInt(1000000) + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                inquiry = new Inquiry();

                inquiry.setAction(constant.statusInquiry);
                inquiry.setMerchantCode(constant.merchantCode);
                inquiry.setOrderId(mTextorderId.getText().toString());
                inquiry.setAmount(hargaBarang[mSpinnerNamaDanCodeBarang.getSelectedItemPosition()]);
                inquiry.setProductDetail(mTextDeskripsi.getText().toString());
                inquiry.setAdditionalParam("");
                String sign = null;
                try {
                    sign = Util.getSHA1(constant.statusInquiry + constant.apiKey + mTextorderId.getText().toString() + constant.merchantCode);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                inquiry.setSign(sign);

                duitkuPreferences.saveInquiry(new Gson().toJson(inquiry));

                mTextDeskripsi.setText(namaBarang[mSpinnerNamaDanCodeBarang.getSelectedItemPosition()]);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }
}

