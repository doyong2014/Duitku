package com.mi1.duitku.Tab5;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.mi1.duitku.BaseActivity;
import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShareCodeActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        ImageView ivBlurPhoto = (ImageView) findViewById(R.id.img_full);
        CircleImageView civUserPhoto = (CircleImageView) findViewById(R.id.civ_user_photo);

        if (!AppGlobal._userInfo.picUrl.isEmpty()) {
            Picasso.with(this).load(AppGlobal._userInfo.picUrl.toLowerCase()).fit().transform(new BlurTransformation(this)).into(ivBlurPhoto);
            Picasso.with(this).load(AppGlobal._userInfo.picUrl.toLowerCase()).fit().into(civUserPhoto);
        }

        TextView tvName = (TextView)findViewById(R.id.txt_name);
        tvName.setText(AppGlobal._userInfo.name + " - Wallet Information");
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
            ShareCodeActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}