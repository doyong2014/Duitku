package com.android.duitku.paymentmethod.view;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.duitku.R;

/**
 * Created by latif on 12/3/15.
 */
public class PaymentMethodAdapter extends BaseAdapter {

    public Context mContext;

    public String[] mImgBank;

    public String[] mTxtBankName;


    public PaymentMethodAdapter(Context context,String[] imgBank,String[] txtBankName){
        this.mContext = context;
        this.mImgBank = imgBank;
        this.mTxtBankName = txtBankName;
    }
    @Override
    public int getCount() {
        return mImgBank.length;
    }

    @Override
    public Object getItem(int i) {
        return mTxtBankName[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(R.layout.content_payment_method_new,null);
        TextView txtBankName = (TextView)view.findViewById(R.id.txt_bank_name);
        ImageView imgLogoBank = (ImageView)view.findViewById(R.id.img_logo_bank);

        Resources resources = mContext.getResources();
        int resImgBank = resources.getIdentifier(mImgBank[i],"drawable",mContext.getPackageName());

        imgLogoBank.setImageResource(resImgBank);
        txtBankName.setText(mTxtBankName[i].toString());

        return view;
    }
}
