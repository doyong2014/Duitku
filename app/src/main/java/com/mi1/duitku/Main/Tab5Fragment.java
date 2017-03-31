package com.mi1.duitku.Main;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.Common.CommonFunction;
import com.mi1.duitku.Common.Constant;
import com.mi1.duitku.Common.UserDetailInfo;
import com.mi1.duitku.Common.UserInfo;
import com.mi1.duitku.LoginActivity;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab5.BlurTransformation;
import com.mi1.duitku.Tab5.ChangePasswordActivity;
import com.mi1.duitku.Tab5.ShareCodeActivity;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab5Fragment extends Fragment {

    private ProgressDialog progress;
    private Context _context;
    private TextView tvEmail;
    private TextView tvPhone;
    private TextView tvFullName;
    private TextView tvBirthday;
    private ImageView ivBlurPhoto;
    private CircleImageView civUserPhoto;
    private String fullName="", birthday="", email="", phone="";
    private Bitmap bmUserPhoto;
    final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

    public Tab5Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_tab5, container, false);
        _context = getContext();

        progress = new ProgressDialog(_context);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        if (!AppGlobal._userDetailInfo.isSync)
            getProfile();

        if (AppGlobal._userDetailInfo.birthday != null) {
            int idx = AppGlobal._userDetailInfo.birthday.indexOf(" ");
            if (idx != -1) {
                birthday = AppGlobal._userDetailInfo.birthday.substring(0, idx);
            } else {
                birthday = AppGlobal._userDetailInfo.birthday;
            }
        }

        fullName = AppGlobal._userInfo.fullName;
        email = AppGlobal._userInfo.email;
        phone = AppGlobal._userInfo.phoneNumber;

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        ivBlurPhoto = (ImageView) view.findViewById(R.id.img_full);
        civUserPhoto = (CircleImageView) view.findViewById(R.id.civ_user_photo);

        if (!AppGlobal._userInfo.picUrl.isEmpty()) {
            Picasso.with(_context).load(AppGlobal._userInfo.picUrl.toLowerCase()).fit().transform(new BlurTransformation(_context)).into(ivBlurPhoto);
            Picasso.with(_context).load(AppGlobal._userInfo.picUrl.toLowerCase()).fit().into(civUserPhoto);
        }

        civUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkpermission())
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA);
                else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 105);
                }
            }
        });

        if (!checkpermission())
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA);

        tvFullName = (TextView)view.findViewById(R.id.txt_full_name);
        tvFullName.setText(fullName);
        LinearLayout layoutUserName = (LinearLayout)view.findViewById(R.id.ll_username);
        layoutUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFullName();
            }
        });

        tvBirthday = (TextView)view.findViewById(R.id.txt_birthday);
        tvBirthday.setText(birthday);
        LinearLayout layoutBirthday = (LinearLayout)view.findViewById(R.id.ll_birthday);
        layoutBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBirthday(savedInstanceState);
            }
        });

        tvEmail = (TextView)view.findViewById(R.id.txt_email);
        tvEmail.setText(email);
        LinearLayout layoutEmail = (LinearLayout)view.findViewById(R.id.ll_email);
        layoutEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEmailAddr();
            }
        });

        TextView tvPassword = (TextView)view.findViewById(R.id.txt_password);
        tvPassword.setText(AppGlobal._userInfo.password);
        LinearLayout layoutPassword = (LinearLayout)view.findViewById(R.id.ll_password);
        layoutPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_context, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        tvPhone = (TextView)view.findViewById(R.id.txt_phone);
        tvPhone.setText(phone);
        LinearLayout layoutPhone = (LinearLayout)view.findViewById(R.id.ll_phone);
        layoutPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPhoneNumber();
            }
        });

        CardView cvShareCode = (CardView)view.findViewById(R.id.card_share_code);
        cvShareCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_context, ShareCodeActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //openCamera();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
