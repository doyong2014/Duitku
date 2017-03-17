package com.mi1.duitku.Tab3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;
import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.Common.Constant;
import com.mi1.duitku.Common.UserInfo;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab3.Common.CPaymentInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PLNPrepaidActivity extends AppCompatActivity {

    private EditText etAmount;
    private EditText etCustomer;
    private String amount;
    private String customer;
    private ProgressDialog progress;
    private final static String PRODUCT_CODE = "PLNPRAH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pln_prepaid);

        progress = new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        etAmount = (EditText) findViewById(R.id.edt_amount);
        etCustomer = (EditText) findViewById(R.id.edt_customer);

        Button btnPay = (Button) findViewById(R.id.btn_pay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    new callInquiryBill().execute();
                }
            }
        });

    }

    public boolean validate() {

        amount = etAmount.getText().toString();
        customer = etCustomer.getText().toString();

        if (amount.isEmpty()) {
            etAmount.setError("please fill payment amount");
            return false;
        }
        else if (Double.parseDouble(amount) < (double)10000) {
            etAmount.setError("Pembayaran minimum RP 10.000,00");
            return false;
        }
        else if (customer.isEmpty()) {
            etCustomer.setError("Mohon masukkan nomor pelanggan");
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.actionbar_bg));

        getMenuInflater().inflate(R.menu.menu_pay, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            PLNPrepaidActivity.this.finish();

        } else if(id == R.id.action_payment) {
            if (validate()) {
                new callInquiryBill().execute();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public class callInquiryBill extends AsyncTask<String, Void, String> {

        CPaymentInfo paymentInfo = new CPaymentInfo();

        @Override
        protected void onPreExecute() {
            progress.setMessage(getString(R.string.wait));
            progress.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String result = null;

            try {

                URL url = new URL(Constant.LOGIN_PAGE);

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("idpelanggan1", customer);
                    jsonObject.put("idpelanggan2", customer);
                    jsonObject.put("idpelanggan3", AppGlobal._userInfo.phoneNumber);
                    jsonObject.put("kodeProduk", PRODUCT_CODE);
                    jsonObject.put("token", AppGlobal._userInfo.token);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(50000); /* milliseconds */
                conn.setConnectTimeout(50000); /* milliseconds */
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
                } else if (conn.getResponseCode() == 401) {
                    //session expired
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
                Toast.makeText(PLNPrepaidActivity.this, getString(R.string.error_failed_connect), Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONObject obj = new JSONObject(result);
                paymentInfo.idPelanggan1 = obj.getString("idPelanggan1");
                paymentInfo.idPelanggan2 = obj.getString("idPelanggan2");
                paymentInfo.idPelanggan3 = obj.getString("idPelanggan3");
                if (obj.getString("biayaAdmin") != "null")
                    paymentInfo.biayaAdmin = Double.parseDouble(obj.getString("biayaAdmin"));
                if (obj.getString("nominal") != "null")
                    paymentInfo.nominal = Double.parseDouble(obj.getString("nominal"));
                paymentInfo.namaPelanggan = obj.getString("namaPelanggan");
                paymentInfo.namaOperator = obj.getString("namaOperator");
                paymentInfo.dayaPln = obj.getString("dayaPln");
                paymentInfo.ref1 = obj.getString("ref1");
                paymentInfo.ref2 = obj.getString("ref2");

            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }

//            if (returnStatus.getHttpCode() == 200 && returnStatus.getReturnCode().equals("00")) {
//                Intent intent = new Intent(PurchaseProcessPLNActivity.this, ConfirmationPaymentActivity.class);
//                intent.putExtra(ConfirmationPaymentActivity.TAG_SUBSCRIBER, edit_subscriberid.getText());
//                paymentInfo.nominal = Double.parseDouble(edit_paymentAmount.getText().toString());
//                intent.putExtra(ConfirmationPaymentActivity.TAG_PAYMENT_PRODUCT, paymentInfo );
//                intent.putExtra(ConfirmationPaymentActivity.TAG_ACTIVITYTITLE, mStrActivityTitle);
//                intent.putExtra(ConfirmationPaymentActivity.TAG_PRODUCT_CODE, ppobProduct.productCode);

                Map<String, Object> eventValue = new HashMap<String, Object>();
                eventValue.put(AFInAppEventParameterName.PRICE,paymentInfo.nominal);
                eventValue.put(AFInAppEventParameterName.DESCRIPTION,paymentInfo);
                AppsFlyerLib.getInstance().trackEvent(PLNPrepaidActivity.this, AFInAppEventType.SPENT_CREDIT,eventValue);

//                startActivity(intent);
                //PurchaseProcessPLNActivity.this.finish();
//            }
//            else if(returnStatus.getReturnCode().equals("02"))
//            {
//                Intent intent = new Intent(PurchaseProcessPLNActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
//                Toast.makeText(PurchaseProcessPLNActivity.this, "Sesi anda telah habis", Toast.LENGTH_LONG).show();
//            }
//            else {
//                showAlert("Proses gagal", returnStatus.getMessage());
//            }
        }

    }

    public void showAlert(String paramString1, String paramString2)
    {
//        final CustomAlertDialog customAlertDialog = new CustomAlertDialog();
//        customAlertDialog.setDialog(PLNPrepaidActivity.this, paramString1, paramString2);
//        customAlertDialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                customAlertDialog.dialog.dismiss();
//            }
//        });
//        customAlertDialog.showDialog();
    }

}
