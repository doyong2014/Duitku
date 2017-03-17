package com.android.duitku.paystatus.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.duitku.R;
import com.android.duitku.base.BaseActivity;
import com.android.duitku.base.PresenterFactory;
import com.android.duitku.inquiry.view.InquiryActivity;
import com.android.duitku.model.Inquiry;
import com.android.duitku.model.InquiryResponse;
import com.android.duitku.model.Login;
import com.android.duitku.model.LoginResponse;
import com.android.duitku.model.Pay;
import com.android.duitku.model.PayResponse;
import com.android.duitku.paymentmethod.view.PaymentMethodActivity;
import com.android.duitku.paystatus.presenter.PayPresenter;
import com.android.duitku.utils.DuitkuPreferences;
import com.android.duitku.utils.Util;
import com.android.duitku.utils.constant;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by latifalbar on 11/19/2015.
 */
public class PayStatusActivity extends BaseActivity implements PayStatusView {

    private TextView txtTransactionId;
    private TextView txtJumlah;
    private LinearLayout linearLayoutTransactionDetail;
    private LinearLayout linearLayoutMisc;
    private DuitkuPreferences duitkuPreferences;
    private LoginResponse loginResponse;
    private InquiryResponse inquiryResponse;
    private Inquiry mInquiry;
    private Login login;
    private PayPresenter mPayPresenter;
    private Pay pay;
    private TextView txtPaymentStatus;
    private TextView txtPaymentDescription;
    private Button mBtnBack;
    private PayResponse mPayResponse = null;
    private String paymentStatus,reference,amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        setContentView(R.layout.success_pay);
        mPayPresenter = PresenterFactory.createPayFactory(this, getNetworkManager());
        mPayPresenter.onInitView();

