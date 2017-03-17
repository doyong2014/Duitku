package com.android.duitku.paymentmethod.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.duitku.R;
import com.android.duitku.base.BaseActivity;
import com.android.duitku.base.PresenterFactory;
import com.android.duitku.checkoutpage.presenter.CheckoutPresenter;
import com.android.duitku.checkoutpage.view.CheckoutActivity;
import com.android.duitku.inquiry.view.InquiryActivity;
import com.android.duitku.login.view.LoginDuitkuActivity;
import com.android.duitku.model.Inquiry;
//import com.android.duitku.model.PGClick;
import com.android.duitku.model.InquiryResponse;
import com.android.duitku.model.PaymentMethod;
import com.android.duitku.paymentmethod.presenter.PaymentMethodPresenter;
import com.android.duitku.redirectbankpage.view.RedirectBankPage;
import com.android.duitku.utils.DuitkuPreferences;
import com.android.duitku.utils.InquiryTransaction;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


import java.io.IOException;

/**
 * Created by latifalbar on 11/16/2015.
 */
public class PaymentMethodActivity extends BaseActivity implements PaymentMethodView {

    //region Initialize
    private RecyclerView rvBank;
    private PaymentMethodPresenter mPaymentMethodPresenter;
    private String[] txtBank;
    private PaymentMethod[] mPaymentMethods;
    private TextView mTxtTotalPrice;
    private TextView mTxtPrice;
    private TextView mTxtOrder;
    private TextView mTxtMerchant;
    private TextView mTxtAdmin;
    private TextView mReference;
    private TextView mBankName;
    private Inquiry mInquiry;
    private DuitkuPreferences duitkuPreferences;
    private Inquiry inquiry;
    private String[] imgBank;
    private ProgressDialog mProgressDialog;
    private Button mButtonBayar, mButtonLeft, mButtonRight;

