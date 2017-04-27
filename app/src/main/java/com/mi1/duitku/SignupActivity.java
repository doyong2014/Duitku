package com.mi1.duitku;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.Common.CommonFunction;
import com.mi1.duitku.Common.Constant;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.StoringMechanism;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SignupActivity extends BaseActivity{

    private String userName;
    private String password;
    private String emailAddress;
    private String phoneNumber;

    private ProgressDialog progress;
    private EditText etUserName;
    private EditText etPhoneNumber;
    private EditText etEmailAddress;
    private EditText etPassword;
    private EditText etConfirpassword;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        etUserName = (EditText)findViewById(R.id.edt_full_name);
        etEmailAddress = (EditText)findViewById(R.id.edt_sign_email);
        etPhoneNumber = (EditText) findViewById(R.id.edt_sign_phone);
        etPassword = (EditText)findViewById(R.id.edt_sign_password);
        etConfirpassword = (EditText)findViewById(R.id.edt_sign_confirm_pwd);
        tvError = (TextView)findViewById(R.id.txt_sign_error);

        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.wait));
        progress.setCanceledOnTouchOutside(false);

        Button btnSign = (Button)findViewById(R.id.btn_sign);
        btnSign.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateSignup()) {
                    signup();
                }
            }
        });

    }

    private boolean validateSignup(){

        tvError.setVisibility(View.INVISIBLE);
        userName = etUserName.getText().toString();
        password = etPassword.getText().toString();
        emailAddress = etEmailAddress.getText().toString();
        phoneNumber = etPhoneNumber.getText().toString();
        String confirmpassword = etConfirpassword.getText().toString();

        if (userName.isEmpty()){
            etUserName.setError(getString(R.string.error_null_username));
            etUserName.requestFocus();
            return false;
        }

        if (emailAddress.isEmpty()){
            etEmailAddress.setError(getString(R.string.error_null_email));
            etEmailAddress.requestFocus();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
            etEmailAddress.setError(getString(R.string.error_invalid_email));
            etEmailAddress.requestFocus();
            return false;
        }

        if (phoneNumber.isEmpty()) {
            dispError(getString(R.string.error_null_phone));
            etPhoneNumber.requestFocus();
            return false;
        }

        if (password.isEmpty()){
            etPassword.setError(getString(R.string.error_null_password));
            etPassword.requestFocus();
            return false;
        }

        if (password.compareTo(confirmpassword) != 0){
            etPassword.setError(getString(R.string.error_incorrect_password));
            etPassword.requestFocus();
            return false;
        }

        hideKeyboard();
        return true;
    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void signup(){

        String[] params = new String[5];
        params[0] = emailAddress; //email
        params[1] = phoneNumber; //phone
        params[2] = CommonFunction.md5(password); //password
        params[3] = userName; //fullName
        params[4] = Constant.COMMUNITY_CODE;; //community_code
        SignupAsync _signupAsync = new SignupAsync();
        _signupAsync.execute(params);
    }

    private void dispError(String error){
        tvError.setText(error);
        tvError.setVisibility(View.VISIBLE);
    }

    public class SignupAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            progress.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... param) {

            String result = null;

            try {

                URL url = new URL(Constant.SIGNUP_PAGE);

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email", param[0]);
                    jsonObject.put("phoneNumber", param[1]);
                    jsonObject.put("password", param[2]);
                    jsonObject.put("name", param[3]);
                    jsonObject.put("community_code", param[4]);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000); /* milliseconds */
                conn.setConnectTimeout(15000); /* milliseconds */
                conn.setUseCaches(false);
                conn.setRequestProperty("content-type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                wr.write(jsonObject.toString());
                wr.flush();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();
                    String str;

                    while((str = reader.readLine()) != null){
                        builder.append(str+"\n");
                    }

                    result = builder.toString();
                } else {
                    result = String.valueOf(conn.getResponseCode());
                }

            } catch (MalformedURLException e){
                //Log.e("oasis", e.toString());
            } catch (IOException e) {
                //Log.e("oasis", e.toString());
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            progress.dismiss();

            if (result == null){
                dispError(getString(R.string.error_failed_connect));
                return;
            }

            try {

                JSONObject jsonObj = new JSONObject(result);
                String statusCode = jsonObj.getString(Constant.JSON_STATUS_CODE);
                if (statusCode.equals("00")){
                    initQBFramework();
                    signUpQB();
                } else {
                    String status = jsonObj.getString(Constant.JSON_STATUS_MESSAGE);
                    dispError(status);
                }

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("error", e.getMessage());
            }
        }
    }

    private void showDialog() {

        MaterialDialog mDialog = new MaterialDialog.Builder(this)
                .content(getString(R.string.alert_sign))
                .positiveText("OK")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(SignupActivity.this, VerifyCodeActivity.class);
                        startActivity(intent);
                        SignupActivity.this.finish();
                    }
                }).build();

        mDialog.show();
    }

    private void signUpQB() {
        QBUser qbUser = new QBUser(AppGlobal._userInfo.phoneNumber, Constant.QB_ACCOUNT_PASS);
        qbUser.setFullName(AppGlobal._userInfo.name);
        qbUser.setEmail(AppGlobal._userInfo.email);
        QBUsers.signUp(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                showDialog();
            }

            @Override
            public void onError(QBResponseException e) {
                progress.dismiss();
                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initQBFramework() {
        QBSettings.getInstance().setStoringMehanism(StoringMechanism.UNSECURED);
        QBSettings.getInstance().init(getApplicationContext(), Constant.QB_APP_ID, Constant.QB_AUTH_KEY, Constant.QB_AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(Constant.QB_ACCOUNT_KEY);
        QBSettings.getInstance().setEnablePushNotification(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.actionbar_bg));

        getMenuInflater().inflate(R.menu.menu_sign, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if (validateSignup()) {
                signup();
            }
        } else if (id == android.R.id.home) {
            SignupActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