        paymentStatus = getIntent().getStringExtra("status");
        reference =  getIntent().getStringExtra("reference");
        amount = getIntent().getStringExtra("amount");
        updateDisplayInformation(paymentStatus,reference);
        //region OldCode
        //        mInquiry = new Gson().fromJson(duitkuPreferences.getInquiry(), Inquiry.class);

//        preparePreferences();
//        if (duitkuPreferences.isUsingDuitku() && (getIntent().getBooleanExtra(constant.isSufficientBalance,false))){
//            showSimpleProgressDialog(null);
//            preparePayObject();
//            mPayPresenter.onProsesBayar(pay, true);
//        }else if (duitkuPreferences.isUsingDuitku() && !(getIntent()
//                .getBooleanExtra(constant.isSufficientBalance,false))){
//                showInfoPayment(getIntent().getBooleanExtra(constant.isSufficientBalance,false));
//        }else if (!duitkuPreferences.isUsingDuitku() && getIntent().getBooleanExtra(constant.successwebpayment,false)){
//            showInforPaymentFromRedirect();
//        }
        //endregion

    }

    @Override
    public void initView() {
        linearLayoutMisc = (LinearLayout) findViewById(R.id.linearLayoutMisc);
        linearLayoutTransactionDetail = (LinearLayout) findViewById(R.id.linearLayoutTransactionDetail);
        txtTransactionId = (TextView) findViewById(R.id.transIdValue);
        txtJumlah = (TextView) findViewById(R.id.transValue);
        txtPaymentStatus = (TextView) findViewById(R.id.txt_payment_status);
        txtPaymentDescription = (TextView) findViewById(R.id.txt_payment_description);
        linearLayoutMisc.setVisibility(View.INVISIBLE);
        mBtnBack = (Button) findViewById(R.id.btn_back);
//        mBtnBack.setOnClickListener(this);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(paymentStatus != null && reference != null)
                {
                    if(paymentStatus.replace("\"","").equals("Success"))
                    {
                        linearLayoutTransactionDetail.setVisibility(View.GONE);
                        linearLayoutMisc.setVisibility(View.VISIBLE);
                        mBtnBack.setVisibility(View.INVISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 3000);
//                        break;
                    }
                    else
                    {
                        Intent intentChooseMethodAgain = new Intent(PayStatusActivity.this,PaymentMethodActivity.class);
                        startActivity(intentChooseMethodAgain);
//                        break;
                    }
                }
            }
        });
    }

    private void updateDisplayInformation(String paymentStatus, String reference)
    {
        if(paymentStatus.equals("Insufficient fund"))
        {
            txtPaymentStatus.setText(getString(R.string.payment_failed));
            txtPaymentDescription.setText(getString(R.string.insufficient_balance));
            txtPaymentDescription.setVisibility(View.VISIBLE);
            txtTransactionId.setText(reference);
            txtJumlah.setText("Rp " + amount);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 5000);
        }
        if(paymentStatus.replace("\"","").equals("Success"))
        {
            txtPaymentStatus.setText(getString(R.string.payment_succeed));
            txtPaymentDescription.setText(getString(R.string.thank_you));
            txtPaymentDescription.setVisibility(View.VISIBLE);
            txtTransactionId.setText(reference);
            txtJumlah.setText("Rp " + amount);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 3000);
        }
        else
        {
            try
            {
//                JSONObject data = new JSONObject(paymentStatus.toString());
//                String status = "Failed";//data.getString("Message");
                txtPaymentStatus.setText(getString(R.string.payment_failed));
                txtPaymentDescription.setText("Harap hubungi Customer Service");
                txtPaymentDescription.setVisibility(View.VISIBLE);
                txtTransactionId.setText(reference);
                txtJumlah.setText("Rp " + amount);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 5000);
            }
            catch (Exception e)
            {
                Log.e("Error","Response " + e);
            }
        }
    }
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.btn_back:
//                if(paymentStatus != null && reference != null)
//                {
//                    if(paymentStatus.replace("\"","").equals("Success"))
//                    {
//                        linearLayoutTransactionDetail.setVisibility(View.GONE);
//                        linearLayoutMisc.setVisibility(View.VISIBLE);
//                        mBtnBack.setVisibility(View.INVISIBLE);
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                        /* Create an Intent that will start the Menu-Activity. */
//                                startActivity(new Intent(PayStatusActivity.this, InquiryActivity.class));
//                            }
//                        }, 3000);
//                        break;
//                    }
//                    else
//                    {
//                        Intent intentChooseMethodAgain = new Intent(PayStatusActivity.this,PaymentMethodActivity.class);
//                        startActivity(intentChooseMethodAgain);
//                        break;
//                    }
//                }
////                if (mPayResponse != null){
////                    if (mPayResponse.getStatusCode().equals("00")){
//////                        super.onBackPressed();
//////                        startActivity(new Intent(PayStatusActivity.this, InquiryActivity.class));
////                        linearLayoutTransactionDetail.setVisibility(View.GONE);
////                        linearLayoutMisc.setVisibility(View.VISIBLE);
////                        mBtnBack.setVisibility(View.INVISIBLE);
////                        new Handler().postDelayed(new Runnable() {
////                            @Override
////                            public void run() {
////                        /* Create an Intent that will start the Menu-Activity. */
////                                startActivity(new Intent(PayStatusActivity.this, InquiryActivity.class));
////                            }
////                        }, 3000);
////                        break;
////                    }else {
////                        Intent intentChooseMethodAgain = new Intent(PayStatusActivity.this,PaymentMethodActivity.class);
////                        duitkuPreferences.setIsLastPaymentFailed(true);
////                        startActivity(intentChooseMethodAgain);
////                        break;
////                    }
////
////                }else if(getIntent().getBooleanExtra(constant.successwebpayment,false)){
////                    linearLayoutTransactionDetail.setVisibility(View.GONE);
////                    linearLayoutMisc.setVisibility(View.VISIBLE);
////                    mBtnBack.setVisibility(View.INVISIBLE);
////                    new Handler().postDelayed(new Runnable() {
////                        @Override
////                        public void run() {
////                        /* Create an Intent that will start the Menu-Activity. */
////                            startActivity(new Intent(PayStatusActivity.this, InquiryActivity.class));
////                        }
////                    }, 3000);
////                    break;
////                }
////                else {
////                    Intent intentChooseMethodAgain = new Intent(PayStatusActivity.this,PaymentMethodActivity.class);
//////                    duitkuPreferences.setIsLastPaymentFailed(true);
////                    startActivity(intentChooseMethodAgain);
////                    break;
////                }
//
//
//        }
//    }

    //region OldCode
    private void preparePreferences() {
//        duitkuPreferences = new DuitkuPreferences(this);
//        loginResponse = new Gson().fromJson(duitkuPreferences.getLoginResponse(), LoginResponse.class);
//        login = new Gson().fromJson(duitkuPreferences.getLogin(), Login.class);
//        inquiryResponse = new Gson().fromJson(duitkuPreferences.getInquiryResponse(), InquiryResponse.class);

    }

    private void preparePayObject() {
//        pay = new Pay();
//        pay.setAction(constant.statusPay);
//        pay.setUserId(loginResponse.getUserId());
//        pay.setOrderId(inquiryResponse.getOrderId());
//        pay.setMerchantCode(login.getMerchantCode());
//        String concatSign = constant.statusPay + constant.apiKey
//                + loginResponse.getUserId() + inquiryResponse.getOrderId() + login.getMerchantCode();
//        try {
//            pay.setSign(Util.getSHA1(concatSign));
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
    }



    @Override
    public void emitPayResponse(PayResponse payResponse) {
//        duitkuPreferences.savePayResponse(new Gson().toJson(payResponse));
//        if (payResponse != null) {
//            dismissSimpleProgressDialog();
//            mPayResponse = payResponse;
//            txtPaymentStatus.setText(getString(R.string.payment_succeed));
//            txtPaymentDescription.setText(getString(R.string.thank_you));
//            txtTransactionId.setText(payResponse.getTrxId());
//            txtJumlah.setText("Rp" + inquiryResponse.getPrice());
//        } else {
//            dismissSimpleProgressDialog();
//            duitkuPreferences.setIsLastPaymentFailed(true);
//            txtPaymentStatus.setText("Pembayaran Gagal");
//            txtPaymentStatus.setTextColor(getResources().getColor(R.color.font_red));
//            Toast.makeText(this, getString(R.string.null_data), Toast.LENGTH_SHORT).show();
//        }

    }

    private void showInforPaymentFromRedirect(){
//        txtPaymentStatus.setText(getString(R.string.payment_succeed));
//        txtPaymentDescription.setText(getString(R.string.thank_you));
//        txtTransactionId.setText(inquiryResponse.getTrxId());
//        txtJumlah.setText("Rp" + inquiryResponse.getPrice());
    }


    private void showInfoPayment(boolean isSufficientBalance){
//        if (isSufficientBalance) {
//            txtPaymentStatus.setText(getString(R.string.payment_succeed));
//            txtPaymentDescription.setText(getString(R.string.thank_you));
//            txtTransactionId.setText(mPayResponse.getTrxId());
//            txtJumlah.setText("Rp" + inquiryResponse.getPrice());
//        }else if (!isSufficientBalance){
//            txtPaymentStatus.setText(getString(R.string.payment_failed));
//            txtPaymentDescription.setText(getString(R.string.insufficient_balance));
//            txtPaymentDescription.setVisibility(View.VISIBLE);
//            txtTransactionId.setText(inquiryResponse.getTrxId());
//            txtJumlah.setText("Rp" + inquiryResponse.getPrice());
//            txtPaymentStatus.setTextColor(getResources().getColor(R.color.font_red));
//        }
//        else {
//            InquiryResponse inquiryResponse = new Gson()
//                    .fromJson(duitkuPreferences.getInquiryResponse(), InquiryResponse.class);
//            txtTransactionId.setText(inquiryResponse.getTrxId());
//            txtJumlah.setText("Rp" + inquiryResponse.getPrice());
//        }
    }


    @Override
    public void emitResponseFromRedirect() {
//        InquiryResponse inquiryResponse = new Gson()
//                .fromJson(duitkuPreferences.getInquiryResponse(),InquiryResponse.class);
//        txtTransactionId.setText(inquiryResponse.getTrxId());
//        txtJumlah.setText("Rp" + inquiryResponse.getPrice());
    }
    //endregion


}
