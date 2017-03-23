package com.mi1.duitku.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.LoginActivity;
import com.mi1.duitku.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout dlDrawer;
    private BottomNavigationViewEx bottomTab;
    private int cur_tab = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomTab = (BottomNavigationViewEx) findViewById(R.id.nav_bottom);
        bottomTab.setTextVisibility(false);
        bottomTab.enableShiftingMode(false);
//        bottomTab.enableAnimation(true);
        bottomTab.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                        case R.id.action_news:
                        selectFragment(1);
                        break;
                    case R.id.action_msg:
                        selectFragment(2);
                        break;
                    case R.id.action_pay:
                        selectFragment(3);
                        break;
                    case R.id.action_buy:
                        selectFragment(4);
                        break;
                    case R.id.action_profile:
                        selectFragment(5);
                        break;
                }
                return true;
            }
        });

        selectFragment(cur_tab);

        dlDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        LinearLayout navProfil = (LinearLayout)findViewById(R.id.nav_profile);
        navProfil.setOnClickListener(this);

        LinearLayout navBeranda = (LinearLayout)findViewById(R.id.nav_home);
        navBeranda.setOnClickListener(this);

        LinearLayout navTentang = (LinearLayout)findViewById(R.id.nav_about);
        navTentang.setOnClickListener(this);

        LinearLayout navHubungi = (LinearLayout)findViewById(R.id.nav_contact);
        navHubungi.setOnClickListener(this);

        LinearLayout navBantuan = (LinearLayout)findViewById(R.id.nav_help);
        navBantuan.setOnClickListener(this);

        LinearLayout navBagikan = (LinearLayout)findViewById(R.id.nav_share);
        navBagikan.setOnClickListener(this);

        LinearLayout navLogout = (LinearLayout)findViewById(R.id.nav_logout);
        navLogout.setOnClickListener(this);

    }

    private void selectFragment(int index){

        Fragment fragment = null;
        cur_tab = index;
        switch (index) {
            case 1:
                fragment = new Tab1Fragment();
                break;
            case 2:
                fragment = new Tab2Fragment();
                break;
            case 3:
                fragment = new Tab3Fragment();
                break;
            case 4:
                fragment = new Tab4Fragment();
                break;
            case 5:
                fragment = new Tab5Fragment();
                break;
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_fragment, fragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        ActionBar actionBar = getSupportActionBar();

        if(cur_tab == 5){
            actionBar.setTitle("Profile");
        } else {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            View mCustomView = LayoutInflater.from(this).inflate(R.layout.actionbar_search, null);
            actionBar.setCustomView(mCustomView);
        }

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_menu) {
            if (dlDrawer.isDrawerOpen(Gravity.RIGHT)) {
                dlDrawer.closeDrawer(Gravity.RIGHT);
            } else {
                dlDrawer.openDrawer(Gravity.RIGHT);
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {

        Intent intent = null;
        Fragment fragment = null;

        switch (v.getId()) {

            case R.id.nav_profile:
                dlDrawer.closeDrawers();
                if (cur_tab != 5) {
                    bottomTab.setCurrentItem(4);
                }
                break;
            case R.id.nav_home:
                dlDrawer.closeDrawers();
                if (cur_tab != 1) {
                    bottomTab.setCurrentItem(0);
                }
                break;
            case R.id.nav_about:
                dlDrawer.closeDrawers();
                intent = new Intent(MainActivity.this, AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_contact:
                dlDrawer.closeDrawers();
                intent = new Intent(MainActivity.this, ContactUsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_help:
                dlDrawer.closeDrawers();
                intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_share:
                dlDrawer.closeDrawers();
                break;
            case R.id.nav_logout:
                dlDrawer.closeDrawers();
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
                AppGlobal._userInfo.clear();
                break;
        }
    }
}
