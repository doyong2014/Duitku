package com.android.duitku.transactionstatus.view;

import android.os.Bundle;

import com.android.duitku.R;
import com.android.duitku.base.BaseActivity;

public class TransactionStatusActivity extends BaseActivity implements TransactionStatusView {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_status);

    }


}