    //endregion

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = PaymentMethodActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //region set notification color to actionbar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                getWindow().setStatusBarColor(getResources().getColor(R.color.primaryBlue));
//            getWindow().setStatusBarColor(getResources().getColor(R.color.notification_bar_green));
        }
        else
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        //endregion

        setContentView(R.layout.activity_payment_method);

        duitkuPreferences = new DuitkuPreferences(PaymentMethodActivity.this);
        inquiry = new Gson().fromJson(duitkuPreferences.getInquiry(), Inquiry.class);
        mPaymentMethodPresenter = PresenterFactory.createPaymentMethodFactory(this);
        mPaymentMethodPresenter.onInitView();
        mPaymentMethodPresenter.onPopulateList();
    }

    @Override
    public void initView() {

        //region AssignView
        int adminFee = 0;
        mTxtTotalPrice = (TextView) findViewById(R.id.totalValue);//.txt_total_price);
        mTxtPrice = (TextView) findViewById(R.id.priceValue);
        mTxtMerchant = (TextView)findViewById(R.id.merchantValue);
        mTxtAdmin = (TextView)findViewById(R.id.administrationValue);
        mTxtOrder = (TextView)findViewById(R.id.orderValue);
        mBankName = (TextView) findViewById(R.id.pg_name);
        mReference = (TextView)findViewById(R.id.referenceValue);
        mButtonBayar = (Button)findViewById(R.id.btn_bayar);
        mButtonLeft = (Button)findViewById(R.id.btnSlideLeft);
        mButtonRight = (Button)findViewById(R.id.btnSlideRight);
        TextView orderLabel = (TextView)findViewById(R.id.orderLabel);
        TextView priceLabel = (TextView)findViewById(R.id.priceLabel);
        TextView referenceLabel = (TextView)findViewById(R.id.referenceLabel);
        TextView admLabel = (TextView)findViewById(R.id.administrationLabel);
        TextView totalLabel = (TextView) findViewById(R.id.totalLabel);
        rvBank = (RecyclerView) findViewById(R.id.rvBank);

        //region set Typeface
        Typeface fontRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        Typeface fontSemibold = Typeface.createFromAsset(getAssets(), "OpenSans-Semibold.ttf");
        orderLabel.setTypeface(fontRegular);
        priceLabel.setTypeface(fontRegular);
        referenceLabel.setTypeface(fontRegular);
        admLabel.setTypeface(fontRegular);
        totalLabel.setTypeface(fontRegular);
        mTxtTotalPrice.setTypeface(fontRegular);
        mTxtPrice.setTypeface(fontRegular);
        mTxtMerchant.setTypeface(fontRegular);
        mTxtAdmin.setTypeface(fontRegular);
        mTxtOrder.setTypeface(fontRegular);
        mReference.setTypeface(fontRegular);
        mButtonBayar.setTypeface(fontSemibold);
        //endregion

        //endregion

        inquiry = new Gson().fromJson(duitkuPreferences.getInquiry(), Inquiry.class);

        //region AssignInitialValue
        mTxtTotalPrice.setText("Rp " + (Integer.parseInt(inquiry.getAmount()) + adminFee));
        mTxtPrice.setText("Rp " + inquiry.getAmount());
        mTxtAdmin.setText("Rp " + adminFee);
        mTxtOrder.setText(inquiry.getProductDetail());
//        mReference.setText(getIntent().getStringExtra("reference"));//inquiry.getOrderId());
        mTxtMerchant.setText(constant.merchantName);
        //endregion
        mProgressDialog = ProgressDialog.show(PaymentMethodActivity.this, null, "Mohon tunggu..");
        mButtonBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isNetworkAvailable()) {
                    showErrorDialog(null, "Koneksi internet tidak tersedia", "Ok");
                    return;
                }

                String bankNames = mBankName.getText().toString();
                mProgressDialog = ProgressDialog.show(PaymentMethodActivity.this, null, "Mohon tunggu..");
                for (int i = 0; i < mPaymentMethods.length; i++) {
                    if(bankNames.equals("duitku"))
                    {
                        showErrorDialogStay(null, "Mohon pilih metode pembayaran", "Ok");
                        break;
                    }
                    if (bankNames.equals(mPaymentMethods[i].getName().replace("FASPAY ", "").replace("CODAPAY-", "").toString())) {
                        setIntentRedirect(i, 0);
                    }
                }
            }
        });
    }


    @Override
    public void populateListView() {
        if (!isNetworkAvailable()) {
            showErrorDialog(null, "Koneksi internet tidak tersedia", "Ok");
            return;
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;

        String urlPGList = constant.pgListUrl; //+ constant.merchantCode;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(urlPGList).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("Request Failed","Response " + e);
                showErrorDialog(null, "Maaf, Layanan sedang dalam perbaikan", "Ok");
                mProgressDialog.dismiss();
                return;
            }

            @Override
            public void onResponse(Response response) throws IOException
            {
                String jsonData = response.body().string(); //Log.i(TAG, "Response " + jsonData); //for testing only

                try
                {
                    //region AssignPGAdapterData
                    PaymentMethod[] paymentMethods = parsePaymentMethod(jsonData);
                    mPaymentMethods = paymentMethods;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PGAdapter pgAdapter = new PGAdapter(PaymentMethodActivity.this, imgBank, txtBank, paymentMethods, width);
                            rvBank.setAdapter(pgAdapter);

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PaymentMethodActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            rvBank.setLayoutManager(layoutManager);
                            rvBank.setHasFixedSize(true);
                            mProgressDialog.dismiss();
                        }
                    });
                    //endregion
                }
                catch (JSONException e)
                {
                    Log.e(TAG, "Error " + e);//Monitor the error
                    try
                    {
                        //region createErrorDialog
                        JSONObject data = new JSONObject(jsonData);
                        String message = data.getString("Message");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showErrorDialog(message, "Mohon hubungi layanan pelanggan", "Ok");

                            }
                        });
                        //endregion
                    }
                    catch (JSONException ex)
                    {
                        Log.e(TAG, "Error " + e);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showErrorDialog(null, "Maaf, Layanan sedang dalam perbaikan", "Ok");
                            }
                        });
                    }
                }
            }
        });
    }

    private void setIntentRedirect(int position, int adminFee)
    {
        mInquiry = new Gson().fromJson(duitkuPreferences.getInquiry(), Inquiry.class);
        getWebLink(position);
    }

    public void getWebLink(int position)
    {
//        String signature = Util.getMD5(constant.merchantCode + getIntent().getStringExtra("reference") + mInquiry.getAmount().toString() + constant.apiKey);
        String url = constant.inquiryUrl;
        try
        {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("token", 0);
            String token = pref.getString("token",null);
            //region requestInquiry
            JSONObject jsonInquiry = new JSONObject();
            jsonInquiry.put("amount",mInquiry.getAmount().toString());
            jsonInquiry.put("pgCode", getPgByPosition(position));
            jsonInquiry.put("token", token);
            jsonInquiry.put("community_code", constant.COMMUNITY_CODE);

            RequestBody body = RequestBody.create(JSON, jsonInquiry.toString());
            Request request = new Request.Builder().url(url).post(body).build();

            OkHttpClient client = new OkHttpClient();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.e("Error Request Inquiry", "Response " + e);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showErrorDialog("Failed Request Inquiry", "Mohon hubungi layanan pelanggan", "Ok");
                        }
                    });
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String jsonData = response.body().string();
                    try
                    {
                        checkReturnDataIsValid(jsonData);

//                        Log.i(TAG, "Respons " + jsonData);//for testing only
                        String pg = getPgByPosition(position);
                        if(pg.equals("WW"))
                        {
                            //region isUsingDuitkuWallet
                            Intent intent = new Intent(PaymentMethodActivity.this, LoginDuitkuActivity.class);
                            intent.putExtra("info",jsonData);
                            intent.putExtra("reference",getIntent().getStringExtra("reference"));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressDialog.dismiss();
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            //endregion
                        }
                        else
                        {
                            //region isUsingOtherPG
                            Intent intent = new Intent(PaymentMethodActivity.this, RedirectBankPage.class);
                            intent.putExtra("info",jsonData);
                            intent.putExtra("amount",mInquiry.getAmount().toString());
                            intent.putExtra("reference",getIntent().getStringExtra("reference"));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressDialog.dismiss();
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            //endregion
                        }
                    }
                    catch (JSONException e)
                    {
                        Log.e("JSON Error","Response " + e);
                        try
                        {
                            JSONObject data = new JSONObject(jsonData);
                            String message = data.getString("Message");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showErrorDialog(message, "Mohon hubungi layanan pelanggan", "Ok");
                                }
                            });
                        }
                        catch (JSONException ex) {
                            Log.e("JSON Error", "Response " + ex);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showErrorDialog(null, "Maaf, Layanan sedang dalam perbaikan", "Ok");
                                }
                            });
                        }
                    }
                }
            });
            //endregion
        }
        catch (JSONException e)
        {
            Log.e(TAG,"Response " + e);
            showErrorDialog(null, "Maaf, Layanan sedang dalam perbaikan", "Ok");
        }
    }

    public String getPgByPosition(int position) {
        String pg = "";
        pg = mPaymentMethods[position].getCode().toString();
        return pg;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected())
        {
            isAvailable = true;
        }
        return isAvailable;
    }

    public void checkReturnDataIsValid(String jsonData)throws JSONException
    {
        JSONObject data = new JSONObject(jsonData);
        String url = data.getString("paymentUrl");
    }

    private void showErrorDialog(String title, String message, String buttonText)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
        builder.show();
    }
    private void showErrorDialogStay(String title, String message, String buttonText)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mProgressDialog.dismiss();
                        return;
                    }
                });
        builder.show();
    }

    private PaymentMethod[] parsePaymentMethod(String jsonData) throws JSONException
    {
        JSONArray pgArray = new JSONArray(jsonData);

        PaymentMethod[] paymentMethods = new PaymentMethod[pgArray.length()];
        for(int i = 0;i < pgArray.length();i++)
        {
            JSONObject pg = pgArray.getJSONObject(i);
            PaymentMethod paymentMethod = new PaymentMethod();
            paymentMethod.setName(pg.getString("name"));
            paymentMethod.setCode(pg.getString("pgCode"));
            paymentMethod.setFee(pg.getString("pgFee"));

            paymentMethods[i] = paymentMethod;
        }

        return paymentMethods;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
