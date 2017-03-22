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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.Common.Constant;
import com.mi1.duitku.LoginActivity;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab3.Common.AppsflyerUtil;
import com.mi1.duitku.Tab3.Common.CPPOBProduct;

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

public class ConfirmationActivity extends AppCompatActivity {

    public static final String TAG_ACTIVITYTITLE = "activity_title";
    public static final String TAG_SUBSCRIBER = "subscriberId";
    public static final String TAG_PRODUCT = "ppobProduct";
    public static final String TAG_TOTALPAYMENT = "totalPayment";
    public static final String TAG_PRODUCT_PRICE = "productPrice";

    private String activityTitle;
    private String subscriberId;
    private String productCode;
    private String productName;
    private String totalPayment;
    private float mProductPrice;
    private ProgressDialog progress;
    private CPPOBProduct mCCPOBProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        // Inflate the layout for this fragment

        Intent intent = getIntent();

        activityTitle = intent.getStringExtra(TAG_ACTIVITYTITLE);
        mCCPOBProduct = intent.getParcelableExtra(TAG_PRODUCT);
        totalPayment = intent.getStringExtra(TAG_TOTALPAYMENT);
        subscriberId = intent.getStringExtra(TAG_SUBSCRIBER);
        mProductPrice = intent.getFloatExtra(TAG_PRODUCT_PRICE, 0f);
        productCode = mCCPOBProduct.productCode;
        productName = mCCPOBProduct.productName;

        progress = new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        TextView tvSubscriberId = (TextView) findViewById(R.id.txt_subscriber_id);
        tvSubscriberId.setText(subscriberId);

        TextView tvProduct = (TextView) findViewById(R.id.txt_product);
        tvProduct.setText(productName);

        TextView tvTotalPayment = (TextView) findViewById(R.id.txt_total_payment);
        tvTotalPayment.setText(totalPayment);

        Button btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new callPaymentBill().execute();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

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
            ConfirmationActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public class callPaymentBill extends AsyncTask<String, Void, String> {

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

                URL url = new URL(Constant.PURCHASE_BILL_PAGE);

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("phoneNumber", subscriberId);
                    jsonObject.put("token", AppGlobal._userInfo.token);
                    jsonObject.put("kodeProduk", productCode);

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
                showAlert("Proses gagal", "Transaction processing failed, please try again later.");
                return;
            } else if(result == "401") {
                AppGlobal._userInfo.clear();
                Toast.makeText(ConfirmationActivity.this, "Sesi anda telah habis", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ConfirmationActivity.this, LoginActivity.class);
                startActivity(intent);
                ConfirmationActivity.this.finish();
                return;
            }

            Intent intent = new Intent(ConfirmationActivity.this, ProcessDoneActivity.class);
            intent.putExtra(ProcessDoneActivity.TAG_BILLAMOUNT, totalPayment);
            intent.putExtra(ProcessDoneActivity.TAG_SUBSCRIBERID, subscriberId);

            Map<String, Object> eventValue = new HashMap<String, Object>();

            AppsflyerUtil appFlyerUtil = new AppsflyerUtil(ConfirmationActivity.this);
            appFlyerUtil.Purchase(mProductPrice, "Pembelian - PPOB", productName, 1);
            startActivity(intent);
            ConfirmationActivity.this.finish();
        }
    }

    public void showAlert(String paramString1, String paramString2)
    {
        new MaterialDialog.Builder(ConfirmationActivity.this)
                .title(paramString1)
                .content(paramString2)
                .positiveText("OK")
                .positiveColorRes(R.color.colorPrimary)
                .show();

    }
}
