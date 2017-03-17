package com.mi1.duitku.Tab1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.mi1.duitku.Common.UserInfo;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab1.Common.DataModel;
import com.mi1.duitku.Tab1.Common.GlobalData;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

/**
 * Created by owner on 3/7/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements BaseSliderView.OnSliderClickListener {

    Context context;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public NewsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(context).inflate(R.layout.dashboard, parent, false);
            HeaderViewHolder view = new HeaderViewHolder (v);
            init_slider(view);
            return view;
        } else if(viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(context).inflate(R.layout.card_news, parent, false);
            return new GenericViewHolder (v);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.txtBalance.setText(String.format(context.getString(R.string.news_balance), UserInfo.mUserBalance));
            headerHolder.txtTopUp.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    Toast.makeText (context, "Clicked topup", Toast.LENGTH_SHORT).show ();
                }
            });
        } else if(holder instanceof GenericViewHolder) {
            DataModel.Post item = GlobalData._newsData.get(position-1);
            GenericViewHolder genericViewHolder = (GenericViewHolder) holder;
            genericViewHolder.txtTitle.setText(item.title);
            genericViewHolder.txtPostTime.setText(item.date);
            if(item.thumbnail_images.thumbnail.url.isEmpty()) {
                genericViewHolder.imgThumbnail.setVisibility(View.GONE);
            } else {
                Picasso.with(context).load(item.thumbnail_images.thumbnail.url).into(genericViewHolder.imgThumbnail);
            }

            genericViewHolder.imgThumbnail.setOnClickListener(new View.OnClickListener() {
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

    private void init_slider(HeaderViewHolder headerHolder) {

        HashMap<String,String> url_maps = new HashMap<String, String>();
//        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
//        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
//        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
//        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal",R.drawable.hannibal);
        file_maps.put("Big Bang Theory",R.drawable.bigbang);
        file_maps.put("House of Cards",R.drawable.house);
        file_maps.put("Game of Thrones", R.drawable.game_of_thrones);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(context);
//            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
//            textSliderView.bundle(new Bundle());
//            textSliderView.getBundle().putString("extra",name);

            headerHolder.slider.addSlider(textSliderView);
        }
//        imageSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        headerHolder.slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        headerHolder.slider.setCustomAnimation(new DescriptionAnimation());
        headerHolder.slider.setDuration(5000);
//        headerHolder.slider.addOnPageChangeListener(this);
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
        return GlobalData._newsData.size()+1;
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(context, "slide click", Toast.LENGTH_SHORT).show();
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        SliderLayout slider;
        TextView txtBalance;
        TextView txtTopUp;

        public HeaderViewHolder (View itemView) {
            super (itemView);
            slider = (SliderLayout)itemView.findViewById(R.id.slider);
            txtBalance = (TextView)itemView.findViewById(R.id.txt_balance);
            txtTopUp = (TextView)itemView.findViewById(R.id.txt_topup);
        }
    }

    class GenericViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView txtPostTime;
        ImageView imgThumbnail;

        public GenericViewHolder (View itemView) {
            super (itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
            txtPostTime = (TextView) itemView.findViewById(R.id.txt_time);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumb);
        }
    }

}
