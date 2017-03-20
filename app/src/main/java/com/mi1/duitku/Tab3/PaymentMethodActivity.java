package com.mi1.duitku.Tab3;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.Common.CommonFunction;
import com.mi1.duitku.Common.Constant;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab3.Common.Inquiry;
import com.mi1.duitku.Tab3.Common.PaymentMethod;
import com.mi1.duitku.Tab3.Common.Tab3Global;

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
import java.util.Collections;


/**
 * Created by latifalbar on 11/16/2015.
 */
public class PaymentMethodActivity  extends AppCompatActivity {

    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView recycler;
    private PGAdapter adapter;
    private int mAdminFee = 0;
    private int mPrice;
    private Inquiry inquiry;
    private ProgressDialog progress;
    private ArrayList<PaymentMethod> mPaymentMethods = new ArrayList<PaymentMethod>();
    private int selBankIndex = -1;

    TextView tvAdminFee;
    TextView tvTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        progress = new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler = (RecyclerView) findViewById(R.id.recycler_bank);
        recycler.setLayoutManager(mLinearLayoutManager);

        GetPGInfo getPGInfo = new GetPGInfo();
        getPGInfo.execute();

        Intent intent = getIntent();
        inquiry = intent.getParcelableExtra("inquiry");

        adapter = new PGAdapter(this, mPaymentMethods);
        recycler.setAdapter(adapter);

        TextView tvOrderDetail = (TextView)findViewById(R.id.txt_order_detail);
        tvOrderDetail.setText(inquiry.getProductDetail());

        TextView tvPrice = (TextView)findViewById(R.id.txt_price);
        tvPrice.setText(CommonFunction.formatNumbering(inquiry.getAmount()));

        mPrice = Integer.valueOf(inquiry.getAmount());

        tvAdminFee = (TextView)findViewById(R.id.txt_admin_fee);
        tvAdminFee.setText("Rp " + mAdminFee);

        tvTotalPrice = (TextView)findViewById(R.id.txt_total_price);
        tvTotalPrice.setText(CommonFunction.formatNumbering(String.valueOf(mAdminFee+mPrice)));

        Button btnPay = (Button)findViewById(R.id.btn_pay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selBankIndex == -1) {
                    showAlert("warning", "Mohon pilih metode pembayaran");
                } else {
                    new RequestInquiry().execute();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.actionbar_bg));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            PaymentMethodActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public class GetPGInfo extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            progress.setMessage("Mohon tunggu..");
            progress.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... param) {

            String result = null;

            try {

                URL url = new URL(Constant.BANK_LIST_PAGE);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(20000); /* milliseconds */
                conn.setConnectTimeout(30000); /* milliseconds */
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

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
                Toast.makeText(PaymentMethodActivity.this, R.string.error_failed_connect, Toast.LENGTH_SHORT).show();
                return;
            }

            try {

                PaymentMethod[] paymentMethods = parsePaymentMethod(result);
                Collections.addAll(mPaymentMethods, paymentMethods);
                adapter.setBank(mPaymentMethods);
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    private PaymentMethod[] parsePaymentMethod(String jsonData) throws JSONException
    {
        JSONArray pgArray = new JSONArray(jsonData);

        PaymentMethod[] paymentMethods = new PaymentMethod[pgArray.length()];
        for(int i = 0;i < pgArray.length();i++)
        {
            JSONObject pg = pgArray.getJSONObject(i);
            PaymentMethod paymentMethod = new PaymentMethod();
            paymentMethod.setName(pg.getString("name"));
            paymentMethod.setCode(pg.getString("pgCode"));
            paymentMethod.setFee(pg.getString("pgFee"));

            paymentMethods[i] = paymentMethod;
        }

        return paymentMethods;
    }

    public class RequestInquiry extends AsyncTask<String, Integer, String> {

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

                URL url = new URL(Constant.TOPUP_INQUIRY_PAGE);

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("amount", inquiry.getAmount());
                    jsonObject.put("pgCode", mPaymentMethods.get(selBankIndex).getCode());
                    jsonObject.put("token", AppGlobal._userInfo.token);
                    jsonObject.put("community_code", Constant.COMMUNITY_CODE);

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
                Toast.makeText(PaymentMethodActivity.this, R.string.error_failed_connect, Toast.LENGTH_SHORT).show();
                return;
            }

            try {

                JSONObject data = new JSONObject(result);

                String pgCode =  mPaymentMethods.get(selBankIndex).getCode();
                if (!pgCode.equals("WW")) {
                    Intent intent = new Intent(PaymentMethodActivity.this, RedirectBankActivity.class);
                    intent.putExtra("info", result);
                    intent.putExtra("amount", inquiry.getAmount());
                    intent.putExtra("reference", getIntent().getStringExtra("reference"));
                    startActivity(intent);
                }

            } catch (Exception e) {
                // TODO: handle exception
                showAlert("alert", "Maaf, Layanan sedang dalam perbaikan");
            }
        }
    }

    public void showAlert(String paramString1, String paramString2)
    {
        new MaterialDialog.Builder(PaymentMethodActivity.this)
                .title(paramString1)
                .content(paramString2)
                .positiveText("OK")
                .positiveColorRes(R.color.colorPrimary)
                .show();

    }

    public class PGAdapter extends RecyclerView.Adapter<PGAdapter.ViewHolder> {

        Context context;
        ArrayList<PaymentMethod> bankInfos;
        int selPos = -1;

        public PGAdapter(Context context, ArrayList<PaymentMethod> bankInfos) {
            this.context = context;
            this.bankInfos = bankInfos;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(context).inflate(R.layout.card_bank, parent, false);
            return new ViewHolder(v);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            PaymentMethod item = bankInfos.get(position);
            String bankName = item.getName().replace("FASPAY ", "").replace("CODAPAY-", "");
            int resID = context.getResources().getIdentifier(item.getCode().toLowerCase() , "drawable", context.getPackageName());
            holder.bankName.setText(bankName);
            holder.markUrl.setBackgroundResource(resID);
            if (selPos == position) {
                holder.cardView.setCardBackgroundColor(0x30000000);
            } else {
                holder.cardView.setCardBackgroundColor(Color.WHITE);
            }

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemChanged(selPos);
                    selPos = position;
                    notifyItemChanged(selPos);
                    PaymentMethod selItem = bankInfos.get(position);
                    tvAdminFee.setText(selItem.getFee());
                    selBankIndex = position;
                }
            });
        }

        @Override
        public int getItemCount() {
            return bankInfos.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView bankName;
            ImageView markUrl;
            CardView cardView;

            public ViewHolder(View v) {
                super(v);
                cardView = (CardView) v.findViewById(R.id.card_bank);
                bankName = (TextView) v.findViewById(R.id.txt_bank_name);
                markUrl = (ImageView) v.findViewById(R.id.img_bank);
            }
        }

        public void setBank(ArrayList<PaymentMethod> bankInfos){
            this.bankInfos = bankInfos;
        }
    }
}
