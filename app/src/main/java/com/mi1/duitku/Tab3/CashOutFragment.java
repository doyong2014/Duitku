package com.mi1.duitku.Tab3;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.Common.Constant;
import com.mi1.duitku.Common.FooterView;
import com.mi1.duitku.Common.HeaderView;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab3.Common.CashInfo;
import com.mi1.duitku.Tab3.Common.Tab3Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class CashOutFragment extends Fragment {

    private LinearLayoutManager layoutManager;
    private TwinklingRefreshLayout refresh;
    private RecyclerView recycler;
    private CashOutAdapter adapter;
    private Context _context;

    public CashOutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_cash, container, false);
        _context = getContext();

        refresh = (TwinklingRefreshLayout) view.findViewById(R.id.refreshLayout);
        HeaderView headerView = (HeaderView) View.inflate(getActivity(), R.layout.header_refresh, null);
        refresh.setHeaderView(headerView);

        FooterView footerView = (FooterView) View.inflate(getActivity(), R.layout.footer_loadmore, null);
        refresh.setBottomView(footerView);

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler = (RecyclerView) view.findViewById(R.id.recycler_cash);
        recycler.setLayoutManager(layoutManager);
        recycler.addItemDecoration(new DividerItemDecoration(_context));

        adapter = new CashOutAdapter(_context);
        recycler.setAdapter(adapter);

        refresh.setOnRefreshListener(new RefreshListenerAdapter() {

            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                Tab3Global._cashOutInfo.curPage = 1;
                getCashOut(Tab3Global._cashOutInfo.curPage, Tab3Global._cashOutInfo.refreshItemNum);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                if (Tab3Global._cashOutInfo.pagesNum < Tab3Global._cashOutInfo.curPage+1) {
                    refreshLayout.finishLoadmore();
                    Toast.makeText(_context, "No more contents", Toast.LENGTH_SHORT).show();
                }else {
                    Tab3Global._cashOutInfo.curPage++;
                    getCashOut(Tab3Global._cashOutInfo.curPage, Tab3Global._cashOutInfo.refreshItemNum);
                }
            }
        });

        if(Tab3Global._cashOutData.size() == 0) {
            refresh.startRefresh();
        }

        return view;
    }

    private void getCashOut(int page, int itemNum){

        String[] params = new String[6];
        params[0] = AppGlobal._userInfo.token;
        params[1] = Constant.COMMUNITY_CODE;
        params[2] = null;
        params[3] = null;
        params[4] = String.valueOf(page);
        params[5] = String.valueOf(itemNum);
        CashOutAsync _getCashOutAsync = new CashOutAsync();
        _getCashOutAsync.execute(params);
    }

    public class CashOutAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... param) {

            String result = null;

            try {

                URL url = new URL(Constant.CASH_OUT_PAGE);

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("token", param[0]);
                    jsonObject.put("community_code", param[1]);
                    jsonObject.put("dateFrom", param[2]);
                    jsonObject.put("dateTo", param[3]);
                    jsonObject.put("page", param[4]);;
                    jsonObject.put("pageSize", param[5]);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000); /* milliseconds */
                conn.setConnectTimeout(15000); /* milliseconds */
                conn.setUseCaches(false);
                conn.setRequestProperty("content-type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                wr.write(jsonObject.toString());
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
        protected void onPostExecute(String result) {

            refresh.finishRefreshing();
            refresh.finishLoadmore();

            if (result == null){
                Toast.makeText(getActivity(), R.string.error_failed_connect, Toast.LENGTH_SHORT).show();
                return;
            }

            try {

                Gson gson = new GsonBuilder().create();
                CashInfo cashOutData = gson.fromJson(result, CashInfo.class);

                Tab3Global._cashOutInfo.pagesNum = Integer.valueOf(cashOutData.totalPageSize);

                if(Tab3Global._cashOutInfo.curPage == 1) {
                    Tab3Global._cashOutData.clear();
                }

                Collections.addAll(Tab3Global._cashOutData, cashOutData.transactionList);

                adapter.notifyDataSetChanged();

            } catch (Exception e) {
                // TODO: handle exception
                //Log.e("oasis", e.toString());
            }
        }
    }
}