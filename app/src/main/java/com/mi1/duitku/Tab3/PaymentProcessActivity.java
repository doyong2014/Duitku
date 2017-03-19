package com.mi1.duitku.Tab3;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;
import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.Common.Constant;
import com.mi1.duitku.LoginActivity;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab3.Common.CPPOBProduct;
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

/**
 * Created by WORYA on 3/16/2016.
 */
public class PaymentProcessActivity extends AppCompatActivity {

    public static final String TAG_ACTIVITYTITLE = "activity_title";
    public static final String TAG_ACTIVITYPRODUCTCODE = "product_code";
    public static final String TAG_ACTIVITYPRODUCTNAME = "product_name";

    private ProgressDialog progress;

    private String subscriberId;
    private String activityTitle;
    private String productCode;
    private String productName;

    private EditText etSubscriberId;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_process);

        Intent intent = getIntent();
        if(intent != null)
        {
            activityTitle = intent.getStringExtra(TAG_ACTIVITYTITLE);
            productName = intent.getStringExtra(TAG_ACTIVITYPRODUCTNAME);
            productCode = intent.getStringExtra(TAG_ACTIVITYPRODUCTCODE);
        }

        progress = new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        etSubscriberId = (EditText) findViewById(R.id.edt_subscriber_id);
        Button btnPayBills = (Button) findViewById(R.id.btn_pay);
        btnPayBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    new callInquiryBill().execute();
                }
            }
        });
    }
    
    private boolean validate() {

        subscriberId = etSubscriberId.getText().toString();

        if(subscriberId.isEmpty()) {
            etSubscriberId.setError("Nomor Pelanggan tidak boleh kosong");
            etSubscriberId.requestFocus();
            return false;
        }

        hideKeyboard();
        return true;
    }

    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(activityTitle);
        actionBar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.actionbar_bg));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            PaymentProcessActivity.this.finish();
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

                URL url = new URL(Constant.INQUIRY_BILL_PAGE);

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("idpelanggan1", subscriberId);
                    jsonObject.put("idpelanggan2", subscriberId);
                    jsonObject.put("idpelanggan3", AppGlobal._userInfo.phoneNumber);
                    jsonObject.put("kodeProduk", productCode);
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
                    return "401";
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
                showAlert("Proses gagal", "inquiry failed, please try again later.");
                return;
            } else if(result == "401") {
                AppGlobal._userInfo.clear();
                Toast.makeText(PaymentProcessActivity.this, "Sesi anda telah habis", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PaymentProcessActivity.this, LoginActivity.class);
                startActivity(intent);
                PaymentProcessActivity.this.finish();
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
                return;
            }

            Intent intent = new Intent(PaymentProcessActivity.this, ConfirmationPaymentActivity.class);
            intent.putExtra(ConfirmationPaymentActivity.TAG_SUBSCRIBER, subscriberId);
            intent.putExtra(ConfirmationPaymentActivity.TAG_PAYMENT_PRODUCT, paymentInfo );
            intent.putExtra(ConfirmationPaymentActivity.TAG_ACTIVITYTITLE, productName);
            intent.putExtra(ConfirmationPaymentActivity.TAG_PRODUCT_CODE, productCode);

            Map<String, Object> eventValue = new HashMap<String, Object>();
            eventValue.put(AFInAppEventParameterName.PRICE,paymentInfo.nominal);
            eventValue.put(AFInAppEventParameterName.DESCRIPTION,paymentInfo);
            AppsFlyerLib.getInstance().trackEvent(PaymentProcessActivity.this, AFInAppEventType.SPENT_CREDIT,eventValue);

            startActivity(intent);
            PaymentProcessActivity.this.finish();
        }
    }

    public void showAlert(String paramString1, String paramString2)
    {
        new MaterialDialog.Builder(PaymentProcessActivity.this)
                .title(paramString1)
                .content(paramString2)
                .positiveText("OK")
                .positiveColorRes(R.color.colorPrimary)
                .show();

    }
}
