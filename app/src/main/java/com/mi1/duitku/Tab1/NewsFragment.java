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
import com.mi1.duitku.R;
import com.mi1.duitku.Tab1.Common.DataModel;
import com.mi1.duitku.Tab1.Common.FooterView;
import com.mi1.duitku.Tab1.Common.HeaderView;
import com.mi1.duitku.Tab1.Common.Tab1Global;

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
public class NewsFragment extends Fragment{

    private LinearLayoutManager layoutManager;
    private TwinklingRefreshLayout refresh;
    private RecyclerView recycler;
    private NewsAdapter adapter;

    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        refresh = (TwinklingRefreshLayout) view.findViewById(R.id.refreshLayout);
        HeaderView headerView = (HeaderView) View.inflate(getActivity(), R.layout.header_refresh, null);
        refresh.setHeaderView(headerView);

        FooterView footerView = (FooterView) View.inflate(getActivity(), R.layout.footer_loadmore, null);
        refresh.setBottomView(footerView);

        LinearLayout upArrow = (LinearLayout) view.findViewById(R.id.ll_top);
        upArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycler.smoothScrollToPosition(0);
            }
        });

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler = (RecyclerView) view.findViewById(R.id.recycler_news);
        recycler.setLayoutManager(layoutManager);

        adapter = new NewsAdapter(getActivity());
        recycler.setAdapter(adapter);

        refresh.setOnRefreshListener(new RefreshListenerAdapter() {

            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                Tab1Global._newsInfo.curPage = 1;
                getNews(Tab1Global._newsInfo.refreshItemNum, Tab1Global._newsInfo.curPage);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                if (Tab1Global._newsInfo.pagesNum < Tab1Global._newsInfo.curPage+1) {
                    refreshLayout.finishLoadmore();
                    Toast.makeText(getContext(), "No more contents", Toast.LENGTH_SHORT).show();
                }else {
                    Tab1Global._newsInfo.curPage++;
                    getNews(Tab1Global._newsInfo.refreshItemNum, Tab1Global._newsInfo.curPage);
                }
            }
        });

        refresh.startRefresh();

        return view;
    }

    private void getNews(int itemNum, int page){

        String[] params = new String[1];
        params[0] = "&count="+String.valueOf(itemNum)+"&page="+String.valueOf(page);
        GetNewsAsync _getNewsAsync = new GetNewsAsync();
        _getNewsAsync.execute(params);
    }

    public class GetNewsAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... param) {

            String result = null;

            try {

                URL url = new URL(Constant.NEWS_PAGE +param[0]);

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

            refresh.finishRefreshing();
            refresh.finishLoadmore();

            if (result == null){
                Toast.makeText(getActivity(), R.string.error_failed_connect, Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Gson gson = new GsonBuilder().create();
                DataModel newsData = gson.fromJson(result, DataModel.class);

                Tab1Global._newsInfo.itemsNum = newsData.category.post_count;
                Tab1Global._newsInfo.pagesNum = newsData.pages;

                if(Tab1Global._newsInfo.curPage == 1) {
                    Tab1Global._newsData.clear();
                }

                Collections.addAll(Tab1Global._newsData, newsData.posts);

                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                // TODO: handle exception
                //Log.e("oasis", e.toString());
            }
        }
    }
}
