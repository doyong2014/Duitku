package com.mi1.duitku.Tab3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.mi1.duitku.Common.CommonFunction;
import com.mi1.duitku.Common.Constant;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab3.Common.Inquiry;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class TopUpActivity extends AppCompatActivity {

    private EditText etAmount;
    private String amount;
    private String current = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);

//        Tab3Global._duitkuPreferences = new DuitkuPreferences();
//        Tab3Global._duitkuPreferences.init();

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

//        Intent intent = new Intent(TopUpActivity.this, InquiryActivity.class);
//        intent.putExtra("nominal", amount);
//        intent.putExtra("token", AppGlobal._userInfo.token);
//        intent.putExtra("order_detail", "Top-up Mi1 Indonesia");
//        intent.putExtra("cp", AppGlobal._userInfo.phoneNumber);
//        startActivityForResult(intent, INQUIRY);

        Random random = new Random();
        String orderId = random.nextInt(1000000) + "";

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
        inquiry.setProductDetail("Top-up Mi1 Indonesia");
        inquiry.setAdditionalParam("");
        inquiry.setSign(sign);

//        Tab3Global._duitkuPreferences.inquiry = new Gson().toJson(inquiry);

        Intent intent = new Intent(TopUpActivity.this, PaymentMethodActivity.class);
        intent.putExtra("inquiry", inquiry);
        startActivity(intent);
    }

    private boolean validateAmount(){

        amount = etAmount.getText().toString();

        if (amount.isEmpty()){
            etAmount.setError("Masukkan nominal topup");
            etAmount.requestFocus();
            return false;
        } else if (Integer.valueOf(amount) < 10000){
            etAmount.setError("Jumlah minimal topup Rp 10.000");
            etAmount.requestFocus();
            return false;
        }

        hideKeyboard();
        return true;
    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
}
