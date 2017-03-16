package com.mi1.duitku.Main;

import android.content.Intent;
import android.os.Bundle;
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

import com.brioal.bottomtab.entity.TabEntity;
import com.brioal.bottomtab.interfaces.OnTabSelectedListener;
import com.brioal.bottomtab.view.BottomLayout;
import com.mi1.duitku.LoginActivity;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab1.GlobalData;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout dlDrawer;
    private BottomLayout mBottomTap;
    private List<TabEntity> mList;
    public static int cur_tab = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomTap = (BottomLayout) findViewById(R.id.main_tab);
        initBottonLayout();

        selectFragment(1);

        dlDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        LinearLayout navProfil = (LinearLayout)findViewById(R.id.nav_profil);
        navProfil.setOnClickListener(this);

        LinearLayout navBeranda = (LinearLayout)findViewById(R.id.nav_beranda);
        navBeranda.setOnClickListener(this);

        LinearLayout navTentang = (LinearLayout)findViewById(R.id.nav_tentang);
        navTentang.setOnClickListener(this);

        LinearLayout navHubungi = (LinearLayout)findViewById(R.id.nav_hubungi);
        navHubungi.setOnClickListener(this);

        LinearLayout navBantuan = (LinearLayout)findViewById(R.id.nav_bantuan);
        navBantuan.setOnClickListener(this);

        LinearLayout navBagikan = (LinearLayout)findViewById(R.id.nav_bagikan);
        navBagikan.setOnClickListener(this);

        LinearLayout navLogout = (LinearLayout)findViewById(R.id.nav_logout);
        navLogout.setOnClickListener(this);

        GlobalData _globalData = new GlobalData();
    }

    private void initBottonLayout() {

        mList = new ArrayList<>();
        mList.add(new TabEntity(R.drawable.ic_icon, "tab1"));
        mList.add(new TabEntity(R.drawable.ic_icon, "tab2"));
        mList.add(new TabEntity(R.drawable.ic_icon, "tab3"));
        mList.add(new TabEntity(R.drawable.ic_icon, "tab4"));
        mList.add(new TabEntity(R.drawable.ic_icon, "tab5"));

        mBottomTap.setList(mList);
        mBottomTap.setSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onSelected(int position) {
                cur_tab = position+1;
                selectFragment(cur_tab);
            }
        });
    }

    private void selectFragment(int index){

        Fragment fragment = null;

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
        switch (v.getId()) {

            case R.id.nav_profil:
                dlDrawer.closeDrawers();
                break;
            case R.id.nav_beranda:
                dlDrawer.closeDrawers();
                break;
            case R.id.nav_tentang:
                dlDrawer.closeDrawers();
                break;
            case R.id.nav_hubungi:
                dlDrawer.closeDrawers();
                break;
            case R.id.nav_bantuan:
                dlDrawer.closeDrawers();
                break;
            case R.id.nav_bagikan:
                dlDrawer.closeDrawers();
                break;
            case R.id.nav_logout:
                dlDrawer.closeDrawers();
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
                break;
        }
    }
}
