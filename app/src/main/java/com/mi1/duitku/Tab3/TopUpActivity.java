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
import android.widget.EditText;
import android.widget.Toast;

import com.android.duitku.inquiry.view.InquiryActivity;
import com.mi1.duitku.Common.UserInfo;
import com.mi1.duitku.R;

public class TopUpActivity extends AppCompatActivity {

    private EditText etAmount;
    private String mAmount;
    private String current = "";
    private final int INQUIRY = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);

        etAmount = (EditText)findViewById(R.id.edt_amount);
//        etAmount.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(!s.toString().equals(current)){
//                    etAmount.removeTextChangedListener(this);
//                    double amount = Double.parseDouble(s.toString());
//                    String formato = NumberFormat.getCurrencyInstance().format((amount/100));
//                    current = formato;
//                    etAmount.setText(current);
//                    etAmount.addTextChangedListener(this);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        Button btnMore = (Button)findViewById(R.id.btn_more);
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateAmount()) {
                    selectPayment();
                }
            }
        });
    }

    private void selectPayment() {

        Intent intent = new Intent(TopUpActivity.this, InquiryActivity.class);
        intent.putExtra("nominal", mAmount);
        intent.putExtra("token", UserInfo.mToken);
        intent.putExtra("order_detail", "Top-up Mi1 Indonesia");
        intent.putExtra("cp", UserInfo.mPhoneNumber);
        startActivityForResult(intent, INQUIRY);
    }

    private boolean validateAmount(){

        mAmount = etAmount.getText().toString();

        if (mAmount.isEmpty()){
            Toast.makeText(TopUpActivity.this, getString(R.string.error_null_amount), Toast.LENGTH_SHORT).show();
            etAmount.requestFocus();
            return false;
        }

        if (Integer.valueOf(mAmount) < 10000){
            Toast.makeText(TopUpActivity.this, getString(R.string.error_invalid_amount), Toast.LENGTH_SHORT).show();
            etAmount.requestFocus();
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

        getMenuInflater().inflate(R.menu.menu_topup, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_topup) {
            if (validateAmount()) {
                selectPayment();
            }
        } else if (id == android.R.id.home) {
            TopUpActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode == INQUIRY)
        {
            Intent broadcast = new Intent();
            //getActivity().sendBroadcast(broadcast);
        }
    }
}
