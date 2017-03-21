package com.mi1.duitku.Main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab5.ShareCodeActivity;
import com.pkmmte.view.CircularImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab5Fragment extends Fragment {


    public Tab5Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_tab5, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap blurTemplate = BitmapFactory.decodeResource(getResources(), R.drawable.house, options);
        ImageView ivBlurPhoto = (ImageView)view.findViewById(R.id.img_full);
        ivBlurPhoto.setImageBitmap(blurTemplate);

        CircularImageView civUserPhoto = (CircularImageView)view.findViewById(R.id.civ_user_photo);
        civUserPhoto.setImageResource(R.drawable.house);

        TextView tvName = (TextView)view.findViewById(R.id.txt_name);
        tvName.setText(AppGlobal._userInfo.name);

        TextView tvPhone = (TextView)view.findViewById(R.id.txt_phone);
        tvPhone.setText(AppGlobal._userInfo.phoneNumber);

        TextView tvEmail = (TextView)view.findViewById(R.id.txt_email);
        tvEmail.setText(AppGlobal._userInfo.email);

        TextView tvInfo = (TextView)view.findViewById(R.id.txt_info);
        tvInfo.setText(AppGlobal._userInfo.phoneNumber);

        CardView cvShareCode = (CardView)view.findViewById(R.id.card_share_code);
        cvShareCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShareCodeActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
