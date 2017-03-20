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
import com.mi1.duitku.Tab3.Common.CPPOBProductParent;
import com.mi1.duitku.Tab3.Common.Tab3Global;

/**
 * Created by WORYA on 3/16/2016.
 */
public class PurchaseActivity extends AppCompatActivity {

    private CPPOBProductParent product = new CPPOBProductParent();
    private ListView listPurchase;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppoblist);

        for (int i = 0; i < Tab3Global._productPurchase.child.size(); i++) {
            CPPOBProductParent tempProduct = Tab3Global._productPurchase.child.get(i);
            if (tempProduct.name.equals("PULSA HP")) {
                product = tempProduct;
            }
        }

        if (product.child.size() > 0) {
            final ArrayAdapter adapter = new ArrayAdapter<CPPOBProductParent>(getApplicationContext(), R.layout.list_ppob, R.id.txt_ppobproduct, product.child);
            listPurchase = (ListView) findViewById(R.id.listPPOBProduct);
            listPurchase.setAdapter(adapter);
            listPurchase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String strTitle = listPurchase.getItemAtPosition(position).toString();
                    CPPOBProductParent purchaseProduct = product.child.get(position);
                    Intent intent = new Intent(PurchaseActivity.this, PurchaseProcessActivity.class);
                    intent.putExtra(PurchaseProcessActivity.TAG_ACTIVITYTITLE, strTitle);
                    intent.putExtra(PurchaseProcessActivity.TAG_ACTIVITYPRODUCT, purchaseProduct);
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
            PurchaseActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
