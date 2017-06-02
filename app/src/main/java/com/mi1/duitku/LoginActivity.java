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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.Common.CommonFunction;
import com.mi1.duitku.Common.Constant;
import com.mi1.duitku.Common.PackageDetailInfo;
import com.mi1.duitku.Common.UserInfo;
import com.mi1.duitku.Main.MainActivity;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.StoringMechanism;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.security.auth.callback.PasswordCallback;

public class LoginActivity extends BaseActivity implements OnClickListener {

    private static String TAG = "LonginActivity";
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
        progress.setMessage(getString(R.string.wait));
        progress.setCanceledOnTouchOutside(false);
    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void login(){

        String[] params = new String[4];
        params[0] = userName;
        params[1] = password;
        //CommonFunction.md5(password);
        params[2] = Constant.LOGIN_TYPE;
        params[3] = Constant.COMMUNITY_CODE;
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
            progress.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... param) {

            String result = null;

            try {

                URL url = new URL(Constant.URLLOGINDIGI1);//(Constant.LOGIN_PAGE); //+ "?loginUrl=" + Constant.URLLOGINDIGI1);


                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", param[0]);
                    jsonObject.put("password", param[1]);
                    jsonObject.put("type", param[2]);
//                    jsonObject.put("community_code", param[3]);

//                    jsonObject.put("username", "8618642502551"); // 081213497969  8618642502551
//                    jsonObject.put("password", CommonFunction.md5("NB1NSFW5"));// TMIA7EPD  NB1NSFW5
//                    jsonObject.put("community_code", param[2]);

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

            if (result == null){
                progress.dismiss();
                dispError(getString(R.string.error_failed_connect));
                return;
            }

            try {

                JSONObject jsonObj = new JSONObject(result);
                JSONArray ja = jsonObj.getJSONArray("obj");
                String statusCode = jsonObj.getString(Constant.JSON_STATUS_CODE);

                if (statusCode.equals("00")){
                    Gson gson = new GsonBuilder().create();
                    AppGlobal._userInfo = gson.fromJson(result, UserInfo.class);
                    List<PackageDetailInfo> packageDetailInfoList = new ArrayList<PackageDetailInfo>();
                    for(int a = 0; a< ja.length(); a++)
                    {
                        String data = ja.getString(a);
                        packageDetailInfoList.add(gson.fromJson(data,PackageDetailInfo.class));
//                        JSONObject jo = new JSONObject(data);
                    }
                    AppGlobal._userInfo.packageDetail = packageDetailInfoList;
                    initQBFramework();
                    loginQB();

                } else if (statusCode.equals("-124")) {
                    progress.dismiss();
                    AppGlobal._userInfo.phoneNumber = jsonObj.getString(Constant.JSON_PHONE_NUM);
                    Intent intent = new Intent(LoginActivity.this, VerifyCodeActivity.class);
                    startActivity(intent);

                } else {
                    progress.dismiss();
                    String status = jsonObj.getString(Constant.JSON_STATUS_MESSAGE);
                    dispError(status);
                }


            } catch (Exception e) {
                // TODO: handle exception
                progress.dismiss();
                Log.e("oasis", e.getMessage());
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

    private void initQBFramework() {

        QBSettings.getInstance().setStoringMehanism(StoringMechanism.UNSECURED);
        QBSettings.getInstance().init(getApplicationContext(), Constant.QB_APP_ID, Constant.QB_AUTH_KEY, Constant.QB_AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(Constant.QB_ACCOUNT_KEY);
        QBSettings.getInstance().setEnablePushNotification(true);
    }

    private void loginQB() {

        QBUser qbUser = new QBUser(AppGlobal._userInfo.phoneNumber, Constant.QB_ACCOUNT_PASS);

        QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                AppGlobal.qbID = qbUser.getId();
                progress.dismiss();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                HomeActivity._instance.finish();
                LoginActivity.this.finish();
            }

            @Override
            public void onError(QBResponseException e) {

                progress.dismiss();
                if (e.getMessage().equals("Unauthorized")) {
                    signUpQB();
                } else {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signUpQB() {

        QBUser qbUser = new QBUser(AppGlobal._userInfo.phoneNumber, Constant.QB_ACCOUNT_PASS);
        qbUser.setFullName(AppGlobal._userInfo.name);
        qbUser.setEmail(AppGlobal._userInfo.email);
        QBUsers.signUp(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                loginQB();
            }

            @Override
            public void onError(QBResponseException e) {
                progress.dismiss();
                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

