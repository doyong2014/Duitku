package com.mi1.duitku.Tab5;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.mi1.duitku.BaseActivity;
import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.Common.PackageDetailInfo;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab5.Adapter.PackageAdapter;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import de.measite.minidns.record.A;

public class ShareCodeActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        ImageView ivBlurPhoto = (ImageView) findViewById(R.id.img_full);
        CircleImageView civUserPhoto = (CircleImageView) findViewById(R.id.civ_user_photo);
        final Spinner spinnerPackage = (Spinner) findViewById(R.id.spinner_package);

        final TextView tvName = (TextView)findViewById(R.id.txt_name);
        final TextView tvTotalPartner = (TextView)findViewById(R.id.txtTotalPartners);
        final TextView tvCummulativeRP = (TextView)findViewById(R.id.txtCummulativeRP);
        final TextView tvLGI = (TextView)findViewById(R.id.txtLGI);
        final TextView tvCummulativeDayLP = (TextView)findViewById(R.id.txtCummulativeDayLP);
        final TextView tvLP = (TextView)findViewById(R.id.txtLP);
        final TextView tvRW = (TextView)findViewById(R.id.txtRW);
        final TextView tvWP = (TextView)findViewById(R.id.txtWP);
        final TextView tvPP = (TextView)findViewById(R.id.txtPP);
        final TextView tvCP = (TextView)findViewById(R.id.txtCP);
        final TextView tvMP = (TextView)findViewById(R.id.txtMP);

        List<String> packageDetailInfoList = new ArrayList<String>();
        PackageAdapter adapter;// = new ArrayAdapter(this,android.R.layout.simple_spinner_item, packageDetailInfoList);

        if (!AppGlobal._userInfo.picUrl.isEmpty()) {
            Picasso.with(this).load(AppGlobal._userInfo.picUrl.toLowerCase()).fit().transform(new BlurTransformation(this)).into(ivBlurPhoto);
            Picasso.with(this).load(AppGlobal._userInfo.picUrl.toLowerCase()).fit().into(civUserPhoto);
        }
        if(AppGlobal._userInfo.packageDetail != null)
        {
            for(int a = 0;a < AppGlobal._userInfo.packageDetail.size(); a++)
            {
                packageDetailInfoList.add(AppGlobal._userInfo.packageDetail.get(a).package_name);
            }
            adapter = new PackageAdapter(this, packageDetailInfoList);
            spinnerPackage.setAdapter(adapter);
            spinnerPackage.setOnItemSelectedListener(this);
//            spinnerPackage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    tvTotalPartner.setText(AppGlobal._userInfo.packageDetail.get(position).children_left.replace(".00","") + "/" + AppGlobal._userInfo.packageDetail.get(position).children_right.replace(".00",""));
//                    tvCummulativeRP.setText(AppGlobal._userInfo.packageDetail.get(position).komulative_vp_left.replace(".00","") + "/" + AppGlobal._userInfo.packageDetail.get(position).komulative_vp_right.replace(".00",""));
//                    tvLGI.setText(AppGlobal._userInfo.packageDetail.get(position).pairing_vp_left.replace(".00","") + "/" + AppGlobal._userInfo.packageDetail.get(position).pairing_vp_right.replace(".00",""));
//                    tvCummulativeDayLP.setText(AppGlobal._userInfo.packageDetail.get(position).day_vp_left.replace(".00","") + "/" + AppGlobal._userInfo.packageDetail.get(position).day_vp_right.replace(".00",""));
//                    tvLP.setText(AppGlobal._userInfo.packageDetail.get(position).lp.replace(".00",""));
//                    tvRW.setText(AppGlobal._userInfo.packageDetail.get(position).rv.replace(".00",""));
//                    tvWP.setText(AppGlobal._userInfo.packageDetail.get(position).wp.replace(".00",""));
//                    tvPP.setText(AppGlobal._userInfo.packageDetail.get(position).pp.replace(".00",""));
//                    tvCP.setText(AppGlobal._userInfo.packageDetail.get(position).cp.replace(".00",""));
//                    tvMP.setText(AppGlobal._userInfo.packageDetail.get(position).mp.replace(".00",""));
//                }
//            });
        }

        tvName.setText(AppGlobal._userInfo.name + " - Wallet Information");
        tvTotalPartner.setText(AppGlobal._userInfo.packageDetail.get(0).children_left.replace(".00","") + "/" + AppGlobal._userInfo.packageDetail.get(0).children_right.replace(".00",""));
        tvCummulativeRP.setText(AppGlobal._userInfo.packageDetail.get(0).komulative_vp_left.replace(".00","") + "/" + AppGlobal._userInfo.packageDetail.get(0).komulative_vp_right.replace(".00",""));
        tvLGI.setText(AppGlobal._userInfo.packageDetail.get(0).pairing_vp_left.replace(".00","") + "/" + AppGlobal._userInfo.packageDetail.get(0).pairing_vp_right.replace(".00",""));
        tvCummulativeDayLP.setText(AppGlobal._userInfo.packageDetail.get(0).day_vp_left.replace(".00","") + "/" + AppGlobal._userInfo.packageDetail.get(0).day_vp_right.replace(".00",""));
        tvLP.setText(AppGlobal._userInfo.packageDetail.get(0).lp.replace(".00",""));
        tvRW.setText(AppGlobal._userInfo.packageDetail.get(0).rv.replace(".00",""));
        tvWP.setText(AppGlobal._userInfo.packageDetail.get(0).wp.replace(".00",""));
        tvPP.setText(AppGlobal._userInfo.packageDetail.get(0).pp.replace(".00",""));
        tvCP.setText(AppGlobal._userInfo.packageDetail.get(0).cp.replace(".00",""));
        tvMP.setText(AppGlobal._userInfo.packageDetail.get(0).mp.replace(".00",""));

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id)
    {
        final TextView tvTotalPartner = (TextView)findViewById(R.id.txtTotalPartners);
        final TextView tvCummulativeRP = (TextView)findViewById(R.id.txtCummulativeRP);
        final TextView tvLGI = (TextView)findViewById(R.id.txtLGI);
        final TextView tvCummulativeDayLP = (TextView)findViewById(R.id.txtCummulativeDayLP);
        final TextView tvLP = (TextView)findViewById(R.id.txtLP);
        final TextView tvRW = (TextView)findViewById(R.id.txtRW);
        final TextView tvWP = (TextView)findViewById(R.id.txtWP);
        final TextView tvPP = (TextView)findViewById(R.id.txtPP);
        final TextView tvCP = (TextView)findViewById(R.id.txtCP);
        final TextView tvMP = (TextView)findViewById(R.id.txtMP);

        tvTotalPartner.setText(AppGlobal._userInfo.packageDetail.get(position).children_left.replace(".00","") + "/" + AppGlobal._userInfo.packageDetail.get(position).children_right.replace(".00",""));
        tvCummulativeRP.setText(AppGlobal._userInfo.packageDetail.get(position).komulative_vp_left.replace(".00","") + "/" + AppGlobal._userInfo.packageDetail.get(position).komulative_vp_right.replace(".00",""));
        tvLGI.setText(AppGlobal._userInfo.packageDetail.get(position).pairing_vp_left.replace(".00","") + "/" + AppGlobal._userInfo.packageDetail.get(position).pairing_vp_right.replace(".00",""));
        tvCummulativeDayLP.setText(AppGlobal._userInfo.packageDetail.get(position).day_vp_left.replace(".00","") + "/" + AppGlobal._userInfo.packageDetail.get(position).day_vp_right.replace(".00",""));
        tvLP.setText(AppGlobal._userInfo.packageDetail.get(position).lp.replace(".00",""));
        tvRW.setText(AppGlobal._userInfo.packageDetail.get(position).rv.replace(".00",""));
        tvWP.setText(AppGlobal._userInfo.packageDetail.get(position).wp.replace(".00",""));
        tvPP.setText(AppGlobal._userInfo.packageDetail.get(position).pp.replace(".00",""));
        tvCP.setText(AppGlobal._userInfo.packageDetail.get(position).cp.replace(".00",""));
        tvMP.setText(AppGlobal._userInfo.packageDetail.get(position).mp.replace(".00",""));
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
        final TextView tvTotalPartner = (TextView)findViewById(R.id.txtTotalPartners);
        final TextView tvCummulativeRP = (TextView)findViewById(R.id.txtCummulativeRP);
        final TextView tvLGI = (TextView)findViewById(R.id.txtLGI);
        final TextView tvCummulativeDayLP = (TextView)findViewById(R.id.txtCummulativeDayLP);
        final TextView tvLP = (TextView)findViewById(R.id.txtLP);
        final TextView tvRW = (TextView)findViewById(R.id.txtRW);
        final TextView tvWP = (TextView)findViewById(R.id.txtWP);
        final TextView tvPP = (TextView)findViewById(R.id.txtPP);
        final TextView tvCP = (TextView)findViewById(R.id.txtCP);
        final TextView tvMP = (TextView)findViewById(R.id.txtMP);

        tvTotalPartner.setText(AppGlobal._userInfo.packageDetail.get(0).children_left.replace(".00","") + "/" + AppGlobal._userInfo.packageDetail.get(0).children_right.replace(".00",""));
        tvCummulativeRP.setText(AppGlobal._userInfo.packageDetail.get(0).komulative_vp_left.replace(".00","") + "/" + AppGlobal._userInfo.packageDetail.get(0).komulative_vp_right.replace(".00",""));
        tvLGI.setText(AppGlobal._userInfo.packageDetail.get(0).pairing_vp_left.replace(".00","") + "/" + AppGlobal._userInfo.packageDetail.get(0).pairing_vp_right.replace(".00",""));
        tvCummulativeDayLP.setText(AppGlobal._userInfo.packageDetail.get(0).day_vp_left.replace(".00","") + "/" + AppGlobal._userInfo.packageDetail.get(0).day_vp_right.replace(".00",""));
        tvLP.setText(AppGlobal._userInfo.packageDetail.get(0).lp.replace(".00",""));
        tvRW.setText(AppGlobal._userInfo.packageDetail.get(0).rv.replace(".00",""));
        tvWP.setText(AppGlobal._userInfo.packageDetail.get(0).wp.replace(".00",""));
        tvPP.setText(AppGlobal._userInfo.packageDetail.get(0).pp.replace(".00",""));
        tvCP.setText(AppGlobal._userInfo.packageDetail.get(0).cp.replace(".00",""));
        tvMP.setText(AppGlobal._userInfo.packageDetail.get(0).mp.replace(".00",""));
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