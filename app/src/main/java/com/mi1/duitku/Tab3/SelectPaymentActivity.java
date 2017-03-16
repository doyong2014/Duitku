package com.mi1.duitku.Tab3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.mi1.duitku.Common.CommonFunction;
import com.mi1.duitku.Common.Constant;
import com.mi1.duitku.R;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

public class SelectPaymentActivity extends AppCompatActivity {

    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView recycler;
    private BankAdapter adapter;
    private ArrayList<BankInfo> bankInfos = new ArrayList<>();
    private String orderId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payment);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler = (RecyclerView) findViewById(R.id.recycler_bank);
        recycler.setLayoutManager(mLinearLayoutManager);

        Intent intent = getIntent();
        String amount = intent.getExtras().getString("amount");

        init();
        adapter = new BankAdapter(this, bankInfos);
        recycler.setAdapter(adapter);

        Random random = new Random();
        orderId = random.nextInt(1000000) + "";

        String sign = null;
        try {
            sign = CommonFunction.getSHA1(Constant.STATUS_INQUIRY + Constant.API_KEY + orderId + Constant.MERCHANT_CODE);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Inquiry inquiry = new Inquiry();

        inquiry.setAction(Constant.STATUS_INQUIRY);
        inquiry.setMerchantCode(Constant.MERCHANT_CODE);
        inquiry.setOrderId(orderId);
        inquiry.setAmount(amount);
        inquiry.setProductDetail(Constant.ORDER_DETAIL);
        inquiry.setAdditionalParam("");
        inquiry.setSign(sign);

    }

    private void init() {
        bankInfos.add(new BankInfo("BCA\nKlikPay", R.drawable.bcaklikpay));
        bankInfos.add(new BankInfo("ATM\nBersama", R.drawable.bcaklikpay));
        bankInfos.add(new BankInfo("CIMB\nKlik", R.drawable.bcaklikpay));
        bankInfos.add(new BankInfo("BCA\nKlikPay", R.drawable.bcaklikpay));
        bankInfos.add(new BankInfo("ATM\nBersama", R.drawable.bcaklikpay));
        bankInfos.add(new BankInfo("CIMB\nKlik", R.drawable.bcaklikpay));
        bankInfos.add(new BankInfo("BCA\nKlikPay", R.drawable.bcaklikpay));
        bankInfos.add(new BankInfo("ATM\nBersama", R.drawable.bcaklikpay));
        bankInfos.add(new BankInfo("CIMB\nKlik", R.drawable.bcaklikpay));
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
}
