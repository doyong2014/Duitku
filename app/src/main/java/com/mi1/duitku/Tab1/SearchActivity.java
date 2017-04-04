package com.mi1.duitku.Tab1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mi1.duitku.Common.Constant;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab1.Common.DataModel;
import com.mi1.duitku.Tab1.Common.Tab1Global;
import com.mi1.duitku.Tab5.HelpContentsActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by WORYA on 3/16/2016.
 */
public class SearchActivity extends AppCompatActivity {

    private int cur_tab = 0;
    private String keywords;
    private ProgressDialog progress;
    private ArrayAdapter adapter;
    private EditText etKeyword;
    private ArrayList<String> searchList = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        cur_tab = intent.getExtras().getInt("curTab");
        keywords = intent.getExtras().getString("keywords");

        if(cur_tab == 0) {
            return;
        }

        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.wait));
        progress.setCanceledOnTouchOutside(false);

        retrieveResult();

        adapter = new ArrayAdapter<String>(this, R.layout.list_ppob, R.id.txt_ppobproduct, searchList);
        ListView listview = (ListView) findViewById(R.id.list_item);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, ContentsActivity.class);
                intent.putExtra("tab", 4);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }

    private void retrieveResult(){

        searchList.clear();
        Tab1Global._searchData.clear();
        String[] params = new String[1];
        params[0] = Constant.SEARCH_NEWS_PAGE + keywords;
        searchAsync _getNewsAsync = new searchAsync();
        _getNewsAsync.execute(params);
    }

    public class searchAsync extends AsyncTask<String, Integer, String> {

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

                URL url = new URL(param[0]);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(20000); /* milliseconds */
                conn.setConnectTimeout(30000); /* milliseconds */
                conn.setUseCaches(false);
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestMethod("POST");
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

            progress.dismiss();
            if (result == null){
                Toast.makeText(SearchActivity.this, R.string.error_failed_connect, Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Gson gson = new GsonBuilder().create();
                DataModel newsData = gson.fromJson(result, DataModel.class);

                Collections.addAll(Tab1Global._searchData, newsData.posts);
                for (int i= 0; i<Tab1Global._searchData.size(); i++) {
                    searchList.add(Tab1Global._searchData.get(i).title);
                }
                adapter.notifyDataSetChanged();

            } catch (Exception e) {
                // TODO: handle exception
                //Log.e("oasis", e.toString());
            }
        }
    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setTitle(keywords);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.argb(255,255,255,255)));
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.actionbar_search1, null);
        etKeyword = (EditText)mCustomView.findViewById(R.id.edt_keywords);
        actionBar.setCustomView(mCustomView);

        getMenuInflater().inflate(R.menu.menu_search, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            SearchActivity.this.finish();
        } else if(id == R.id.action_search) {
            keywords = etKeyword.getText().toString();
            hideKeyboard();
            retrieveResult();
        }

        return super.onOptionsItemSelected(item);
    }

}