//                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA);
                    //finish();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private boolean checkpermission() {
        int targetSdkVersion = 0;
        try {
            final PackageInfo info = getActivity().getApplicationContext().getPackageManager().getPackageInfo(
                    getActivity().getApplicationContext().getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // For Android < Android M, self permissions are always granted.
        boolean result = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                result = getActivity().getApplicationContext().checkSelfPermission(Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                result = PermissionChecker.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CAMERA)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        }
        return result;
    }

    private void getProfile() {

        String[] params = new String[1];
        params[0] = AppGlobal._userInfo.token;
        GetProfileAsync _getProfileAsync = new GetProfileAsync();
        _getProfileAsync.execute(params);
    }

    private void dispUserDetailInfo() {
        birthday = CommonFunction.getFormatedDate(AppGlobal._userDetailInfo.birthday);
        tvBirthday.setText(birthday);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 105 && resultCode == RESULT_OK) {

            String mImgURI = CommonFunction.getFilePathFromUri(_context, data.getData());
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 4;
            bmUserPhoto = BitmapFactory.decodeFile(mImgURI);

            String[] params = new String[2];
            params[0] = AppGlobal._userInfo.token;
            params[1] = Constant.PICTURE_TYPE.PROFILE;
            UploadImageAsync _uploadImageAsync = new UploadImageAsync();
            _uploadImageAsync.execute(params);
        }
    }

    public class GetProfileAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            progress.setMessage(getString(R.string.wait));
            progress.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... param) {

            String result = null;

            try {

                URL url = new URL(Constant.GET_PROFILE_PAGE + param[0]);

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

                }  else {
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
                Toast.makeText(getActivity(), R.string.error_failed_connect, Toast.LENGTH_SHORT).show();
                return;
            } else if(result.equals("401")) {
                Toast.makeText(_context, "Sesi anda telah habis", Toast.LENGTH_SHORT).show();
                logout();
                return;
            }

            try {
                Gson gson = new GsonBuilder().create();
                AppGlobal._userDetailInfo = gson.fromJson(result, UserDetailInfo.class);
                AppGlobal._userDetailInfo.isSync = true;
                dispUserDetailInfo();
            } catch (Exception e) {
                // TODO: handle exception
                //Toast.makeText(_context, "session expired.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateProfile(){

        String[] params = new String[9];
        params[0] = AppGlobal._userInfo.token;
        params[1] = fullName;
        params[2] = email;
        params[3] = phone;
        params[4] = birthday;
        params[5] = ""; //gender
        params[6] = ""; //adress
        params[7] = ""; //addrdetail
        params[8] = AppGlobal._userDetailInfo.zipcode;
        UpdateProfileAsync _updateProfileAsync = new UpdateProfileAsync();
        _updateProfileAsync.execute(params);
    }

    private void setFullName() {

        new MaterialDialog.Builder(_context)
                .title("Input Full Name")
                .content("Please type 3 to 40")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .inputRangeRes(3, 40, R.color.colorError)
                .positiveText("OK")
                .positiveColorRes(R.color.colorPrimary)
                .input("", fullName, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        fullName = String.valueOf(input);
                        updateProfile();
                    }
                })
                .negativeText("CANCEL")
                .negativeColorRes(R.color.colorDisable)
                .canceledOnTouchOutside(false)
                .show();
    }

    private void setBirthday(Bundle savedInstanceState) {

        LayoutInflater inflater = this.getLayoutInflater(savedInstanceState);
        View dialogView = inflater.inflate(R.layout.dialog_birthday, null);
        final DatePicker dateBirthday = (DatePicker)dialogView.findViewById(R.id.dp_birthday);

        if (!birthday.isEmpty()) {
            String[] birday = birthday.split("/");
            dateBirthday.updateDate(Integer.valueOf(birday[2]), Integer.valueOf(birday[0]) - 1, Integer.valueOf(birday[1]));
        }

        new MaterialDialog.Builder(_context)
                .title("Input Birthday")
                .customView(dialogView, false)
                .positiveText("OK")
                .positiveColorRes(R.color.colorPrimary)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        birthday = (dateBirthday.getMonth()+1) + "/" + dateBirthday.getDayOfMonth() +"/"+ dateBirthday.getYear();
                        updateProfile();
                    }
                })
                .negativeText("CANCEL")
                .negativeColorRes(R.color.colorDisable)
                .canceledOnTouchOutside(false)
                .show();
    }

    private void setEmailAddr() {

        new MaterialDialog.Builder(_context)
                .title("Input Email Address")
                .inputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                .positiveText("OK")
                .positiveColorRes(R.color.colorPrimary)
                .input("", email, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        email = String.valueOf(input);
                        updateProfile();
                    }
                })
                .negativeText("CANCEL")
                .negativeColorRes(R.color.colorDisable)
                .canceledOnTouchOutside(false)
                .show();
    }

    private void setPhoneNumber() {

        new MaterialDialog.Builder(_context)
                .title("Input Phone Number")
                .inputType(InputType.TYPE_CLASS_PHONE)
                .inputRangeRes(7, 20, R.color.colorError)
                .positiveText("OK")
                .positiveColorRes(R.color.colorPrimary)
                .input("", phone, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if (input.toString().isEmpty()) {}
                        phone = String.valueOf(input);
                        updateProfile();
                    }
                })
                .negativeText("CANCEL")
                .negativeColorRes(R.color.colorDisable)
                .canceledOnTouchOutside(false)
                .show();
    }

    public class UpdateProfileAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            progress.setMessage(getString(R.string.wait));
            progress.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... param) {

            String result = null;

            try {

                URL url = new URL(Constant.UPDATE_PROFILE_PAGE);

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("token", param[0]);
                    jsonObject.put("fullName", param[1]);
                    jsonObject.put("email", param[2]);
                    jsonObject.put("phoneNumber", param[3]);
                    jsonObject.put("birthday", param[4]);
                    jsonObject.put("gender", param[5]);
                    jsonObject.put("addr", param[6]);
                    jsonObject.put("addrDetail", param[7]);
                    jsonObject.put("zipCode", param[8]);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000); /* milliseconds */
                conn.setConnectTimeout(15000); /* milliseconds */
                conn.setUseCaches(false);
                conn.setRequestProperty("content-type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                wr.write(jsonObject.toString());
                wr.flush();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();
                    String str;

                    while((str = reader.readLine()) != null){
                        builder.append(str+"\n");
                    }

                    result = builder.toString();
                }  else {
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
                Toast.makeText(getActivity(), R.string.error_failed_connect, Toast.LENGTH_SHORT).show();
                return;
            } else if(result.equals("401")) {
                Toast.makeText(_context, "Sesi anda telah habis", Toast.LENGTH_SHORT).show();
                logout();
                return;
            }

            try {

                JSONObject jsonObj = new JSONObject(result);
                String statusCode = jsonObj.getString(Constant.JSON_STATUS_CODE);

                if (statusCode.equals("00")){
                    Gson gson = new GsonBuilder().create();
                    AppGlobal._userInfo = gson.fromJson(result, UserInfo.class);
                    AppGlobal._userDetailInfo.birthday = birthday;
                    dispUpdateInfo();
                } else {
                    String status = jsonObj.getString(Constant.JSON_STATUS_MESSAGE);
                    Toast.makeText(_context, status, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                // TODO: handle exception
                //Log.e("oasis", e.toString());
            }
        }
    }

    private void dispUpdateInfo() {



        tvFullName.setText(AppGlobal._userInfo.fullName);
        tvBirthday.setText(birthday);
        tvEmail.setText(AppGlobal._userInfo.email);
        tvPhone.setText(AppGlobal._userInfo.phoneNumber);
    }

    private void logout() {
        AppGlobal._userInfo = null;
        AppGlobal._userDetailInfo = null;
        Intent intent = new Intent(_context, LoginActivity.class);
        startActivity(intent);
        MainActivity._instance.finish();
    }

    public class UploadImageAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            progress.setMessage(getString(R.string.wait));
            progress.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... param) {

            String result = null;

            try {

                String url = Constant.UPLOAD_IMAGE_PAGE+param[0]+"/"+param[1];

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmUserPhoto.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                //there are some my custom fields form
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("photo", "tmp_photo_" + System.currentTimeMillis(), RequestBody.create(MediaType.parse("image/jpeg"), byteArray))
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();


                final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");

                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();

                if(response.code() == HttpURLConnection.HTTP_OK){
                    result = response.body().toString();
                } else {
                    result = String.valueOf(response.code());
                }

            } catch (MalformedURLException e) {
//                Log.e("oasis", e.toString());
            } catch (Exception e) {
//                Log.e("oasis", e.toString());
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            progress.dismiss();

            if (result == null){
                Toast.makeText(getActivity(), R.string.error_failed_connect, Toast.LENGTH_SHORT).show();
                return;
            } else if(result.equals("401")) {
                Toast.makeText(_context, "Sesi anda telah habis", Toast.LENGTH_SHORT).show();
                logout();
                return;
            }

            try {

                JSONObject jsonObj = new JSONObject(result);
                String statusCode = jsonObj.getString(Constant.JSON_STATUS_CODE);

                if (statusCode.equals("00")){
                    AppGlobal._userInfo.picUrl = jsonObj.getString(Constant.JSON_PIC_URL);
                    Picasso.with(_context).load(AppGlobal._userInfo.picUrl.toLowerCase()).memoryPolicy(MemoryPolicy.NO_CACHE )
                            .networkPolicy(NetworkPolicy.NO_CACHE).fit().into(civUserPhoto);
                    Picasso.with(_context).load(AppGlobal._userInfo.picUrl.toLowerCase()).memoryPolicy(MemoryPolicy.NO_CACHE )
                            .networkPolicy(NetworkPolicy.NO_CACHE).fit().transform(new BlurTransformation(_context)).into(ivBlurPhoto);
                    Picasso.with(MainActivity._instance).load(AppGlobal._userInfo.picUrl.toLowerCase()).memoryPolicy(MemoryPolicy.NO_CACHE )
                            .networkPolicy(NetworkPolicy.NO_CACHE).fit().into(MainActivity._instance.civUserPhoto);
                } else {
                    String status = jsonObj.getString(Constant.JSON_STATUS_MESSAGE);
                    Toast.makeText(_context, status, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                // TODO: handle exception
                //Log.e("oasis", e.toString());
            }
        }
    }
}
