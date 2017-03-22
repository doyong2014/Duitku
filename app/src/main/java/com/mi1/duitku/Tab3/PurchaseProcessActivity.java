package com.mi1.duitku.Tab3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.Common.CommonFunction;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab3.Common.CPPOBProduct;
import com.mi1.duitku.Tab3.Common.CPPOBProductParent;

/**
 * Created by WORYA on 3/16/2016.
 */
public class PurchaseProcessActivity extends AppCompatActivity {

    public static final String TAG_ACTIVITYTITLE = "activity_title";
    public static final String TAG_ACTIVITYPRODUCT = "activity_product";

    private String activityTitle;
    private String sellPrice;

    private Spinner spinDenom;
    private EditText etSellPrice;
    private EditText etPhoneNumer;
    private CPPOBProduct ppobProduct;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_process);

        Intent intent = getIntent();
        activityTitle = intent.getStringExtra(TAG_ACTIVITYTITLE);

        CPPOBProductParent product = getIntent().getParcelableExtra(this.TAG_ACTIVITYPRODUCT);

        spinDenom = (Spinner) findViewById(R.id.spinner_denom);
        etSellPrice = (EditText) findViewById(R.id.edt_sell_price);
        etSellPrice.setFocusable(false);

        etPhoneNumer = (EditText) findViewById(R.id.edt_phone_num);

        Button btnProcess = (Button) findViewById(R.id.btn_process);
        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PurchaseProcessActivity.this, ConfirmationActivity.class);
                intent.putExtra(ConfirmationActivity.TAG_SUBSCRIBER, etPhoneNumer.getText().toString());
                intent.putExtra(ConfirmationActivity.TAG_TOTALPAYMENT, sellPrice);
                intent.putExtra(ConfirmationActivity.TAG_PRODUCT, ppobProduct);
                intent.putExtra(ConfirmationActivity.TAG_ACTIVITYTITLE, ppobProduct.productName);
                intent.putExtra(ConfirmationActivity.TAG_PRODUCT_PRICE, ppobProduct.productPrice);
                startActivity(intent);
            }
        });

        ArrayAdapter<CPPOBProduct> denom_adapter = new ArrayAdapter<CPPOBProduct>(PurchaseProcessActivity.this, R.layout.spinner_item, R.id.txt_spinneritem, product.productList);
        spinDenom.setAdapter(denom_adapter);
        spinDenom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ppobProduct = (CPPOBProduct)spinDenom.getSelectedItem();
                    sellPrice = CommonFunction.formatNumbering(ppobProduct.productPrice);
                    etSellPrice.setText(sellPrice);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinDenom.setSelection(0, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(activityTitle);
        actionBar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.actionbar_bg));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            PurchaseProcessActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
