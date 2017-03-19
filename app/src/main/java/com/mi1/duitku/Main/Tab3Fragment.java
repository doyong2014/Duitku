package com.mi1.duitku.Main;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mi1.duitku.Common.Constant;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab3.Common.CPPOBProduct;
import com.mi1.duitku.Tab3.Common.CPPOBProductParent;
import com.mi1.duitku.Tab3.Common.Tab3Global;
import com.mi1.duitku.Tab3.TabPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab3Fragment extends Fragment {

    private TabLayout topTap;
    private ViewPager viewPager;
    private ProgressDialog progress;

    public Tab3Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_tab, container, false);

        progress = new ProgressDialog(getContext());
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        if (Tab3Global.m_product_payment == null || Tab3Global.m_product_purchase == null) {
            new callProductList().execute();
        }

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        // Initializing the TabLayout
        topTap = (TabLayout) view.findViewById(R.id.tab_top);
        topTap.addTab(topTap.newTab().setText("DOMPET"));
        topTap.addTab(topTap.newTab().setText("CASH IN"));
        topTap.addTab(topTap.newTab().setText("CASH OUT"));
        topTap.setTabGravity(TabLayout.GRAVITY_FILL);
        topTap.setTabMode(TabLayout.MODE_FIXED);
        topTap.setSelectedTabIndicatorColor(Color.YELLOW);

        // Initializing ViewPager
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);

        // Creating TabPagerAdapter adapter
        TabPagerAdapter pagerAdapter = new TabPagerAdapter(activity.getSupportFragmentManager(), topTap.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(topTap));

        // Set TabSelectedListener
        topTap.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    public class callProductList extends AsyncTask<String, Void, String> {

//        ReturnStatus returnStatus = new ReturnStatus();

        @Override
        protected void onPreExecute() {
            progress.setMessage(getString(R.string.wait));
            progress.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String result = null;

            try {

                URL url = new URL(Constant.PRODUCT_LIST_PAGE);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(20000); /* milliseconds */
                conn.setConnectTimeout(30000); /* milliseconds */
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();
                    String str;

                    while((str = reader.readLine()) != null){
                        builder.append(str+"\n");
                    }

                    result = builder.toString();
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

            progress.dismiss();

            if (result == null){
                Toast.makeText(getContext(), R.string.error_failed_connect, Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONArray objArr = new JSONArray(result);
                for (int i = 0; i < objArr.length(); i++) {
                    JSONObject obj = objArr.getJSONObject(i);
                    if (obj.getString("name").toString().toUpperCase().equals("PPOB PRA BAYAR")) {
                        Tab3Global.m_product_purchase = ConvertJsontoCCPOBProductParent(obj);
                    }
                    else if (obj.getString("name").toString().toUpperCase().equals("PPOB PASCA BAYAR")) {
                        Tab3Global.m_product_payment = ConvertJsontoCCPOBProductParent(obj);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private CPPOBProductParent ConvertJsontoCCPOBProductParent(JSONObject obj) throws JSONException {
        CPPOBProductParent product = new CPPOBProductParent();
        product.id = obj.getInt("id");
        product.name = obj.getString("name");
        JSONArray childArr = obj.getJSONArray("child");
        if (childArr.length() > 0) {
            for (int i = 0; i < childArr.length(); i++) {
                CPPOBProductParent childProduct = ConvertJsontoCCPOBProductParent(childArr.getJSONObject(i));
                product.child.add(childProduct);
            }
        }
        JSONArray productList = obj.getJSONArray("productList");
        if (productList.length() > 0) {
            for (int i = 0; i < productList.length(); i++) {
                CPPOBProduct childProduct = new CPPOBProduct();
                JSONObject objChild = productList.getJSONObject(i);
                childProduct.productCode = objChild.getString("productCode");
                childProduct.productName = objChild.getString("productName");
                childProduct.productType = objChild.getString("productType");
                childProduct.productPrice = objChild.getDouble("productPrice");
                product.productList.add(childProduct);
            }
        }
        return product;
    }
}
