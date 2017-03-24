package com.mi1.duitku.Tab1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.Common.CommonFunction;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab1.Common.DataModel;
import com.mi1.duitku.Tab1.Common.Tab1Global;
import com.mi1.duitku.Tab3.PaymentActivity;
import com.mi1.duitku.Tab3.PaymentProcessActivity;
import com.mi1.duitku.Tab3.PurchaseActivity;
import com.mi1.duitku.Tab3.PurchaseProcessPLNActivity;
import com.mi1.duitku.Tab3.TopUpActivity;
import com.mi1.duitku.Tab3.TransferActivity;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

/**
 * Created by owner on 3/7/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context context;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private HeaderViewHolder hvHolder;

    public NewsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(context).inflate(R.layout.dashboard, parent, false);
            hvHolder = new HeaderViewHolder (v);
            if (Tab1Global._newsData.size() >= 5) {
                init_slider();
            }
            return hvHolder;
        } else if(viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(context).inflate(R.layout.list_news, parent, false);
            return new GenericViewHolder (v);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.tvBalance.setText(String.format(context.getString(R.string.news_balance), CommonFunction.formatNumberingWithoutRP(AppGlobal._userInfo.userbalance)));
            headerHolder.tvTopUp.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    Intent intent  = new Intent(context, TopUpActivity.class);
                    context.startActivity(intent);
                }
            });
            headerHolder.cardPayment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPrePaidDialog();
                }
            });
            headerHolder.cardBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPostPaidDialog();
                }
            });
            headerHolder.cardTransfer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent  = new Intent(context, TransferActivity.class);
                    context.startActivity(intent);
                }
            });
        } else if(holder instanceof GenericViewHolder) {
            DataModel.Post item = Tab1Global._newsData.get(position-1);
            GenericViewHolder genericViewHolder = (GenericViewHolder) holder;
            genericViewHolder.tvTitle.setText(item.title);
            genericViewHolder.tvPostTime.setText(item.date);
            if(item.thumbnail_images.thumbnail.url.isEmpty()) {
                genericViewHolder.ivThumbnail.setVisibility(View.GONE);
            } else {
                Picasso.with(context).load(item.thumbnail_images.thumbnail.url).into(genericViewHolder.ivThumbnail);
            }

            genericViewHolder.ivThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ContentsActivity.class);
                    intent.putExtra("tab", 1);
                    intent.putExtra("position", position-1);
                    context.startActivity(intent);
                }
            });
        }
    }

    public void init_slider() {

        hvHolder.slider.removeAllSliders();

        for(int i=0; i<5 ; i++) {
            TextSliderView textSliderView = new TextSliderView(context);
//            // initialize a SliderLayout
            textSliderView
                    .image(Tab1Global._newsData.get(i).thumbnail_images.thumbnail.url)
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                            Intent intent = new Intent(context, ContentsActivity.class);
                            intent.putExtra("tab", 1);
                            intent.putExtra("position", hvHolder.slider.getCurrentPosition());
                            context.startActivity(intent);
                        }
                    });

            hvHolder.slider.addSlider(textSliderView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader (position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader (int position) {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        return Tab1Global._newsData.size()+1;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        SliderLayout slider;
        TextView tvBalance;
        TextView tvTopUp;
        CardView cardPayment;
        CardView cardBuy;
        CardView cardTransfer;

        public HeaderViewHolder (View itemView) {
            super (itemView);
            slider = (SliderLayout)itemView.findViewById(R.id.slider);
            slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//            slider.setCustomAnimation(new DescriptionAnimation());
            slider.setDuration(4000);

            tvBalance = (TextView)itemView.findViewById(R.id.txt_balance);
            tvTopUp = (TextView)itemView.findViewById(R.id.txt_topup);
            cardPayment = (CardView) itemView.findViewById(R.id.card_payment);
            cardBuy = (CardView) itemView.findViewById(R.id.card_buy);
            cardTransfer = (CardView) itemView.findViewById(R.id.card_transfer);
        }
    }

    class GenericViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvPostTime;
        ImageView ivThumbnail;

        public GenericViewHolder (View itemView) {
            super (itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.txt_title);
            tvPostTime = (TextView) itemView.findViewById(R.id.txt_time);
            ivThumbnail = (ImageView) itemView.findViewById(R.id.img_thumb);
        }
    }

    private void showPrePaidDialog() {

        new MaterialDialog.Builder(context)
                .items(R.array.prepaid)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if(which == 0) {
                            Intent intent = new Intent(context, PurchaseProcessPLNActivity.class);
                            context.startActivity(intent);
                        } else if(which == 1){
                            Intent intent = new Intent(context, PurchaseActivity.class);
                            context.startActivity(intent);
                        }
                        return true;
                    }
                })
                .positiveText("OK")
                .positiveColorRes(R.color.colorPrimary)
                .negativeText("CANCEL")
                .negativeColorRes(R.color.colorDisable)
                .canceledOnTouchOutside(false)
                .show();
    }

    private void showPostPaidDialog() {

        new MaterialDialog.Builder(context)
                .items(R.array.postpaid)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        Intent intent = null;
                        if(which == 0) {
                            intent = new Intent(context, PaymentProcessActivity.class);
                            intent.putExtra(PaymentProcessActivity.TAG_ACTIVITYTITLE, "PLN Pasca Bayar");
                            intent.putExtra(PaymentProcessActivity.TAG_ACTIVITYPRODUCTCODE, "PLNPASCH");
                            intent.putExtra(PaymentProcessActivity.TAG_ACTIVITYPRODUCTNAME, "PLN PASCA BAYAR");
                            context.startActivity(intent);
                        } else {
                            intent = new Intent(context, PaymentActivity.class);
                            String product_title = context.getResources().getStringArray(R.array.postpaid)[which];
                            intent.putExtra(PaymentActivity.TAG_ACTIVITYTITLE, product_title);
                            context.startActivity(intent);
                        }
                        return true;
                    }
                })
                .positiveText("OK")
                .positiveColorRes(R.color.colorPrimary)
                .negativeText("CANCEL")
                .negativeColorRes(R.color.colorDisable)
                .canceledOnTouchOutside(false)
                .show();
    }

}
