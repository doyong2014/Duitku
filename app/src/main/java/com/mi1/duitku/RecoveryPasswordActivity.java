package com.mi1.duitku;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mi1.duitku.Common.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RecoveryPasswordActivity extends BaseActivity {

    private String phoneNumber;
    private String newPassword;
    private String konfirmPassword;
    private String PIN;
    private String konfirmPIN;

    private EditText etPhoneNumber;
    private EditText etNewPassword;
    private EditText etKonfirmPassword;
    private EditText etPIN;
    private EditText etKonfirmPIN;
    private TextView tvError;
    private ProgressDialog progress;

      @Override
    protected void onCreate(Bundle savedInstanceState) {

          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_recovery);

          Button btnRecoveryPwd = (Button)findViewById(R.id.btn_submit);
          btnRecoveryPwd.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  if(validatePhone()) {
                      recoveryPassword();
                  }
              }
          });

          etPhoneNumber = (EditText)findViewById(R.id.edt_recovery_phone);
          etNewPassword = (EditText)findViewById(R.id.edt_recovery_new_password);
          etKonfirmPassword = (EditText)findViewById(R.id.edt_recovery_confirm_new_password);
          etPIN = (EditText)findViewById(R.id.edt_recovery_pin);
          etKonfirmPIN = (EditText)findViewById(R.id.edt_recovery_confirm_pin);
          tvError = (TextView)findViewById(R.id.txt_recovery_error);

          progress = new ProgressDialog(this);
          progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

    }

    private boolean validatePhone() {

        tvError.setVisibility(View.INVISIBLE);
        phoneNumber = etPhoneNumber.getText().toString();
        newPassword = etNewPassword.getText().toString();
        konfirmPassword = etKonfirmPassword.getText().toString();
        PIN = etPIN.getText().toString();
        konfirmPIN = etKonfirmPIN.getText().toString();

        if (phoneNumber.isEmpty()) {
            etPhoneNumber.setError(getString(R.string.error_null_phone));
            etPhoneNumber.requestFocus();
            return false;

        }
//        else if(phoneNumber.length() < 7) {
//            etPhoneNumber.setError(getString(R.string.error_incorrect_phone));
//            etPhoneNumber.requestFocus();
//            return false;
//        }
        if(newPassword.isEmpty())
        {
            etNewPassword.setError("Password harus diisi.");
            etNewPassword.requestFocus();
            return false;
        }
        if(!konfirmPassword.equals(newPassword))
        {
            etKonfirmPassword.setError("Password tidak sama.");
            etKonfirmPassword.requestFocus();
            return false;
        }
        if(PIN.isEmpty())
        {
            etPIN.setError("PIN harus diisi.");
            etPIN.requestFocus();
            return false;
        }
        if(!konfirmPIN.equals(PIN))
        {
            etKonfirmPIN.setError("PIN tidak sama.");
            etKonfirmPIN.requestFocus();
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

    private void recoveryPassword(){

        String[] params = new String[6];
        params[0] = newPassword;
        params[1] = konfirmPassword;
        params[2] = PIN;
        params[3] = konfirmPIN;
        params[4] = phoneNumber;
        params[5] = "telpnum";
//        params[1] = Constant.COMMUNITY_CODE;
        RecoveryPasswordAsync _recoveryPwdAsync = new RecoveryPasswordAsync();
        _recoveryPwdAsync.execute(params);
    }

    public class RecoveryPasswordAsync extends AsyncTask<String, Integer, String> {

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

                URL url = new URL(Constant.FORGOT_PASSWORD_NEW_PASSWORD_DIGI1);//URL(Constant.RECOVERY_PASSWORD_PAGE);

//                JSONObject jsonObject = new JSONObject();
//                try {
//                    jsonObject.put("phoneNumber", param[0]);
//                    jsonObject.put("community_code", param[1]);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                StringBuilder postData = new StringBuilder();
                postData.append("password=" + param[0] + "&");
                postData.append("confirm=" + param[1] + "&");
                postData.append("pin=" + param[2] + "&");
                postData.append("confirm_pin=" + param[3] + "&");
                postData.append("username=" + param[4] + "&");
                postData.append("type=" + param[5]);
                byte[] postDataBytes = postData.toString().getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000); /* milliseconds */
                conn.setConnectTimeout(15000); /* milliseconds */
                conn.setUseCaches(false);
//                conn.setRequestProperty("content-type", "application/json");
//                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                wr.write(postData.toString());
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
                String statusCode = jsonObj.getString("st");

                if (statusCode.equals("true")){
                    showDialog();
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

    private void showDialog() {

        MaterialDialog mDialog = new MaterialDialog.Builder(this)
            .content(getString(R.string.alert_recovery))
            .positiveText("OK")
            .cancelable(false)
            .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    RecoveryPasswordActivity.this.finish();
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
            if(validatePhone()) {
                recoveryPassword();
            }
        } else if (id == android.R.id.home) {
            RecoveryPasswordActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

