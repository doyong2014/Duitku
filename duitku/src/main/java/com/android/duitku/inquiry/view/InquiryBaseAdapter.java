package com.android.duitku.inquiry.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.duitku.R;

/**
 * Created by latif on 11/15/15.
 */
public class InquiryBaseAdapter extends BaseAdapter {

    private Context mContext;
    private String[] mNamaBarang,mHargaBarang;


    public InquiryBaseAdapter(Context context, String[] namaBarang, String[] hargaBarang) {
        this.mContext = context;
        this.mNamaBarang = namaBarang;
        this.mHargaBarang = hargaBarang;
    }

    @Override
    public int getCount() {
        return mNamaBarang.length;
    }

    @Override
    public Object getItem(int i) {

        return mHargaBarang[i];

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(R.layout.content_spinner,null);
        TextView textNamaBarang = (TextView)view.findViewById(R.id.text_nama_barang);
        textNamaBarang.setText(mNamaBarang[position].toString());

        return view;
    }
}
