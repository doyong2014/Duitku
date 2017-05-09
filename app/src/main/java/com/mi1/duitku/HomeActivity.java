package com.mi1.duitku;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.Common.Constant;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.StoringMechanism;

public class HomeActivity extends BaseActivity {

    public static HomeActivity _instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.initQBFramework();

        setContentView(R.layout.activity_home);

        _instance = this;

        if (AppGlobal._userInfo == null){
            AppGlobal.initData();
        }

        Button btnLogin = (Button)findViewById(R.id.btn_home_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        Button btnSign = (Button)findViewById(R.id.btn_home_sign);
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initQBFramework() {
        QBSettings.getInstance().setStoringMehanism(StoringMechanism.UNSECURED);
        QBSettings.getInstance().init(getApplicationContext(), Constant.QB_APP_ID, Constant.QB_AUTH_KEY, Constant.QB_AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(Constant.QB_ACCOUNT_KEY);
        QBSettings.getInstance().setEnablePushNotification(true);
    }
}
