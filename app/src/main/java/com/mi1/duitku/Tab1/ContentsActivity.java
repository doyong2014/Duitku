package com.mi1.duitku.Tab1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mi1.duitku.R;
import com.mi1.duitku.Tab1.Common.DataModel;
import com.mi1.duitku.Tab1.Common.Tab1Global;
import com.squareup.picasso.Picasso;

public class ContentsActivity extends AppCompatActivity {

    private String title = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);

        ImageView ivThumb = (ImageView)findViewById(R.id.img_full);
        TextView tvTitle = (TextView)findViewById(R.id.txt_title);
        TextView tvPostTime = (TextView)findViewById(R.id.txt_time);
        TextView tvContents = (TextView)findViewById(R.id.txt_content);

        Intent intent = getIntent();
        int tab = intent.getExtras().getInt("tab");
        int position = intent.getExtras().getInt("position");

        DataModel.Post item = null;
        if (tab == 1) {
            item = Tab1Global._newsData.get(position);
            title = "NEWS";
        }else if(tab == 2) {
            item = Tab1Global._promovData.get(position);
            title = "PROMOV";
        }else if(tab == 3) {
            item = Tab1Global._eventsData.get(position);
            title = "EVENTS";
        }else if(tab == 4) {
            item = Tab1Global._searchData.get(position);
            title = "NEWS";
        }

        Picasso.with(this).load(item.thumbnail_images.medium.url).into(ivThumb);
        tvTitle.setText(item.title);
        tvPostTime.setText(item.date);
        tvContents.setText(fromHtml(item.content));

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(title);
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });

        final NestedScrollView scroll = (NestedScrollView)findViewById(R.id.scroll);
        LinearLayout upArrow = (LinearLayout) findViewById(R.id.ll_top);
        upArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scroll.pageScroll(0);
            }
        });

        LinearLayout llBack = (LinearLayout) findViewById(R.id.ll_back);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentsActivity.this.finish();
            }
        });

    }

    public static Spanned fromHtml(String source) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            // noinspection deprecation
            return Html.fromHtml(source);
        }
        return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        getMenuInflater().inflate(R.menu.menu_content, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_text_size) {

        } else if (id == R.id.action_share) {

        } else if (id == android.R.id.home) {
            ContentsActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}