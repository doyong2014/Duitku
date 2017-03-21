package com.mi1.duitku.Tab3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.Common.CommonFunction;
import com.mi1.duitku.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DompetFragment extends Fragment {

    private Context context;

    public DompetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_dompet, container, false);

        context = getContext();

        TextView tvBalance = (TextView) view.findViewById(R.id.txt_balance);
        tvBalance.setText(CommonFunction.formatNumberingWithoutRP(AppGlobal._userInfo.userbalance));

        TextView tvTopup = (TextView) view.findViewById(R.id.txt_topup);
        tvTopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(context, TopUpActivity.class);
                context.startActivity(intent);
            }
        });

        TextView tvTopupBank = (TextView) view.findViewById(R.id.txt_topup_bank);
        tvTopupBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(context, BankTransferActivity.class);
                context.startActivity(intent);
            }
        });

        TextView tvPrePayment = (TextView) view.findViewById(R.id.txt_pre_payment);
        tvPrePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPrePaidDialog();
            }
        });

        TextView tvPostPayment = (TextView) view.findViewById(R.id.txt_post_payment);
        tvPostPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPostPaidDialog();
            }
        });

        TextView tvTransfer = (TextView) view.findViewById(R.id.txt_transfer);
        tvTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(context, TransferActivity.class);
                context.startActivity(intent);
            }
        });

        CardView cardPayment = (CardView) view.findViewById(R.id.card_payment);
        cardPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        CardView cardBuy = (CardView) view.findViewById(R.id.card_buy);
        cardBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        CardView cardTransfer = (CardView) view.findViewById(R.id.card_transfer);
        cardTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(context, TransferActivity.class);
                context.startActivity(intent);
            }
        });

        return view;
    }

    private void showPrePaidDialog() {

        new MaterialDialog.Builder(getContext())
                .items(R.array.prepaid)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if(which == 0) {
                            Intent intent = new Intent(context, PurchaseProcessPLNActivity.class);
                            startActivity(intent);
                        } else if(which == 1){
                            Intent intent = new Intent(context, PurchaseActivity.class);
                            startActivity(intent);
                        }
                        return true;
                    }
                })
                .positiveText("OK")
                .positiveColorRes(R.color.colorPrimary)
                .negativeText("CANCEL")
                .negativeColorRes(R.color.colorDisable)
                .show();
    }

    private void showPostPaidDialog() {

        new MaterialDialog.Builder(getContext())
                .items(R.array.postpaid)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        Intent intent = null;
                        if(which == 0) {
                            intent = new Intent(getContext(), PaymentProcessActivity.class);
                            intent.putExtra(PaymentProcessActivity.TAG_ACTIVITYTITLE, "PLN Pasca Bayar");
                            intent.putExtra(PaymentProcessActivity.TAG_ACTIVITYPRODUCTCODE, "PLNPASCH");
                            intent.putExtra(PaymentProcessActivity.TAG_ACTIVITYPRODUCTNAME, "PLN PASCA BAYAR");
                            startActivity(intent);
                        } else {
                            intent = new Intent(getContext(), PaymentActivity.class);
                            String product_title = getResources().getStringArray(R.array.postpaid)[which];
                            intent.putExtra(PaymentActivity.TAG_ACTIVITYTITLE, product_title);
                            startActivity(intent);
                        }
                        return true;
                    }
                })
                .positiveText("OK")
                .positiveColorRes(R.color.colorPrimary)
                .negativeText("CANCEL")
                .negativeColorRes(R.color.colorDisable)
                .show();
    }

}
