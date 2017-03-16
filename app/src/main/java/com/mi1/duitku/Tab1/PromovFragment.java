package com.mi1.duitku.Tab1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.mi1.duitku.Common.Constant;
import com.mi1.duitku.Common.FooterView;
import com.mi1.duitku.Common.HeaderView;
import com.mi1.duitku.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class PromovFragment extends Fragment {

    private LinearLayoutManager mLinearLayoutManager;
    private TwinklingRefreshLayout refreshPromov;
    private RecyclerView recycler;
    private PromovAdapter adapter;

    public PromovFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_promov, container, false);

        refreshPromov = (TwinklingRefreshLayout) view.findViewById(R.id.refreshLayout);
        HeaderView headerView = (HeaderView) View.inflate(getActivity(), R.layout.header_refresh, null);
        refreshPromov.setHeaderView(headerView);

        FooterView footerView = (FooterView) View.inflate(getActivity(), R.layout.footer_loadmore, null);
        refreshPromov.setBottomView(footerView);

        LinearLayout upArrow = (LinearLayout) view.findViewById(R.id.ll_top);
        upArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycler.smoothScrollToPosition(0);
            }
        });

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler = (RecyclerView) view.findViewById(R.id.recycler_promov);
        recycler.setLayoutManager(mLinearLayoutManager);

        adapter = new PromovAdapter(getActivity());
        recycler.setAdapter(adapter);

        refreshPromov.setOnRefreshListener(new RefreshListenerAdapter() {

            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                GlobalData._promovInfo.curPage = 1;
                getPromov(GlobalData._promovInfo.refreshItemNum, GlobalData._promovInfo.curPage);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                if (GlobalData._promovInfo.pagesNum < GlobalData._promovInfo.curPage+1) {
                    refreshLayout.finishLoadmore();
                    refreshLayout.setEnableLoadmore(false);
                }else {
                    GlobalData._promovInfo.curPage++;
                    getPromov(GlobalData._promovInfo.refreshItemNum, GlobalData._promovInfo.curPage);
                }
            }
        });

        refreshPromov.startRefresh();

        return view;
    }

    private void getPromov(int itemNum, int page){

        String[] params = new String[1];
        params[0] = "&count="+String.valueOf(itemNum)+"&page="+String.valueOf(page);
        GetPromovAsync _getPromovAsync = new GetPromovAsync();
        _getPromovAsync.execute(params);
    }

    public class GetPromovAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... param) {

            String result = null;

            try {

                URL url = new URL(Constant.PROMOV_PAGE +param[0]);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(20000); /* milliseconds */
                conn.setConnectTimeout(30000); /* milliseconds */
                conn.setUseCaches(false);
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
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

            refreshPromov.finishRefreshing();
            refreshPromov.finishLoadmore();

            if (result == null){
                Toast.makeText(getActivity(), R.string.error_failed_connect, Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Gson gson = new GsonBuilder().create();
                DataModel promovData = gson.fromJson(result, DataModel.class);

                GlobalData._promovInfo.itemsNum = promovData.category.post_count;
                GlobalData._promovInfo.pagesNum = promovData.pages;

                if(GlobalData._promovInfo.curPage == 1) {
                    GlobalData._promovData.clear();
                }

                Collections.addAll(GlobalData._promovData, promovData.posts);

                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                // TODO: handle exception
                //Log.e("oasis", e.toString());
            }
        }
    }
}
