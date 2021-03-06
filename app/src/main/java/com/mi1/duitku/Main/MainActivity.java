package com.mi1.duitku.Main;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.mi1.duitku.BaseActivity;
import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.Common.Constant;
import com.mi1.duitku.LoginActivity;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab1.Common.Tab1Global;
import com.mi1.duitku.Tab1.SearchActivity;
import com.mi1.duitku.Tab2.Common.Contacts;
import com.mi1.duitku.Tab3.Common.CPPOBProduct;
import com.mi1.duitku.Tab3.Common.CPPOBProductParent;
import com.mi1.duitku.Tab3.Common.Tab3Global;
import com.mi1.duitku.Tab5.AboutUsActivity;
import com.mi1.duitku.Tab5.ContactUsActivity;
import com.mi1.duitku.Tab5.HelpActivity;
import com.mi1.duitku.Tab5.Register.RegisterChildActivity;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.BaseService;
import com.quickblox.auth.session.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.messages.services.SubscribeService;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static String TAG = "MainActivity";
    private DrawerLayout dlDrawer;
    private BottomNavigationViewEx bottomTab;
    private int cur_tab = 1;
    private EditText etKeywords;
    public CircleImageView civUserPhoto;
    private ProgressDialog progress;
    public static MainActivity _instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _instance = this;

        createSessionForChat();
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(pushBroadcastReceiver,
                new IntentFilter("new-push-event"));
        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.wait));
        progress.setCanceledOnTouchOutside(false);

        if (Tab1Global._newsData == null | Tab1Global._newsInfo == null) {
            Tab1Global.initData();
        }

        if (Tab3Global._cashInData == null || Tab3Global._cashInInfo == null){
            Tab3Global.initData();
        }

        if (Tab3Global._productPayment == null || Tab3Global._productPurchase == null) {
            new callProductList().execute();
        }

        getContacts();

        bottomTab = (BottomNavigationViewEx) findViewById(R.id.nav_bottom);
        bottomTab.setTextVisibility(false);
        bottomTab.enableShiftingMode(false);
        bottomTab.setIconSize(20, 20);
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

        civUserPhoto = (CircleImageView) findViewById(R.id.civ_user_photo);

        if (!AppGlobal._userInfo.picUrl.isEmpty()) {
            Picasso.with(this).load(AppGlobal._userInfo.picUrl.toLowerCase()).fit().into(civUserPhoto);
        }

        LinearLayout navProfil = (LinearLayout)findViewById(R.id.nav_profile);
        navProfil.setOnClickListener(this);

        LinearLayout navBeranda = (LinearLayout)findViewById(R.id.nav_home);
        navBeranda.setOnClickListener(this);

        LinearLayout navTentang = (LinearLayout)findViewById(R.id.nav_about);
        navTentang.setOnClickListener(this);

        LinearLayout navHubungi = (LinearLayout)findViewById(R.id.nav_contact);
        navHubungi.setOnClickListener(this);

        LinearLayout navBantuan = (LinearLayout)findViewById(R.id.nav_help);
        //navBantuan.setOnClickListener(this);

        LinearLayout navBagikan = (LinearLayout)findViewById(R.id.nav_share);
        navBagikan.setOnClickListener(this);

        LinearLayout navAddUser = (LinearLayout)findViewById(R.id.nav_add_user);
        navAddUser.setOnClickListener(this);

        LinearLayout navLogout = (LinearLayout)findViewById(R.id.nav_logout);
        navLogout.setOnClickListener(this);

        Fragment fragment = null;
        fragment = new Tab5Fragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_fragment, fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        MaterialDialog dialog = new MaterialDialog.Builder(MainActivity.this)
                .title("memastikan")
                .content("Apakah anda yakin ingin keluar dari aplikasi?")
                .positiveText("Ya")
                .negativeText("Tidak")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        _instance.finish();
                    }
                }).build();
        dialog.show();
    }

    private void selectFragment(int index) {

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
//        bottomTab.setCurrentItem(4);
    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void createSessionForChat() {

        final QBUser qbUser = new QBUser(AppGlobal._userInfo.phoneNumber, Constant.QB_ACCOUNT_PASS);
        QBAuth.createSession(qbUser).performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                initNotification();
                qbUser.setId(qbSession.getUserId());
                try {
                    qbUser.setPassword(BaseService.getBaseService().getToken());
                } catch (BaseServiceException e) {
                    e.printStackTrace();
                }

                QBChatService.getInstance().login(qbUser, new QBEntityCallback() {
                    @Override
                    public void onSuccess(Object o, Bundle bundle) {
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.e("error", e.getMessage());
                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("error", e.getMessage());
            }
        });
    }

    private void initNotification() {
        SubscribeService.subscribeToPushes(MainActivity.this, true);
    }

    private BroadcastReceiver pushBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            String from = intent.getStringExtra("from");
            sendNotification(message);
        }
    };

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logodigi1)
                .setContentTitle("Digi1")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void getContacts() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionResult = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS);

            if (permissionResult == PackageManager.PERMISSION_DENIED) {

                /* 사용자가 CALL_PHONE 권한을 한번이라도 거부한 적이 있는 지 조사한다.
                * 거부한 이력이 한번이라도 있다면, true를 리턴한다.
                */
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_CONTACTS)) {

                    MaterialDialog dialog = new MaterialDialog.Builder(MainActivity.this)
                            .title("You need to permission for fetch contacts")
                            .content("")
                            .positiveText("Ya")
                            .negativeText("Tidak")
                            .cancelable(false)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1000);
                                    }
                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Toast.makeText(MainActivity.this, "canceled the permission.", Toast.LENGTH_SHORT).show();
                                }
                            }).build();

                    dialog.show();
                } else { //최초로 권한을 요청할 때
                    // CALL_PHONE 권한을 Android OS 에 요청한다.
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1000);
                }

            } else { /* CALL_PHONE의 권한이 있을 때 */
                Contacts.getIntstace().listContact = getContactList();
            }

        } else {
            Contacts.getIntstace().listContact = getContactList();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Contacts.getIntstace().listContact = getContactList();
            }
            else {
                Toast.makeText(MainActivity.this, "Access denied", Toast.LENGTH_SHORT).show();
            }
        }
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
            etKeywords = (EditText) mCustomView.findViewById(R.id.edt_keywords);
            ImageView ivSearch = (ImageView) mCustomView.findViewById(R.id.img_search);
            ivSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideKeyboard();
                    String keyword = etKeywords.getText().toString();
                    etKeywords.setText("");
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra("curTab", cur_tab);
                    intent.putExtra("keywords", keyword);
                    startActivity(intent);
                }
            });
            actionBar.setCustomView(mCustomView);
        }

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    public class callProductList extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progress.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String result = null;

            try {

                URL url = new URL(Constant.PRODUCT_LIST_PAGE);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(20000); /* milliseconds */
                conn.setConnectTimeout(30000); /* milliseconds */
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();
                    String str;

                    while((str = reader.readLine()) != null){
                        builder.append(str+"\n");
                    }

                    result = builder.toString();
                } else {
                    result = String.valueOf(conn.getResponseCode());
                }

            } catch (MalformedURLException e){
                //Log.e("oasis", e.toString());
            } catch (IOException e) {
                //Log.e("oasis", e.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            progress.dismiss();

            if (result == null){
                Toast.makeText(MainActivity.this, R.string.error_failed_connect, Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONArray objArr = new JSONArray(result);
                for (int i = 0; i < objArr.length(); i++) {
                    JSONObject obj = objArr.getJSONObject(i);
                    if (obj.getString("name").toString().toUpperCase().equals("PPOB PRA BAYAR")) {
                        Tab3Global._productPurchase = ConvertJsontoCCPOBProductParent(obj);
                    }
                    else if (obj.getString("name").toString().toUpperCase().equals("PPOB PASCA BAYAR")) {
                        Tab3Global._productPayment = ConvertJsontoCCPOBProductParent(obj);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private CPPOBProductParent ConvertJsontoCCPOBProductParent(JSONObject obj) throws JSONException {
        CPPOBProductParent product = new CPPOBProductParent();
        product.id = obj.getInt("id");
        product.name = obj.getString("name");
        JSONArray childArr = obj.getJSONArray("child");
        if (childArr.length() > 0) {
            for (int i = 0; i < childArr.length(); i++) {
                CPPOBProductParent childProduct = ConvertJsontoCCPOBProductParent(childArr.getJSONObject(i));
                product.child.add(childProduct);
            }
        }
        JSONArray productList = obj.getJSONArray("productList");
        if (productList.length() > 0) {
            for (int i = 0; i < productList.length(); i++) {
                CPPOBProduct childProduct = new CPPOBProduct();
                JSONObject objChild = productList.getJSONObject(i);
                childProduct.productCode = objChild.getString("productCode");
                childProduct.productName = objChild.getString("productName");
                childProduct.productType = objChild.getString("productType");
                childProduct.productPrice = objChild.getDouble("productPrice");
                product.productList.add(childProduct);
            }
        }
        return product;
    }

    private ArrayList<String> getContactList() {

        String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER };

      //  Cursor contactCursor = this.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
      //          projection, ContactsContract.Contacts.HAS_PHONE_NUMBER+"=1", null, null);
        Cursor contactCursor = this.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                          projection, null, null, null);

        ArrayList<String> contactlist = new ArrayList<String>();

        if (contactCursor.moveToFirst()) {
            do {
                //String phonenumber = contactCursor.getString(0).replaceAll("-", "");
                String phonenumber = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                //remove spacing
                phonenumber = phonenumber.replaceAll("\\s+","");
                // change +62 for indonesian code number to 0
                if (phonenumber.contains("+62")){
                    phonenumber = phonenumber.replace("+62", "0");
                }
                contactlist.add(phonenumber);
            } while (contactCursor.moveToNext());
        }

        return contactlist;
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
                logout();
                break;
            case R.id.nav_add_user:
                dlDrawer.closeDrawers();
                intent = new Intent(MainActivity.this, RegisterChildActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void logout() {
        AppGlobal._userInfo = null;
        AppGlobal._userDetailInfo = null;
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        QBUsers.signOut().performAsync(new QBEntityCallback<Void>(){
            @Override
            public void onSuccess(Void result, Bundle bundle) {
                Log.i(TAG, ">>> User was successfully signed out");
            }

            @Override
            public void onError(QBResponseException errors)
            {
//                handleErrors(errors);
            }
        });
        MainActivity._instance.finish();
    }
}
