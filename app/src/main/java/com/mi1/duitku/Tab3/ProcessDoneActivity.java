package com.mi1.duitku.Tab3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mi1.duitku.R;

/**
 * Created by WORYA on 3/16/2016.
 */
public class ProcessDoneActivity extends AppCompatActivity {

    public static final String TAG_BILLAMOUNT = "bill_amount";
    public static final String TAG_SUBSCRIBERID = "subscriber_id";
    public static final String TAG_STATUS_MESSAGE = "status_message";

    private String mStrBillAmount;
    private String mStrSubscriberId;
    private String mStrStatusMsg;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_layout);

        Intent intent = getIntent();
        if(intent != null)
        {
            mStrBillAmount = intent.getStringExtra(TAG_BILLAMOUNT);
            mStrSubscriberId = intent.getStringExtra(TAG_SUBSCRIBERID);
            mStrStatusMsg = intent.getStringExtra(TAG_STATUS_MESSAGE);
        }

        TextView tvBillAmmount = (TextView) findViewById(R.id.txt_amount);
        tvBillAmmount.setText(mStrBillAmount);

        TextView tvSubscriberId = (TextView) findViewById(R.id.txt_subscriber_id);
        tvSubscriberId.setText(mStrSubscriberId);

        TextView tvStatus = (TextView) findViewById(R.id.txt_sent_to);
//        tvStatus.setText(mStrStatusMsg);

        Button btnFinish = (Button) findViewById(R.id.btn_finish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProcessDoneActivity.this.finish();
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

}
