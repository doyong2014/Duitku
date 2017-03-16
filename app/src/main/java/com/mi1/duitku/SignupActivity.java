package com.mi1.duitku;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mi1.duitku.Common.CommonFunction;
import com.mi1.duitku.Common.Constant;
import com.mi1.duitku.Common.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignupActivity extends AppCompatActivity{

    private String mUserName;
    private String mPassword;
    private String mEmailAddress;
    private String mPhoneNumber;

    private ProgressDialog progressDialog;
    private EditText etUserName;
    private EditText etPhoneNumber;
    private EditText etEmailAddress;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private TextView txtError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        etUserName = (EditText)findViewById(R.id.edt_full_name);
        etEmailAddress = (EditText)findViewById(R.id.edt_sign_email);
        etPhoneNumber = (EditText) findViewById(R.id.edt_sign_phone);
        etPassword = (EditText)findViewById(R.id.edt_sign_password);
        etConfirmPassword = (EditText)findViewById(R.id.edt_sign_confirm_pwd);
        txtError = (TextView)findViewById(R.id.txt_sign_error);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        Button btnSign = (Button)findViewById(R.id.btn_sign);
        btnSign.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard();

                if (validateSignup()) {
                    signup();
                }
            }
        });

    }

    private boolean validateSignup(){

        txtError.setVisibility(View.INVISIBLE);
        mUserName = etUserName.getText().toString();
        mPassword = etPassword.getText().toString();
        mEmailAddress = etEmailAddress.getText().toString();
        mPhoneNumber = etPhoneNumber.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        if (mUserName.isEmpty()){
            dispError(getString(R.string.error_null_username));
            etUserName.requestFocus();
            return false;
        }

        if (mEmailAddress.isEmpty()){
            dispError(getString(R.string.error_null_email));
            etEmailAddress.requestFocus();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mEmailAddress).matches()){
            dispError(getString(R.string.error_invalid_email));
            etEmailAddress.requestFocus();
            return false;
        }

        if (mPhoneNumber.isEmpty()) {
            dispError(getString(R.string.error_null_phone));
            etPhoneNumber.requestFocus();
            return false;
        }

        if (mPassword.isEmpty()){
            dispError(getString(R.string.error_null_password));
            etPassword.requestFocus();
            return false;
        }

        if (mPassword.compareTo(confirmPassword) != 0){
            dispError(getString(R.string.error_incorrect_password));
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void signup(){

        String[] params = new String[5];
        params[0] = mEmailAddress; //email
        params[1] = mPhoneNumber; //phone
        params[2] = CommonFunction.md5(mPassword); //password
        params[3] = mUserName; //name
        params[4] = Constant.COMMUNITY_CODE;; //community_code
        SignupAsync _signupAsync = new SignupAsync();
        _signupAsync.execute(params);
    }

    private void dispError(String error){
        txtError.setText(error);
        txtError.setVisibility(View.VISIBLE);
    }

    public class SignupAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            progressDialog.setMessage("SignUping...");
            progressDialog.show();
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

            String success;
            progressDialog.dismiss();

            if (result == null){
                dispError(getString(R.string.error_failed_connect));
                return;
            }

            try {
                JSONObject jsonObj = new JSONObject(result);

                success = jsonObj.getString(Constant.JSON_STATUS_MESSAGE);

                if (success.equals("SUCCESS")){
                    setUserInfo();
                    showDialog();
                } else {
                    dispError(success);
                }


            } catch (Exception e) {
                // TODO: handle exception
                //Log.e("oasis", e.toString());
            }
        }
    }

    private void setUserInfo() {
        UserInfo.mEmail = mEmailAddress;
        UserInfo.mPhoneNumber = mPhoneNumber;
        UserInfo.mUserName = mUserName;
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

