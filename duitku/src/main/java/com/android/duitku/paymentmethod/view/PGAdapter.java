package com.android.duitku.paymentmethod.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.duitku.R;
import com.android.duitku.checkoutpage.view.CheckoutActivity;
import com.android.duitku.model.PaymentMethod;

import org.w3c.dom.Text;

/**
 * Created by Ardian on 15/02/2016.
 */
public class PGAdapter extends RecyclerView.Adapter<PGAdapter.PGViewHolder> {

    public Context mContext;

    public String[] mImgBank;

    public String[] mTxtBankName;

    public PaymentMethod[] mPaymentMethods;

    public int mWidth;

    public int nowSelected;

    public boolean[] pgPositionArray;

    private PGAdapter mPGAdapter = this;

    public PGAdapter(Context context,String[] imgBank,String[] txtBankName, PaymentMethod[] paymentMethods, int width)
    {
        this.mContext = context;
        this.mImgBank = imgBank;
        this.mTxtBankName = txtBankName;
        this.mPaymentMethods = paymentMethods;
        this.mWidth = width;
        pgPositionArray = new boolean[mPaymentMethods.length];

        for(int i = 0;i<mPaymentMethods.length;i++)
        {
            pgPositionArray[i] = false;
        }
    }

    public PGViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_payment_method_new, parent, false);

        PGViewHolder viewHolder = new PGViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PGViewHolder holder, int position) {
        holder.bindPG(position);
    }

    @Override
    public int getItemCount() {
        return mPaymentMethods.length;
    }

    public class PGViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView mBankName;
        public ImageView mImageBank;
        public CardView mSelView;
        public RecyclerView mRecyclerView;
        public TextView mAdminFeeLabel;
        public TextView mOngkirLabel;
        public TextView mPriceLabel;
        public TextView mTotalPriceLabel;
        public TextView mPGName;
        public int adminFee;

        public PGViewHolder(View itemView) {
            super(itemView);

            mBankName = (TextView) itemView.findViewById(R.id.txt_bank_name);
            mImageBank = (ImageView)itemView.findViewById(R.id.img_logo_bank);
            mSelView = (CardView) itemView.findViewById(R.id.card_bank);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.rvBank);

            View rootView = ((Activity)mContext).getWindow().getDecorView().findViewById(android.R.id.content);

            mAdminFeeLabel = (TextView) rootView.findViewById(R.id.administrationValue);
            mOngkirLabel = (TextView) rootView.findViewById(R.id.deliveryValue);
            mPriceLabel = (TextView) rootView.findViewById(R.id.priceValue);
            mTotalPriceLabel = (TextView) rootView.findViewById(R.id.totalValue);
            mPGName = (TextView) rootView.findViewById(R.id.pg_name);

            itemView.setOnClickListener(this);
        }

        public void bindPG(int position)
        {
            Resources resources = mContext.getResources();
            int resImgBank = resources.getIdentifier(mPaymentMethods[position].getCode().toLowerCase(),"drawable",mContext.getPackageName());
            try
            {
                mImageBank.setImageDrawable(mContext.getResources().getDrawable(resImgBank));
            }
            catch (Exception ex)
            {
                mImageBank.setImageDrawable(mContext.getResources().getDrawable(R.drawable.duitku));
            }
            mBankName.setText(mPaymentMethods[position].getName().replace("FASPAY ", "").replace("CODAPAY-", "").toString());
            mBankName.setTextSize(13);

            //region setPGTextSize
            if(mWidth <= 1280) {
                if (mPaymentMethods[position].getName().replace("FASPAY ", "").replace("CODAPAY-", "").toString().length() >= 20) {
                    mBankName.setTextSize(10);
                }
                if (mPaymentMethods[position].getName().replace("FASPAY ", "").replace("CODAPAY-", "").toString().length() >= 23) {
                    mBankName.setTextSize(9);
                }
            }
            if(mWidth <= 480) {
                if (mPaymentMethods[position].getName().replace("FASPAY ", "").replace("CODAPAY-", "").toString().length() >= 15) {
                    mBankName.setTextSize(10);
                }
                if (mPaymentMethods[position].getName().replace("FASPAY ", "").replace("CODAPAY-", "").toString().length() >= 20) {
                    mBankName.setTextSize(9);
                }
            }
            //endregion

            View rootView = ((Activity)mContext).getWindow().getDecorView().findViewById(android.R.id.content);
            mRecyclerView = (RecyclerView)rootView.findViewById(R.id.rvBank);

            if(pgPositionArray[position])
            {
                mSelView.setSelected(true);
            }
            else
            {
                mSelView.setSelected(false);
            }
        }

        private void updateTransactionInfo(String bankName, String adminFeeInfo, String code)
        {
            mPGName.setText(bankName);
            mAdminFeeLabel.setText(adminFeeInfo);
        }
        @Override
        public void onClick(View v)
        {
            if (pgPositionArray[nowSelected]) {
                pgPositionArray[nowSelected] = false;
                mPGAdapter.notifyItemChanged(nowSelected);
            }
            if(!pgPositionArray[getAdapterPosition()]){
                pgPositionArray[getAdapterPosition()] = true;
                mPGAdapter.notifyItemChanged(getAdapterPosition());
                nowSelected = getAdapterPosition();
            }

            for(int i = 0;i < mPaymentMethods.length; i++)
            {
                if (mBankName.getText().toString().equals(mPaymentMethods[i].getName().replace("FASPAY ","").replace("CODAPAY-","").toString())) {
                    String adminFeeInfo = mPaymentMethods[i].getFee().toString();
                    String code = mPaymentMethods[i].getCode().toString();
                    adminFee = 1000;
                    updateTransactionInfo(mBankName.getText().toString(), adminFeeInfo, code);//duitku
                }
            }
        }
    }

}
