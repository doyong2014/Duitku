package com.mi1.duitku.Tab5;

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
import com.mi1.duitku.LoginActivity;
import com.mi1.duitku.Main.MainActivity;
import com.mi1.duitku.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ChangePasswordActivity extends AppCompatActivity {

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

    private EditText etOldPassword;
    private EditText etNewPassword;
    private EditText etConfirmPassword;
    private TextView tvError;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Button btnSave = (Button)findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    changePassword();
                }
            }
        });

        tvError = (TextView)findViewById(R.id.txt_login_error);

        etOldPassword = (EditText)findViewById(R.id.edt_old_pwd);
        etNewPassword = (EditText)findViewById(R.id.edt_new_pwd);
        etConfirmPassword = (EditText)findViewById(R.id.edt_confirm_pwd);

        progress = new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void changePassword(){

        String[] params = new String[6];
        params[0] = AppGlobal._userInfo.token;
        params[1] = CommonFunction.md5(oldPassword);
        params[2] = CommonFunction.md5(newPassword);
        params[3] = CommonFunction.md5(confirmPassword);
        params[4] = AppGlobal._userInfo.phoneNumber;
        params[5] = Constant.COMMUNITY_CODE;
        ChangePasswordAsync _changePasswordAsync = new ChangePasswordAsync();
        _changePasswordAsync.execute(params);
    }

    private boolean validate(){

        tvError.setVisibility(View.INVISIBLE);
        oldPassword = etOldPassword.getText().toString();
        newPassword = etNewPassword.getText().toString();
        confirmPassword = etConfirmPassword.getText().toString();

        if (oldPassword.isEmpty()){
            etOldPassword.setError(getString(R.string.error_null_password));
            etOldPassword.requestFocus();
            return false;
        }

        if (newPassword.isEmpty()){
            etNewPassword.setError(getString(R.string.error_null_password));
            etNewPassword.requestFocus();
            return false;
        }


        if (confirmPassword.isEmpty()){
            etConfirmPassword.setError(getString(R.string.error_null_password));
            etConfirmPassword.requestFocus();
            return false;
        }

        if (!newPassword.equals(confirmPassword)){
            dispError(getString(R.string.error_dismatch_password));
            return false;
        }

        hideKeyboard();
        return true;
    }

    private void dispError(String error){
        tvError.setText(error);
        tvError.setVisibility(View.VISIBLE);
    }

    public class ChangePasswordAsync extends AsyncTask<String, Integer, String> {

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

                URL url = new URL(Constant.CHANGE_PASSWORD_PAGE);

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("token", param[0]);
                    jsonObject.put("oldPassword", param[1]);
                    jsonObject.put("newPassword", param[2]);
                    jsonObject.put("confirmPassword", param[3]);
                    jsonObject.put("phoneNumber", param[4]);
                    jsonObject.put("community_code", param[5]);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000); /* milliseconds */
                conn.setConnectTimeout(15000); /* milliseconds */
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
            } else if(result.equals("401")) {
                Toast.makeText(ChangePasswordActivity.this, "Sesi anda telah habis", Toast.LENGTH_SHORT).show();
                logout();
                return;
            }

            try {

                JSONObject jsonObj = new JSONObject(result);
                String statusCode = jsonObj.getString(Constant.JSON_STATUS_CODE);

                if (statusCode.equals("00")){
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
                .content(getString(R.string.alert_change_password))
                .positiveText("OK")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        ChangePasswordActivity.this.finish();
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

        getMenuInflater().inflate(R.menu.menu_change_password, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            ChangePasswordActivity.this.finish();
        } else if (id == R.id.action_settings) {
            if (validate()){
                changePassword();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        AppGlobal._userInfo = null;
        AppGlobal._userDetailInfo = null;
        Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        ChangePasswordActivity.this.finish();
        MainActivity._instance.finish();
    }
}