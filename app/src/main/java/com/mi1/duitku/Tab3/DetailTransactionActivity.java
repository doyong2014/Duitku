package com.mi1.duitku.Tab3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.mi1.duitku.Common.CommonFunction;
import com.mi1.duitku.Common.Constant;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab3.Common.CashInfo;
import com.mi1.duitku.Tab3.Common.Tab3Global;

public class DetailTransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaction);

        Intent intent = getIntent();
        int index = intent.getExtras().getInt("index");
        int position = intent.getExtras().getInt("position");

        CashInfo.TransactionList item;
        if(index == 0){
            item = Tab3Global._cashInData.get(position);
        }else  {
            item = Tab3Global._cashOutData.get(position);
        }

        TextView tvAmount = (TextView)findViewById(R.id.txt_amount);
        tvAmount.setText(CommonFunction.formatNumbering(item.amount));

        TextView tvDate = (TextView)findViewById(R.id.txt_date);
        tvDate.setText(CommonFunction.getFormatedDate(item.date));

        TextView tvRefNum = (TextView)findViewById(R.id.txt_reference_num);
        tvRefNum.setText(item.reference);

        String transactionType="";
        switch (Integer.parseInt(item.typeTransaction))
        {
            case Constant.TRANSACTION_TYPE.TOPUP:
                transactionType = "Top Up";
                break;
            case Constant.TRANSACTION_TYPE.GIFT:
                transactionType = "Gift";
                break;
            case Constant.TRANSACTION_TYPE.TRANSACTION_PPOB:
                transactionType = "Pembelian";
                break;
            case Constant.TRANSACTION_TYPE.TRANSACTION_TRANSFER:
                transactionType = "Top Up";
                break;
        }

        TextView tvType = (TextView)findViewById(R.id.txt_type);
        tvType.setText(transactionType);

        TextView tvDesc = (TextView)findViewById(R.id.txt_description);
        tvDesc.setText(item.descript);

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
            DetailTransactionActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}