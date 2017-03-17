package com.android.duitku.login.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.duitku.R;
import com.android.duitku.base.BaseActivity;
import com.android.duitku.base.PresenterFactory;
import com.android.duitku.checkoutpage.view.CheckoutActivity;
import com.android.duitku.inquiry.view.InquiryActivity;
import com.android.duitku.login.presenter.LoginDuitkuPresenter;
import com.android.duitku.model.Inquiry;
import com.android.duitku.model.InquiryResponse;
import com.android.duitku.model.Login;
import com.android.duitku.model.LoginResponse;
import com.android.duitku.paystatus.view.PayStatusActivity;
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

public class LoginDuitkuActivity extends BaseActivity implements LoginDuitkuView, View.OnClickListener {

    private Inquiry mInquiry;
    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnLogin;
    private Login mLogin;
    private EditText mHintUsername;
    private EditText mHintPassword;
    private Button mButtonHint;
    private LoginDuitkuPresenter mLoginDuitkuPresenter;
    private boolean isShowHint = false;
    private DuitkuPreferences duitkuPreferences;
    private int orderTotal;
    private ProgressBar mProgressBar;
    private LoginResponse mLoginResponse;
    private ProgressDialog mProgressDialog;
    private String emailClient;
    private String passClient;
    private String mToken;
    private static final String TAG = LoginDuitkuActivity.class.getSimpleName();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                getWindow().setStatusBarColor(getResources().getColor(R.color.notification_bar_brown));
        }
        else
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setContentView(R.layout.activity_login_duitku);

        duitkuPreferences = new DuitkuPreferences(LoginDuitkuActivity.this);
        mLoginDuitkuPresenter = PresenterFactory.createLoginFactory(this, getNetworkManager());
        mLoginDuitkuPresenter.onInitView();
        initToolbarView();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backarrow));
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    public void initView() {
        txtUsername = (EditText) findViewById(R.id.text_username);
        txtPassword = (EditText) findViewById(R.id.text_password);
        btnLogin = (Button) findViewById(R.id.button_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable())
                {
                    try
                    {
                        mProgressDialog = ProgressDialog.show(LoginDuitkuActivity.this, null, "Please wait..");
                        emailClient = txtUsername.getText().toString();
                        passClient = Util.getMD5(txtPassword.getText().toString());

                        JSONObject json = new JSONObject();
                        json.put("username", emailClient);
                        json.put("password", passClient);

                        String urlApi = constant.loginUrl;
                        OkHttpClient client = new OkHttpClient();

                        RequestBody body = RequestBody.create(JSON, json.toString());
                        Request request = new Request.Builder()
                                .url(urlApi)
                                .post(body)
                                .build();
                        Call call = client.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Request request, IOException e) {
                                Log.e("Failed to Request Login","Response " + e);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showErrorDialog("Failed to Login", "Please try again or contact customer service", "Ok");
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Response response) throws IOException {
                                String jsonData = response.body().string();
                                try {
//                                    Log.i(TAG, "Respons " + jsonData);
                                    mToken = getLoginToken(jsonData);
                                    getUserData(mToken);
//                                    Intent intent = new Intent(LoginDuitkuActivity.this, LoginDuitkuActivity.class);
//                                    intent.putExtra("auth", mToken);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mProgressDialog.dismiss();
                                        }
                                    });
//                                    startActivity(intent);
//                                    LoginDuitkuActivity.this.finish();
                                } catch (JSONException e) {
                                    Log.e(TAG, "Error " + e);
                                    try
                                    {
                                        JSONObject data = new JSONObject(jsonData);
                                        String message = data.getString("Message");
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mProgressDialog.dismiss();
                                                if(message.equals("Wrong username or password")) {
                                                    showErrorDialog(null, message, "Ok");
                                                }else{
                                                    showErrorDialog("Failed to Login", "Please contact customer service", "Ok");}
                                            }
                                        });
                                    }
                                    catch (JSONException ex)
                                    {
                                        Log.e("JSON Error", "Response " + e);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                showErrorDialog(null, "Sorry, the service is under maintenance", "Ok");
                                            }
                                        });
                                    }
                                }
                                //region Mungkin nanti kepake
                                //                                catch (IOException e) {
