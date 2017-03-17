package com.mi1.duitku.Tab3;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mi1.duitku.Common.Constant;
import com.mi1.duitku.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class SelectPaymentActivity extends AppCompatActivity {

    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView recycler;
    private PGAdapter adapter;
    private int mAdminFee = 0;
    private int mPrice;
    private ArrayList<PaymentMethod> mPaymentMethods = new ArrayList<PaymentMethod>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payment);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler = (RecyclerView) findViewById(R.id.recycler_bank);
        recycler.setLayoutManager(mLinearLayoutManager);

        Intent intent = getIntent();
        Inquiry inquiry = intent.getParcelableExtra("inquiry");

        GetPGInfo getPGInfo = new GetPGInfo();
        getPGInfo.execute();

        adapter = new PGAdapter(this, mPaymentMethods);
        recycler.setAdapter(adapter);

        TextView orderDetail = (TextView)findViewById(R.id.txt_order_detail);
        orderDetail.setText(inquiry.getProductDetail());

        TextView price = (TextView)findViewById(R.id.txt_price);
        price.setText("Rp " + inquiry.getAmount());

        mPrice = Integer.valueOf(inquiry.getAmount());

        TextView adminFee = (TextView)findViewById(R.id.txt_price);
//        adminFee.setText("Rp " + inquiry.getAmount());

        TextView totalPrice = (TextView)findViewById(R.id.txt_total_price);
        totalPrice.setText("Rp " + String.valueOf(mAdminFee+mPrice));
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

        if (id == R.id.action_payment) {

        } else if (id == android.R.id.home) {
            SelectPaymentActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public class GetPGInfo extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
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
//                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestMethod("GET");
                conn.setDoOutput(true);
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

            if (result == null){
                Toast.makeText(SelectPaymentActivity.this, R.string.error_failed_connect, Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Gson gson = new GsonBuilder().create();
                PaymentMethod newsData = gson.fromJson(result, PaymentMethod.class);

                Collections.addAll(mPaymentMethods, newsData);

//                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                // TODO: handle exception
                //Log.e("oasis", e.toString());
            }
        }
    }
}
