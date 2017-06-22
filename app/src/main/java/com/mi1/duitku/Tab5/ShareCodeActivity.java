package com.mi1.duitku.Tab5;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mi1.duitku.BaseActivity;
import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.Common.CommonFunction;
import com.mi1.duitku.Common.Constant;
import com.mi1.duitku.Common.PackageDetailInfo;
import com.mi1.duitku.Common.UserInfo;
import com.mi1.duitku.LoginActivity;
import com.mi1.duitku.Main.MainActivity;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab3.TransferActivity;
import com.mi1.duitku.Tab5.Adapter.PackageAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import de.measite.minidns.record.A;

public class ShareCodeActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    private ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.wait));
        progress.setCanceledOnTouchOutside(false);

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

        String[] paramProfile = new String[2];
        paramProfile[0] = AppGlobal._userInfo.phoneNumber;
        paramProfile[1] = "telpnum";
        GetProfileAsync _profileAsync = new GetProfileAsync();
        _profileAsync.execute(paramProfile);

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
        }

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
        tvCummulativeRP.setText(CommonFunction.formatNumberingWithoutRP(AppGlobal._userInfo.packageDetail.get(position).komulative_vp_left.replace(".00","")) + "/" + CommonFunction.formatNumberingWithoutRP(AppGlobal._userInfo.packageDetail.get(position).komulative_vp_right.replace(".00","")));
        tvLGI.setText(CommonFunction.formatNumberingWithoutRP(AppGlobal._userInfo.packageDetail.get(position).pairing_vp_left.replace(".00","")) + "/" + CommonFunction.formatNumberingWithoutRP(AppGlobal._userInfo.packageDetail.get(position).pairing_vp_right.replace(".00","")));
        tvCummulativeDayLP.setText(CommonFunction.formatNumberingWithoutRP(AppGlobal._userInfo.packageDetail.get(position).day_vp_left.replace(".00","")) + "/" + CommonFunction.formatNumberingWithoutRP(AppGlobal._userInfo.packageDetail.get(position).day_vp_right.replace(".00","")));
        tvLP.setText(CommonFunction.formatNumberingWithoutRP(AppGlobal._userInfo.packageDetail.get(position).lp.replace(".00","")));
        tvRW.setText(CommonFunction.formatNumberingWithoutRP(AppGlobal._userInfo.packageDetail.get(position).rv.replace(".00","")));
        tvWP.setText(CommonFunction.formatNumberingWithoutRP(AppGlobal._userInfo.packageDetail.get(position).wp.replace(".00","")));
        tvPP.setText(CommonFunction.formatNumberingWithoutRP(AppGlobal._userInfo.packageDetail.get(position).pp.replace(".00","")));
        tvCP.setText(CommonFunction.formatNumberingWithoutRP(AppGlobal._userInfo.packageDetail.get(position).cp.replace(".00","")));
        tvMP.setText(CommonFunction.formatNumberingWithoutRP(AppGlobal._userInfo.packageDetail.get(position).mp.replace(".00","")));
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
        tvCummulativeRP.setText(CommonFunction.formatNumberingWithoutRP(AppGlobal._userInfo.packageDetail.get(0).komulative_vp_left.replace(".00","")) + "/" + CommonFunction.formatNumberingWithoutRP(AppGlobal._userInfo.packageDetail.get(0).komulative_vp_right.replace(".00","")));
        tvLGI.setText(CommonFunction.formatNumberingWithoutRP(AppGlobal._userInfo.packageDetail.get(0).pairing_vp_left.replace(".00","")) + "/" + CommonFunction.formatNumberingWithoutRP(AppGlobal._userInfo.packageDetail.get(0).pairing_vp_right.replace(".00","")));
        tvCummulativeDayLP.setText(CommonFunction.formatNumberingWithoutRP(AppGlobal._userInfo.packageDetail.get(0).day_vp_left.replace(".00","")) + "/" + CommonFunction.formatNumberingWithoutRP(AppGlobal._userInfo.packageDetail.get(0).day_vp_right.replace(".00","")));
        tvLP.setText(CommonFunction.formatNumberingWithoutRP(AppGlobal._userInfo.packageDetail.get(0).lp.replace(".00","")));
        tvRW.setText(CommonFunction.formatNumberingWithoutRP(AppGlobal._userInfo.packageDetail.get(0).rv.replace(".00","")));
        tvWP.setText(CommonFunction.formatNumberingWithoutRP(AppGlobal._userInfo.packageDetail.get(0).wp.replace(".00","")));
        tvPP.setText(CommonFunction.formatNumberingWithoutRP(AppGlobal._userInfo.packageDetail.get(0).pp.replace(".00","")));
        tvCP.setText(CommonFunction.formatNumberingWithoutRP(AppGlobal._userInfo.packageDetail.get(0).cp.replace(".00","")));
        tvMP.setText(CommonFunction.formatNumberingWithoutRP(AppGlobal._userInfo.packageDetail.get(0).mp.replace(".00","")));
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

    public class GetProfileAsync extends AsyncTask<String, Integer, String>
    {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            progress.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... param) {

            String result = null;

            try {

                URL url = new URL(Constant.URL_GET_PROFILE_DIGI1);//(Constant.LOGIN_PAGE); //+ "?loginUrl=" + Constant.URLLOGINDIGI1);
                StringBuilder postData = new StringBuilder();
                postData.append("username=" + AppGlobal._userInfo.phoneNumber + "&");
                postData.append("type=" + "telpnum");
                byte[] postDataBytes = postData.toString().getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000); /* milliseconds */
                conn.setConnectTimeout(15000); /* milliseconds */
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                wr.write(postData.toString());
                wr.flush();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();
                    String str;

                    while((str = reader.readLine()) != null){
                        builder.append(str+"\n");
                    }

                    result = builder.toString();
                } else {
                    result = String.valueOf(conn.getResponseCode());
                }

            } catch (MalformedURLException e){
                //Log.e("oasis", e.toString());
            } catch (IOException e) {
                //Log.e("oasis", e.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result)
        {
            progress.dismiss();
            if (result == null){
                Toast.makeText(ShareCodeActivity.this, getString(R.string.error_failed_connect), Toast.LENGTH_SHORT).show();
                return;
            } else if(result.equals("401")) {
                Toast.makeText(ShareCodeActivity.this, "Sesi anda telah habis", Toast.LENGTH_SHORT).show();
                logout();
                return;
            }

            try {

                JSONObject jsonObj = new JSONObject(result);
                boolean status = (boolean)jsonObj.get("status");

                if (status){
                    JSONArray ja = jsonObj.getJSONArray("obj");
                    Gson gson = new GsonBuilder().create();
                    AppGlobal._userInfo = new UserInfo();
                    AppGlobal._userInfo = CommonFunction.JimmyApitoUserInfo(ja.getJSONObject(0)); //get first occurence
                    List<PackageDetailInfo> packageDetailInfoList = new ArrayList<PackageDetailInfo>();
                    for(int a = 0; a< ja.length(); a++)
                    {
                        String data = ja.getString(a);
                        packageDetailInfoList.add(gson.fromJson(data,PackageDetailInfo.class));
                    }
                    AppGlobal._userInfo.packageDetail = packageDetailInfoList;
                }
                else {
                    progress.dismiss();
                    String msg = jsonObj.getString("msg");
                    Toast.makeText(ShareCodeActivity.this, msg, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                // TODO: handle exception
                //Log.e("oasis", e.toString());
            }
        }
    }
    private void logout() {
        AppGlobal._userInfo = null;
        AppGlobal._userDetailInfo = null;
        Intent intent = new Intent(ShareCodeActivity.this, LoginActivity.class);
        startActivity(intent);
        ShareCodeActivity.this.finish();
        MainActivity._instance.finish();
    }
}