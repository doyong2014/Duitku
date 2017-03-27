package com.mi1.duitku.Tab5;

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

import java.util.ArrayList;

/**
 * Created by WORYA on 3/16/2016.
 */
public class HelpActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        final ArrayList<String> helpList = new ArrayList<String>();
        helpList.add("Cara Top-up");
        helpList.add("Cara Transfer");
        helpList.add("Cara Cash out");
        helpList.add("Cara Dafter");
        helpList.add("Pengetahuan Wallet");

        final ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_ppob, R.id.txt_ppobproduct, helpList);
        ListView listview = (ListView) findViewById(R.id.list_item);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HelpActivity.this, HelpContentsActivity.class);
                intent.putExtra("title", helpList.get(position).toString());
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

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
            HelpActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
