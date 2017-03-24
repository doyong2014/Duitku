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
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mi1.duitku.Common.AppGlobal;
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

public class LoginActivity extends AppCompatActivity implements OnClickListener {

    private String userName;
    private String password;

    private EditText etUserName;
    private EditText etPassword;
    private TextView tvError;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView getPassword = (TextView)findViewById(R.id.txt_forget_password);
        getPassword.setOnClickListener(this);

        TextView newAccount = (TextView)findViewById(R.id.txt_no_account);
        newAccount.setOnClickListener(this);

        Button btnLogin = (Button)findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

        tvError = (TextView)findViewById(R.id.txt_login_error);

        etUserName = (EditText)findViewById(R.id.edt_login_email);
        etPassword = (EditText)findViewById(R.id.edt_login_password);

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

    private void login(){

        String[] params = new String[3];
        params[0] = userName;
        params[1] = CommonFunction.md5(password);
        params[2] = Constant.COMMUNITY_CODE;
        LoginAsync _loginAsync = new LoginAsync();
        _loginAsync.execute(params);
    }

    @Override
    public void onClick(View v) {

        Intent intent = null;

        switch(v.getId()){

            case R.id.btn_login:

                if (validateLogin()) {
                    login();
                }
                break;

            case R.id.txt_forget_password:
                etPassword.setText("");
                intent = new Intent(LoginActivity.this, RecoveryPasswordActivity.class);
                startActivity(intent);
                break;

            case R.id.txt_no_account:
                intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                break;
        }
    }

    private boolean validateLogin(){

        tvError.setVisibility(View.INVISIBLE);
        userName = etUserName.getText().toString();
        password = etPassword.getText().toString();

        if (userName.isEmpty()){
            etUserName.setError(getString(R.string.error_null_email));
            etUserName.requestFocus();
            return false;
        }

        if (password.isEmpty()){
            etPassword.setError(getString(R.string.error_null_password));
            etPassword.requestFocus();
            return false;
        }

        hideKeyboard();
        return true;
    }

    private void dispError(String error){
        tvError.setText(error);
        tvError.setVisibility(View.VISIBLE);
    }

    public class LoginAsync extends AsyncTask<String, Integer, String> {

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

                URL url = new URL(Constant.LOGIN_PAGE);

                JSONObject jsonObject = new JSONObject();
                try {
//                    jsonObject.put("username", param[0]);
//                    jsonObject.put("password", param[1]);
//                    jsonObject.put("community_code", param[2]);

                    jsonObject.put("username", "0818718184");
                    jsonObject.put("password", CommonFunction.md5("ZOP8AUK2"));
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
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    HomeActivity._instance.finish();
                    LoginActivity.this.finish();

                } else if (statusCode.equals("-124")) {

                    AppGlobal._userInfo.phoneNumber = jsonObj.getString(Constant.JSON_PHONE_NUM);
                    Intent intent = new Intent(LoginActivity.this, VerifyCodeActivity.class);
                    startActivity(intent);

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

        getMenuInflater().inflate(R.menu.menu_login, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            if (validateLogin()) {
                login();
            }
        } else if (id == android.R.id.home) {
            LoginActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

