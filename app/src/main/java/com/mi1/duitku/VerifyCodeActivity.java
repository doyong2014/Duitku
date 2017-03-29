package com.mi1.duitku;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mi1.duitku.Common.AppGlobal;
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
    private TextView tvError;
    private ProgressDialog progress;

      @Override
    protected void onCreate(Bundle savedInstanceState) {

          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_verify_code);

          Button btnVerify = (Button)findViewById(R.id.btn_verify);
          btnVerify.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if(validateCode()) {
                      VerifyCode();
                  }
              }
          });

          phoneNumber = AppGlobal._userInfo.phoneNumber;
          etVerifyCode = (EditText)findViewById(R.id.edt_verify_code);
          tvError = (TextView)findViewById(R.id.txt_verify_error);

          TextView resubmitCode = (TextView)findViewById(R.id.txt_resubmit);
          resubmitCode.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  resubmitCode();
              }
          });

          progress = new ProgressDialog(this);
          progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    private boolean validateCode() {

        tvError.setVisibility(View.INVISIBLE);
        verifyCode = etVerifyCode.getText().toString();

        if (verifyCode.isEmpty()) {
            etVerifyCode.setError(getString(R.string.error_null_code));
            etVerifyCode.requestFocus();
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

    private void dispError(String error){
        tvError.setText(error);
        tvError.setVisibility(View.VISIBLE);
    }

    private void resubmitCode() {


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
            progress.setMessage(getString(R.string.wait));
            progress.show();
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

                    Gson gson = new GsonBuilder().create();
                    AppGlobal._userInfo = gson.fromJson(result, UserInfo.class);

                    Intent intent = new Intent(VerifyCodeActivity.this, MainActivity.class);
                    startActivity(intent);
                    HomeActivity._instance.finish();
                    VerifyCodeActivity.this.finish();

                } else {
                    String status = jsonObj.getString(Constant.JSON_STATUS_MESSAGE);
                    dispError(status);
                }

            } catch (Exception e) {
                // TODO: handle exception
                //Log.e("oasis", e.toString());
            }
        }
    }

    public class resubmitCodeAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            progress.setMessage(getString(R.string.wait));
            progress.show();
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
            progress.dismiss();

            if (result == null){
                dispError(getString(R.string.error_failed_connect));
                return;
            }

            try {

                JSONObject jsonObj = new JSONObject(result);
                String statusCode = jsonObj.getString(Constant.JSON_STATUS_CODE);

                if (statusCode.equals("00")) {
                    //VerifyCodeActivity.this.finish();
                } else {
                    String status = jsonObj.getString(Constant.JSON_STATUS_MESSAGE);
                    dispError(status);
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
        actionBar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.actionbar_bg));

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

