package com.mi1.duitku.Tab3;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mi1.duitku.R;

import java.util.ArrayList;

/**
 * Created by owner on 3/7/2017.
 */

public class PGAdapter extends RecyclerView.Adapter<PGAdapter.ViewHolder> {

    Context context;
    ArrayList<PaymentMethod> bankInfos;
    int selPos = 0;

    public PGAdapter(Context context, ArrayList<PaymentMethod> bankInfos) {
        this.context = context;
        this.bankInfos = bankInfos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.card_bank, parent, false);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        BankInfo item = bankInfos.get(position);
//        holder.bankName.setText(item.name);
//        holder.markUrl.setBackgroundResource(item.mark);
//        if (selPos == position) {
//            holder.cardView.setCardBackgroundColor(0x80FFFFFF);
//        } else {
//            holder.cardView.setCardBackgroundColor(Color.WHITE);
//        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(selPos);
                selPos = position;
                notifyItemChanged(selPos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bankInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView bankName;
        ImageView markUrl;
        CardView cardView;

        public ViewHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.card_bank);
            bankName = (TextView) v.findViewById(R.id.txt_bank_name);
            markUrl = (ImageView) v.findViewById(R.id.img_bank);
        }
    }
}
