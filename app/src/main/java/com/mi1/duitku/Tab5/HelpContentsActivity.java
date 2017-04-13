package com.mi1.duitku.Tab5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mi1.duitku.R;

public class HelpContentsActivity extends AppCompatActivity {

    private String title = "";
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_contents);

        Intent intent = getIntent();
        title = intent.getExtras().getString("title");
        position = intent.getExtras().getInt("position");

        TextView tvContents = (TextView)findViewById(R.id.txt_contents);
        String conetnts = getResources().getStringArray(R.array.help_content)[position];
        tvContents.setText(conetnts);

        final NestedScrollView scroll = (NestedScrollView)findViewById(R.id.scroll);
        LinearLayout llUpArrow = (LinearLayout) findViewById(R.id.ll_top);
        llUpArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scroll.fullScroll(ScrollView.FOCUS_UP);
            }
        });

        LinearLayout llBack = (LinearLayout) findViewById(R.id.ll_back);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpContentsActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(title);
        actionBar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.actionbar_bg));

        getMenuInflater().inflate(R.menu.menu_content, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_text_size) {

        } else if (id == R.id.action_share) {

        } else if (id == android.R.id.home) {
            HelpContentsActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}