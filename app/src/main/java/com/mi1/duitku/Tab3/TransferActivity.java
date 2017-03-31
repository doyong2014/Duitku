package com.mi1.duitku.Tab3;

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
import com.mi1.duitku.Common.Constant;
import com.mi1.duitku.LoginActivity;
import com.mi1.duitku.Main.MainActivity;
import com.mi1.duitku.R;
import com.mi1.duitku.RecoveryPasswordActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TransferActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etAmount;
    private String amount;
    private String email;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        progress = new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        etEmail = (EditText) findViewById(R.id.edt_receiver_email);
        etAmount = (EditText) findViewById(R.id.edt_amount);

        Button btnPay = (Button) findViewById(R.id.btn_pay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    showDialog();
                }
            }
        });

    }

    public boolean validate() {

        email = etEmail.getText().toString();
        amount = etAmount.getText().toString();

        if (amount.isEmpty()) {
            etAmount.setError("please fill payment amount");
            etAmount.requestFocus();
            return false;
        }
        else if (Double.parseDouble(amount) < (double)10000) {
            etAmount.setError("Pembayaran minimum RP 10.000,00");
            etAmount.requestFocus();
            return false;
        }
        else if (email.isEmpty()) {
            etEmail.setError("telepon penerima");
            etEmail.requestFocus();
            return false;
        }

        hideKeyboard();
        return true;
    }

    private void Gift(){

        String[] params = new String[5];
        params[0] = AppGlobal._userInfo.token;
        params[1] = amount;
        params[2] = "";
        params[3] = email;
        params[4] = Constant.COMMUNITY_CODE;
        GiftAsync _giftAsync = new GiftAsync();
        _giftAsync.execute(params);
    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public class GiftAsync extends AsyncTask<String, Integer, String> {

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

                URL url = new URL(Constant.GIFT_PAGE);

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("token", param[0]);
                    jsonObject.put("amount", param[1]);
                    //jsonObject.put("email", param[2]);
                    jsonObject.put("phoneNumber", param[3]);
                    jsonObject.put("community_code", param[4]);

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
                Toast.makeText(TransferActivity.this, getString(R.string.error_failed_connect), Toast.LENGTH_SHORT).show();
                return;
            } else if(result.equals("401")) {
                Toast.makeText(TransferActivity.this, "Sesi anda telah habis", Toast.LENGTH_SHORT).show();
                logout();
                return;
            }

            try {

                JSONObject jsonObj = new JSONObject(result);
                String statusCode = jsonObj.getString(Constant.JSON_STATUS_CODE);

                if (statusCode.equals("00")){
                    Intent intent = new Intent(TransferActivity.this, ProcessDoneActivity.class);
                    intent.putExtra(ProcessDoneActivity.TAG_BILLAMOUNT, amount);
                    intent.putExtra(ProcessDoneActivity.TAG_SUBSCRIBERID, email);
                    startActivity(intent);
                    TransferActivity.this.finish();
                } else {
                    String status = jsonObj.getString(Constant.JSON_STATUS_MESSAGE);
                    Toast.makeText(TransferActivity.this, status, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                // TODO: handle exception
                //Log.e("oasis", e.toString());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.actionbar_bg));

        getMenuInflater().inflate(R.menu.menu_topup, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            TransferActivity.this.finish();

        } else if(id == R.id.action_payment) {
            if (validate()) {
                showDialog();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        AppGlobal._userInfo = null;
        AppGlobal._userDetailInfo = null;
        Intent intent = new Intent(TransferActivity.this, LoginActivity.class);
        startActivity(intent);
        TransferActivity.this.finish();
        MainActivity._instance.finish();
    }

    private void showDialog() {

        MaterialDialog mDialog = new MaterialDialog.Builder(this)
                .customView(R.layout.dialog_transfer_confirmation, true)
                .negativeText("CANCEL")
                .negativeColorRes(R.color.colorDisable)
                .canceledOnTouchOutside(false)
                .neutralText("OK")
                .neutralColorRes(R.color.colorPrimary)
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Gift();
                    }
                }).build();


        TextView tvPhoneNumber = (TextView) mDialog.getCustomView().findViewById(R.id.txt_phoneNumber);
        TextView tvJumlahDana = (TextView) mDialog.getCustomView().findViewById(R.id.txt_amount);

        tvPhoneNumber.setText(email);
        tvJumlahDana.setText(amount);

        mDialog.show();
    }
}
