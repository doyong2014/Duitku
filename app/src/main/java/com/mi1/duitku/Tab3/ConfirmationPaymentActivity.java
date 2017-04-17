package com.mi1.duitku.Tab3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mi1.duitku.BaseActivity;
import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.Common.CommonFunction;
import com.mi1.duitku.Common.Constant;
import com.mi1.duitku.LoginActivity;
import com.mi1.duitku.Main.MainActivity;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab3.Common.AppsflyerUtil;
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

public class ConfirmationPaymentActivity extends BaseActivity {

    public static final String TAG_ACTIVITYTITLE = "activity_title";
    public static final String TAG_SUBSCRIBER = "subscriberId";
    public static final String TAG_PAYMENT_PRODUCT = "paymentProduct";
    public static final String TAG_PRODUCT_CODE = "productCode";
    public static final String TAG_MONTH_PERIOD = "periodeBulan";
    private String mStrActivityTitle;
    private String mStrSubscriberId;
    private String mStrTotalPayment;
    private ProgressDialog progress;
    private Button btnConfirm;
    private CPaymentInfo mPaymentInfo;
    private String mStrCodeProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_payment);
        // Inflate the layout for this fragment

        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.wait));
        progress.setCanceledOnTouchOutside(false);

        TextView tvSubscriberId = (TextView) findViewById(R.id.txt_subscriber_id);
        TextView tvSubscriberName = (TextView) findViewById(R.id.txt_subscriber_name);
        TextView tvFee = (TextView) findViewById(R.id.txt_fee);
        TextView tvTotalPayment = (TextView) findViewById(R.id.txt_total_payment);
        TextView tvNominal = (TextView) findViewById(R.id.txt_nominal);
        TextView tvReference = (TextView) findViewById(R.id.txt_reference);

        Intent intent = getIntent();

        mStrActivityTitle = intent.getStringExtra(TAG_ACTIVITYTITLE);
        mPaymentInfo = intent.getParcelableExtra(TAG_PAYMENT_PRODUCT);
        mStrCodeProduct = intent.getStringExtra(TAG_PRODUCT_CODE);
        mStrTotalPayment = CommonFunction.formatNumbering(mPaymentInfo.nominal + mPaymentInfo.biayaAdmin);
        mStrSubscriberId = mPaymentInfo.idPelanggan1;

        tvSubscriberId.setText(mStrSubscriberId);
        tvSubscriberName.setText(mPaymentInfo.namaPelanggan);
        tvTotalPayment.setText(mStrTotalPayment);
        tvFee.setText(CommonFunction.formatNumbering(mPaymentInfo.biayaAdmin));
        tvNominal.setText(CommonFunction.formatNumbering(mPaymentInfo.nominal));
        tvReference.setText(mPaymentInfo.ref2);


        btnConfirm = (Button) findViewById(R.id.btn_confirm);
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
        actionBar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.actionbar_bg));

        return true;
    }

    public class callPaymentBill extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progress.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String result = null;

            try {

                URL url = new URL(Constant.PAYMENT_BILL_PAGE);

                JSONObject jsonObject = new JSONObject();
                try {
                    if (mStrCodeProduct.equals("ASRBPJSKS")) {
                        jsonObject.put("idPelanggan", mPaymentInfo.idPelanggan1);
                        jsonObject.put("kodeProduk", mStrCodeProduct);
                        jsonObject.put("noHp", AppGlobal._userInfo.phoneNumber);
                        jsonObject.put("token", AppGlobal._userInfo.token);
                        jsonObject.put("nominal", mPaymentInfo.nominal);
                        jsonObject.put("reference", mPaymentInfo.ref2);
                        jsonObject.put("periodeBulan", getIntent().getStringExtra(TAG_MONTH_PERIOD));
                    }
                    else {
                        jsonObject.put("idPelanggan1", mPaymentInfo.idPelanggan1);
                        jsonObject.put("idPelanggan2", mPaymentInfo.idPelanggan2);
                        jsonObject.put("idPelanggan3", mPaymentInfo.idPelanggan3);
                        jsonObject.put("kodeProduk", mStrCodeProduct);
                        jsonObject.put("token", AppGlobal._userInfo.token);
                        jsonObject.put("nominal", mPaymentInfo.nominal);
                        jsonObject.put("reference", mPaymentInfo.ref2);
                    }
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
                showAlert("Proses gagal", "inquiry failed, please try again later.");
                return;
            } else if(result.equals("401")) {
                Toast.makeText(ConfirmationPaymentActivity.this, "Sesi anda telah habis", Toast.LENGTH_SHORT).show();
                logout();
                return;
            }

            String status = "";
            try {
                JSONObject obj = new JSONObject(result);
                status = obj.getString("statusMessage");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(ConfirmationPaymentActivity.this, ProcessDoneActivity.class);
            intent.putExtra(ProcessDoneActivity.TAG_BILLAMOUNT, mStrTotalPayment.replace("Rp ",""));
            if (mStrCodeProduct.equals("TELEPON"))
                intent.putExtra(ProcessDoneActivity.TAG_SUBSCRIBERID, mStrSubscriberId + "-" + mPaymentInfo.idPelanggan2);
            else
                intent.putExtra(ProcessDoneActivity.TAG_SUBSCRIBERID, mStrSubscriberId);
            intent.putExtra(ProcessDoneActivity.TAG_STATUS_MESSAGE, status);

            AppsflyerUtil appFlyerUtil = new AppsflyerUtil(getApplicationContext());
            Double totalPayment = mPaymentInfo.nominal + mPaymentInfo.biayaAdmin;
            appFlyerUtil.Purchase(totalPayment.floatValue(), "Pembayaran - PPOB", mStrCodeProduct, 1);

            startActivity(intent);
            ConfirmationPaymentActivity.this.finish();
        }
    }

    private void showAlert(String paramString1, String paramString2)
    {
        new MaterialDialog.Builder(ConfirmationPaymentActivity.this)
                .title(paramString1)
                .content(paramString2)
                .positiveText("OK")
                .positiveColorRes(R.color.colorPrimary)
                .show();

    }

    private void logout() {
        AppGlobal._userInfo = null;
        AppGlobal._userDetailInfo = null;
        Intent intent = new Intent(ConfirmationPaymentActivity.this, LoginActivity.class);
        startActivity(intent);
        ConfirmationPaymentActivity.this.finish();
        MainActivity._instance.finish();
    }
}