//                                    Log.e(TAG, "Error " + e);
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            showErrorDialogBackToInquiry(null, "Sorry, the service is under maintenance", "Ok");
//                                        }
//                                    });
//                                }
                                //endregion
                            }
                        });
                    }
                    catch (JSONException e)
                    {
                        Log.e(TAG,"Error Response " + e);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showErrorDialogBackToInquiry(null, "Sorry, the service is under maintenance", "Ok");
                            }
                        });
                    }
                }
                else
                {
                    showErrorDialogBackToInquiry("No Network", "There is no internet connection","Ok");
                }
            }
        });
//        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    //region OldLoginFlow
    @Override
    public void checkLoginResponse(LoginResponse loginResponse) {
        if (loginResponse != null) {
            mLoginResponse = loginResponse;
            duitkuPreferences.saveLogin(new Gson().toJson(mLogin));
            duitkuPreferences.saveLoginResponse(new Gson().toJson(loginResponse));
            System.out.println(loginResponse.getStatusCode());
            if (loginResponse.getStatusCode().equals("00")) {
                Inquiry inquiry = new Gson().fromJson(duitkuPreferences.getInquiry(), Inquiry.class);
                int item = Integer.parseInt(inquiry.getAmount().toString());
                int adminfee = duitkuPreferences.getAdminFee();
                orderTotal = item + adminfee;
                inquiry.setAmount(String.valueOf(orderTotal));
                //inquiry transaction
                mLoginDuitkuPresenter.onInquiryData(inquiry);

            } else {
                dismissSimpleProgressDialog();
                Toast.makeText(LoginDuitkuActivity.this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
            }
        } else {
            dismissSimpleProgressDialog();
            Toast.makeText(LoginDuitkuActivity.this, getString(R.string.null_data), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void emitInquiryResponse(InquiryResponse inquiryResponse) {
        dismissSimpleProgressDialog();
        if (inquiryResponse != null) {
            duitkuPreferences.saveInquiryResponse(new Gson().toJson(inquiryResponse));
            if (inquiryResponse.getStatusCode().equals("00")) {
                Intent intentPay = new Intent(LoginDuitkuActivity.this, PayStatusActivity.class);
                intentPay.putExtra(constant.isSufficientBalance,isSufficientBalance(mLoginResponse));
                startActivity(intentPay);
            } else {
                startActivity(new Intent(LoginDuitkuActivity.this, InquiryActivity.class));
                Toast.makeText(LoginDuitkuActivity.this, "Maaf, Pembayaran Gagal. Data ini telah digunakan", Toast.LENGTH_SHORT).show();
                finish();
            }

        }

    }
    private boolean isSufficientBalance(LoginResponse loginResponse) {
        InquiryResponse inquiryResponse = new Gson().fromJson(duitkuPreferences.getInquiryResponse(), InquiryResponse.class);
        boolean result = false;
        double doublePriceInquiry = Double.valueOf(inquiryResponse.getPrice().toString());
        double doublePriceLogin = Double.valueOf(loginResponse.getUserBalance().toString());
        if (doublePriceLogin >= doublePriceInquiry) {
            result = true;
        }
        return result;
    }



    //@Override
    public void onClick(View v)
    {
        //region OldCode
//        switch (v.getId()) {
//            case R.id.button_login:
//                showSimpleProgressDialog(null);
//                mLogin = new Login();
//                mLogin.setAction(constant.statusLogin);
//                mLogin.setUsername(txtUsername.getText().toString());
//                String passMD5 = Util.getMD5(txtPassword.getText().toString());
//                mLogin.setPass(passMD5);
//                mLogin.setMerchantCode(constant.merchantCode);
//                String concatSign = constant.statusLogin + constant.apiKey + txtUsername.getText().toString() + passMD5;
//                String signsha1 = "";
//                try {
//
//                    mLogin.setSign(Util.getSHA1(concatSign));
//                   signsha1=Util.getSHA1(concatSign);
//                } catch (NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//
//
//                mLoginDuitkuPresenter.onLoginProcess(mLogin);
//                break;
//        }
        //endregion
    }
    //endregion

    private String getLoginToken(String jsonData) throws JSONException{
        JSONObject LoginToken = new JSONObject(jsonData);
        String token = LoginToken.getString("token");

        return token;
    }

    private void getUserData(String token) throws JSONException
    {
        mInquiry = new Gson().fromJson(duitkuPreferences.getInquiry(), Inquiry.class);
        String reference = parseReference(getIntent().getStringExtra("info"));
        int amount = Integer.parseInt(mInquiry.getAmount());
        String urlApi = constant.userInfoUrl + token;
        OkHttpClient client = new OkHttpClient();

        Request requestUserInfo = new Request.Builder().url(urlApi).build();
        Call call = client.newCall(requestUserInfo);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("Failed request UserData", "Response " + e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showErrorDialog("Failed to request User Data", "Please contact customer service", "Ok");//showErrorDialogBackToInquiry(null, "Sorry, the service is under maintenance", "Ok");
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String jsonData = response.body().string();
                try
                {
                    String userBalance = getUserWalletAmount(jsonData).replace(".00","");
                    int balance = Integer.parseInt(userBalance);
                    if(balance < amount)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(LoginDuitkuActivity.this,PayStatusActivity.class);
                                intent.putExtra("reference",reference);
                                intent.putExtra("status","Insufficient fund");
                                intent.putExtra("amount",mInquiry.getAmount());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressDialog.dismiss();
                                        finish();
                                        startActivity(intent);
                                    }
                                });
                            }
                        });
                    }
                    else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try
                                {
                                    doTransaction(token,reference);
                                }
                                catch (JSONException e)
                                {
                                    Log.e("Error", "Response " + e);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            showErrorDialog("Failed to request User Data", "Please contact customer service", "Ok");//showErrorDialogBackToInquiry(null, "Sorry, the service is under maintenance", "Ok");
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
                catch (JSONException e)
                {
                    Log.e("JSONError ", "Response " + e);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showErrorDialog("Failed to request User Data", "Please contact customer service", "Ok");//showErrorDialogBackToInquiry(null, "Sorry, the service is under maintenance", "Ok");
                        }
                    });
                }
            }
        });
    }

    private void doTransaction(String token, String reference) throws JSONException
    {
        mInquiry = new Gson().fromJson(duitkuPreferences.getInquiry(), Inquiry.class);
        String merchantOrderId = getIntent().getStringExtra("reference");//mInquiry.getOrderId();
        String merchantCode = constant.merchantCode;
        String apiKey = constant.apiKey;
        String signature = Util.getMD5(merchantCode + merchantOrderId + reference + apiKey);

        JSONObject json = new JSONObject();
        json.put("token", token);
        json.put("reference", reference);
        json.put("merchantOrderId",merchantOrderId);
        json.put("merchantCode",merchantCode);
        json.put("signature",signature);

        String urlApi = constant.mobilePayUrl;
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, json.toString());
        Request request = new Request.Builder()
                .url(urlApi)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("Failed do transaction", "Response " + e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showErrorDialogBackToInquiry(null, "Sorry, the service is under maintenance", "Ok");
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String jsonData = response.body().string();
//                Log.i("ResponseUrl","Response " + jsonData);//for development only
                Intent intent = new Intent(LoginDuitkuActivity.this,PayStatusActivity.class);
                intent.putExtra("reference", reference);
                intent.putExtra("status",jsonData);
                intent.putExtra("amount",mInquiry.getAmount());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressDialog.dismiss();
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }

    private void showErrorDialog(String title, String message, String buttonText)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)//R.string.error_title)
                .setMessage(message)//R.string.error_message)
                .setPositiveButton(buttonText, null);//R.string.error_ok_button, null);
        builder.show();
    }
    private void showErrorDialogBackToInquiry(String title, String message, String buttonText)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)//R.string.error_title)
                .setMessage(message)//R.string.error_message)
                .setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(LoginDuitkuActivity.this, InquiryActivity.class);
                        startActivity(intent);
                    }
                });//R.string.error_ok_button, null);
        builder.show();
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

    public String parseReference(String jsonData) throws JSONException{
        JSONObject Info = new JSONObject(jsonData);
        String reference = Info.getString("reference");
        return reference;
    }

    private String getUserWalletAmount(String jsonData) throws JSONException
    {
        JSONObject userInfo = new JSONObject(jsonData);
        String reference = userInfo.getString("userBalance");
        return reference;
    }

    @Override
    public void onBackPressed() {
        finish();
//        startActivity(new Intent(this, InquiryActivity.class));
    }
}
