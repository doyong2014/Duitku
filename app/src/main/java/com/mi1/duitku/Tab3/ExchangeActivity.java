package com.mi1.duitku.Tab3;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.mi1.duitku.Tab3.Adapter.ChoiceAdapter;
import com.mi1.duitku.Tab5.Adapter.PackageAdapter;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ExchangeActivity extends AppCompatActivity
{
    private EditText etAmount;
    private EditText etPIN;
    private EditText etKonfirmasiPIN;
    private Spinner spinnerChoice;

    private String amount;
    private String pin;
    private String konfirmasiPin;
    private String transferType;
    private String transactionType;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.wait));
        progress.setCanceledOnTouchOutside(false);

        Intent intent = getIntent();
        if(intent != null)
        {
            transferType = intent.getStringExtra("product_code");
        }

//        etAmount = (EditText) findViewById(R.id.edt_amount);
        spinnerChoice = (Spinner)findViewById(R.id.spinner_package);
        etPIN = (EditText) findViewById(R.id.edt_pin);
        etKonfirmasiPIN = (EditText) findViewById(R.id.edt_konfirmasi_pin);

        Button btnPay = (Button) findViewById(R.id.btn_pay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    showDialog();
                }
            }
        });

        ChoiceAdapter adapter;
        adapter = new ChoiceAdapter(this, getResources().getStringArray(R.array.choiceexchangedigi1));
        spinnerChoice.setAdapter(adapter);
        spinnerChoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                amount = getResources().getStringArray(R.array.choiceexchangedigi1)[position].replace("%","");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public boolean validate()
    {
        pin = etPIN.getText().toString();
        konfirmasiPin = etKonfirmasiPIN.getText().toString();

        if (amount.isEmpty()) {
            etAmount.setError("please fill conversion amount");
            etAmount.requestFocus();
            return false;
        }
        else if(!konfirmasiPin.equals(pin))
        {
            etKonfirmasiPIN.setError("PIN tidak sama");
            etKonfirmasiPIN.requestFocus();
            return false;
        }

        hideKeyboard();
        return true;
    }
    private void Gift()
    {
        String[] params = new String[5];
        params[0] = AppGlobal._userInfo.packageDetail.get(0).kode_user;
        params[1] = "kode_user";
        params[2] = konfirmasiPin;
        params[3] = amount;
        params[4] = transferType;
        ExchangeActivity.GiftAsync _giftAsync = new ExchangeActivity.GiftAsync();
        _giftAsync.execute(params);
    }
    private void hideKeyboard()
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public class GiftAsync extends AsyncTask<String, Integer, String>
    {

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
                URL url = new URL(Constant.URL_EXCHANGE_DIGI1);

                if(transferType.equals("MP to DiGiMall.com IDR"))
                {
                    transactionType = "mpdgidr";
                }
                else if(transferType.equals("LP to eCash IDR"))
                {
                    transactionType = "lpcashidr";
                }
                else if(transferType.equals("LP to Mobile Apps IDR"))
                {
                    transactionType = "lpapps";
                }

                StringBuilder postData = new StringBuilder();
                postData.append("username=" + param[0] + "&");
                postData.append("type=" + param[1] + "&");
                postData.append("pin=" + param[2] + "&");
                postData.append("choice=" + param[3] + "&");
                postData.append("transaction_type=" + transactionType);
                byte[] postDataBytes = postData.toString().getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000); /* milliseconds */
                conn.setConnectTimeout(15000); /* milliseconds */
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
                Toast.makeText(ExchangeActivity.this, getString(R.string.error_failed_connect), Toast.LENGTH_SHORT).show();
                return;
            } else if(result.equals("401")) {
                Toast.makeText(ExchangeActivity.this, "Sesi anda telah habis", Toast.LENGTH_SHORT).show();
                logout();
                return;
            }

            try {

                JSONObject jsonObj = new JSONObject(result);
                String statusCode = jsonObj.getString("st");

                if (statusCode.equals("true")){
                    String trxId = jsonObj.getString("url");
                    Intent intent = new Intent(ExchangeActivity.this, ProcessDoneActivity.class);
                    intent.putExtra(ProcessDoneActivity.TAG_BILLAMOUNT, amount);
                    intent.putExtra(ProcessDoneActivity.TAG_STATUS_MESSAGE,jsonObj.getString("msg"));
                    intent.putExtra(ProcessDoneActivity.TAG_SUBSCRIBERID, "");
                    intent.putExtra("TrxID", trxId);
                    startActivity(intent);
                    ExchangeActivity.this.finish();
                } else {
                    String status = jsonObj.getString("msg");
                    Toast.makeText(ExchangeActivity.this, status, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                // TODO: handle exception
                //Log.e("oasis", e.toString());
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.actionbar_bg));

        getMenuInflater().inflate(R.menu.menu_topup, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            ExchangeActivity.this.finish();

        } else if(id == R.id.action_payment) {
            if (validate()) {
                showDialog();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout()
    {
        AppGlobal._userInfo = null;
        AppGlobal._userDetailInfo = null;
        Intent intent = new Intent(ExchangeActivity.this, LoginActivity.class);
        startActivity(intent);
        ExchangeActivity.this.finish();
        MainActivity._instance.finish();
    }

    private void showDialog()
    {

        MaterialDialog mDialog = new MaterialDialog.Builder(this)
                .customView(R.layout.dialog_conversion_confirmation, true)
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

        tvPhoneNumber.setText(transferType);
        tvJumlahDana.setText(amount + "%");

        mDialog.show();
    }
}
