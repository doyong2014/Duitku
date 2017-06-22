package com.mi1.duitku.Tab5.Register;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.badoualy.stepperindicator.StepperIndicator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.Common.CommonFunction;
import com.mi1.duitku.Common.Constant;
import com.mi1.duitku.Common.PackageDetailInfo;
import com.mi1.duitku.Common.PackageList;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab5.HelpActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RegisterChildActivity extends AppCompatActivity {

    private  StepperIndicator indicator;
    private  ViewPager pager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_child);

        pager = (ViewPager) findViewById(R.id.pager);
        assert pager != null;
        pager.setAdapter(new EmptyPagerAdapter(getSupportFragmentManager()));

        indicator = (StepperIndicator) findViewById(R.id.stepper_indicator);
        assert indicator != null;
        // We keep last page for a "finishing" page
        indicator.setViewPager(pager, true);

        indicator.addOnStepClickListener(new StepperIndicator.OnStepClickListener() {
            @Override
            public void onStepClicked(int step) {
                pager.setCurrentItem(step, true);
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
            RegisterChildActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }


    public void jumpToPage(View view) {
        if (indicator.getCurrentStep() != indicator.getStepCount()) {
            //indicator.setCurrentStep(indicator.getCurrentStep() + 1);
            pager.setCurrentItem(pager.getCurrentItem() + 1, true);
        }
    }

    private class EmptyPagerAdapter extends FragmentPagerAdapter {

        public EmptyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new StepRegisterOneFragment();
            }
            else if (position == 2) {
                return new StepRegisterTwoFragment();
            }
            else if (position == 3) {
                return new StepRegisterThreeFragment();
            }
            else if (position == 1) {
                return new StepRegisterFourFragment();
            }
            else
                return StepRegisterFragment.newInstance(position + 1, position == getCount() - 1);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }
    }

    public class PackageAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
//            progress.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... param) {

            String result = null;

            try {

                URL url = new URL(Constant.URL_GETCOUNTRY);//(Constant.LOGIN_PAGE); //+ "?loginUrl=" + Constant.URLLOGINDIGI1);


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                InputStream wr = conn.getInputStream();

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
        protected void onPostExecute(String result) {

            if (result == null){
                return;
            }

            try {

                JSONObject jsonObj = new JSONObject(result);
                boolean status = (boolean)jsonObj.get("status");

                if (status){
                    JSONArray ja = jsonObj.getJSONArray("listpackage");
                    Gson gson = new GsonBuilder().create();
//                    AppGlobal._packageList = gson.fromJson(result, PackageList.class);
//                    AppGlobal._userInfo = CommonFunction.JimmyApitoUserInfo(ja.getJSONObject(0)); //get first occurence
                    List<PackageList> packageList = new ArrayList<PackageList>();
                    for(int a = 0; a< ja.length(); a++)
                    {
                        String data = ja.getString(a);
                        packageList.add(gson.fromJson(data,PackageList.class));
//                        JSONObject jo = new JSONObject(data);
                    }
                    AppGlobal._packageList= packageList;
                }
                else {
                    String error = jsonObj.getString("errors");
//                    dispError(error);
                }


            } catch (Exception e) {
                // TODO: handle exception
                Log.e("oasis", e.getMessage());
            }
        }
    }
}
