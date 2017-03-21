package com.mi1.duitku.Tab3;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.mi1.duitku.R;

public class TransferActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etAmount;
    private String amount;
    private String email;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        progress = new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        etEmail = (EditText) findViewById(R.id.edt_receiver_email);
        etAmount = (EditText) findViewById(R.id.edt_amount);

        Button btnPay = (Button) findViewById(R.id.btn_pay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {

                }
            }
        });

    }

    public boolean validate() {

        email = etEmail.getText().toString();
        amount = etAmount.getText().toString();

        if (amount.isEmpty()) {
            etAmount.setError("please fill payment amount");
            etAmount.requestFocus();
            return false;
        }
        else if (Double.parseDouble(amount) < (double)10000) {
            etAmount.setError("Pembayaran minimum RP 10.000,00");
            etAmount.requestFocus();
            return false;
        }
        else if (email.isEmpty()) {
            etEmail.setError("Alamat e-mail penerima");
            etEmail.requestFocus();
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

        if (id == android.R.id.home) {
            TransferActivity.this.finish();

        } else if(id == R.id.action_payment) {
            if (validate()) {

            }
        }

        return super.onOptionsItemSelected(item);
    }
}
