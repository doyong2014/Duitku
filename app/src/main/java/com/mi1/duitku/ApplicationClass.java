package com.mi1.duitku;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

/**
 * Created by owner on 4/17/2017.
 */

public class ApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/Lato-Regular.ttf"))
                .addBold(Typekit.createFromAsset(this, "fonts/Lato-Bold.ttf"));
    }
}
