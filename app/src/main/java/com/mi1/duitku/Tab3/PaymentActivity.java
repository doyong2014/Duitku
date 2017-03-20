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
import android.widget.ListView;

import com.mi1.duitku.R;
import com.mi1.duitku.Tab3.Common.CPPOBProduct;
import com.mi1.duitku.Tab3.Common.CPPOBProductParent;
import com.mi1.duitku.Tab3.Common.Tab3Global;

/**
 * Created by WORYA on 3/16/2016.
 */
public class PaymentActivity extends AppCompatActivity {

    private CPPOBProductParent product = new CPPOBProductParent();
    private ListView listPayment;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppoblist);

        for (int i = 0; i < Tab3Global._productPayment.child.size(); i++) {
            CPPOBProductParent tempProduct = Tab3Global._productPayment.child.get(i);
            if (tempProduct.name.equals("HP PASCABAYAR")) {
                product = tempProduct;
            }
        }

        if (product.productList.size() > 0) {
            final ArrayAdapter adapter = new ArrayAdapter<CPPOBProduct>(getApplicationContext(), R.layout.list_ppob, R.id.txt_ppobproduct, product.productList);
            listPayment = (ListView) findViewById(R.id.listPPOBProduct);
            listPayment.setAdapter(adapter);
            listPayment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String strTitle = listPayment.getItemAtPosition(position).toString();
                    CPPOBProduct paymentProduct = product.productList.get(position);
                    Intent intent = new Intent(PaymentActivity.this, PaymentProcessActivity.class);
                    intent.putExtra(PaymentProcessActivity.TAG_ACTIVITYTITLE, strTitle);
                    intent.putExtra(PaymentProcessActivity.TAG_ACTIVITYPRODUCTCODE, paymentProduct.productCode);
                    intent.putExtra(PaymentProcessActivity.TAG_ACTIVITYPRODUCTNAME, paymentProduct.productName);
                    startActivity(intent);
                }
            });
        }
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
            PaymentActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
