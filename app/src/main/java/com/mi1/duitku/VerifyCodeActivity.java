package com.mi1.duitku;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mi1.duitku.Common.CommonFunction;
import com.mi1.duitku.Common.Constant;
import com.mi1.duitku.Common.UserInfo;
import com.mi1.duitku.Main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class VerifyCodeActivity extends AppCompatActivity {

    private String verifyCode;
    private String phoneNumber;

    private EditText etVerifyCode;
    private TextView txtError;
    private ProgressDialog progressDialog;

      @Override
    protected void onCreate(Bundle savedInstanceState) {

          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_verify_code);

          Button btnVerify = (Button)findViewById(R.id.btn_verify);
          btnVerify.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  hideKeyboard();

                  if(validateCode()) {
                      VerifyCode();
                  }
              }
          });

          etVerifyCode = (EditText)findViewById(R.id.edt_verify_code);
          txtError = (TextView)findViewById(R.id.txt_verify_error);

          TextView resubmitCode = (TextView)findViewById(R.id.txt_resubmit);
          resubmitCode.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  resubmitCode();
              }
          });

          progressDialog = new ProgressDialog(this);
          progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    private boolean validateCode() {

        txtError.setVisibility(View.INVISIBLE);
        verifyCode = etVerifyCode.getText().toString();
        phoneNumber = UserInfo.mPhoneNumber;

        if (verifyCode.isEmpty()) {
            dispError(getString(R.string.error_null_code));
            etVerifyCode.requestFocus();
            return false;
        }
        return true;
    }

    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void dispError(String error){
        txtError.setText(error);
        txtError.setVisibility(View.VISIBLE);
    }

    private void resubmitCode() {

        phoneNumber = UserInfo.mPhoneNumber;
        String[] params = new String[2];
        params[0] = phoneNumber;
        params[1] = Constant.COMMUNITY_CODE;
        resubmitCodeAsync _resubmitCodeAsync = new resubmitCodeAsync();
        _resubmitCodeAsync.execute(params);
    }

    private void VerifyCode(){

        String[] params = new String[3];
        params[0] = phoneNumber;
        params[1] = verifyCode;
        params[2] = Constant.COMMUNITY_CODE;
        verifyCodeAsync _verifyCodeAsync = new verifyCodeAsync();
        _verifyCodeAsync.execute(params);
    }

    public class verifyCodeAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            progressDialog.setMessage("Verifying code...");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... param) {

            String result = null;

            try {

                URL url = new URL(Constant.VERIFY_CODE_PAGE);

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("phoneNumber", param[0]);
                    jsonObject.put("otpCode", param[1]);
                    jsonObject.put("community_code", param[2]);

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

                if (success.equals("SUCCESS")){String token = jsonObj.getString(Constant.JSON_TOKEN);
                    String email = jsonObj.getString(Constant.JSON_EMAIL);
                    String userBalance = jsonObj.getString(Constant.JSON_USER_BALANCE);
                    String vaNumber = jsonObj.getString(Constant.JSON_VA_NUMBER);
                    String vaNumberPermata = jsonObj.getString(Constant.JSON_VA_NUMBER_PERMATA);
                    String username = jsonObj.getString(Constant.JSON_USER_NAME);
                    String phoneNum = jsonObj.getString(Constant.JSON_PHONE_NUM);
                    String userPic = jsonObj.getString(Constant.JSON_PIC_URL);

                    setUserInfo(token, email, userBalance, vaNumber, vaNumberPermata, username, phoneNum, userPic);
                    Intent intent = new Intent(VerifyCodeActivity.this, MainActivity.class);
                    startActivity(intent);
                    HomeActivity._instance.finish();
                    VerifyCodeActivity.this.finish();
                } else {
                    dispError(success);
                }

            } catch (Exception e) {
                // TODO: handle exception
                //Log.e("oasis", e.toString());
            }
        }
    }

    private void setUserInfo(String token, String email, String balance, String vaNumber, String vaNumberPermata,
                             String username, String phoneNum, String userPic) {
        UserInfo.mToken = token;
        UserInfo.mEmail = email;
        UserInfo.mUserBalance = balance;
        UserInfo.mVaNumber = vaNumber;
        UserInfo.mVaNumberPermata = vaNumberPermata;
        UserInfo.mUserName = username;
        UserInfo.mPhoneNumber = phoneNum;
        UserInfo.mPicUrl = userPic;
    }

    public class resubmitCodeAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            progressDialog.setMessage("Resubmit fetch code...");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... param) {

            String result = null;

            try {

                URL url = new URL(Constant.RESUBMIT_CODE_PAGE);

                StringBuilder params = new StringBuilder(32);
                params.append("phoneNumber=").append(param[0]);
                params.append("&community_code=").append(param[1]);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000); /* milliseconds */
                conn.setConnectTimeout(15000); /* milliseconds */
                conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                wr.write(params.toString());
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
                    //VerifyCodeActivity.this.finish();
                } else {
                    dispError(success);
                }

            } catch (Exception e) {
                // TODO: handle exception
                //Log.e("oasis", e.toString());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));

        getMenuInflater().inflate(R.menu.menu_recovery, menu);

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
            if(validateCode()) {
                VerifyCode();
            }
        } else if (id == android.R.id.home) {
            VerifyCodeActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

