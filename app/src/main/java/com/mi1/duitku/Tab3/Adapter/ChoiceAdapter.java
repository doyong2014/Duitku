package com.mi1.duitku.Tab3.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mi1.duitku.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ibrahimovic on 26/05/2017.
 */

public class ChoiceAdapter extends BaseAdapter
{
    Context context;
    String[] packageName;
    LayoutInflater inflater;

    public ChoiceAdapter(Context context, String[] packageName) {
        this.context = context;
        this.packageName = packageName;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return packageName.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.list_package_spinner, null);
        TextView names = (TextView) view.findViewById(R.id.txtPackage);
        names.setText(packageName[i]);
        return view;
    }
}
