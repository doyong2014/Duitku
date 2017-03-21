package com.mi1.duitku.Tab3;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mi1.duitku.R;
import com.mi1.duitku.Tab3.Common.CashInfo;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CashOutFragment extends Fragment {

    private LinearLayoutManager layoutManager;
    private RecyclerView recycler;
    private CashAdapter adapter;
    private ArrayList<CashInfo> cashInfos = new ArrayList<>();

    public CashOutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_cashin, container, false);

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler = (RecyclerView) view.findViewById(R.id.recycler_cash_in);
        recycler.setLayoutManager(layoutManager);
        recycler.addItemDecoration(new DividerItemDecoration(getContext()));

        init();

        adapter = new CashAdapter(getActivity(), cashInfos);
        recycler.setAdapter(adapter);

        return view;
    }

    private void init() {
        cashInfos.add(new CashInfo("1.000.000", "09/23/2016"));
        cashInfos.add(new CashInfo("250.000", "09/23/2016"));
        cashInfos.add(new CashInfo("1.000.000", "09/23/2016"));
        cashInfos.add(new CashInfo("1.000.000", "09/23/2016"));
        cashInfos.add(new CashInfo("1.000.000", "09/23/2016"));
    }

}