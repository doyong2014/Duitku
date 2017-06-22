package com.mi1.duitku.Tab5.Register;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.Common.Constant;
import com.mi1.duitku.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepRegisterFourFragment extends Fragment {

    private EditText edt_referral_code;
    private EditText edt_referral_name;
    private RadioGroup radioPlacement;
    private RadioGroup radioMethod;
    private EditText edt_pin;

    private String referralCode;
    private String referralName;
    private String referralId;
    private String pin;

    private ProgressDialog progress;

    public StepRegisterFourFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_step_register_four, container, false);
        progress = new ProgressDialog(getContext());
        progress.setMessage(getString(R.string.wait));
        progress.setCanceledOnTouchOutside(false);
        edt_referral_code = (EditText) view.findViewById(R.id.edt_referral_code);
        edt_referral_name = (EditText) view.findViewById(R.id.edt_referral_name);
        edt_pin = (EditText) view.findViewById(R.id.edt_pin);
        radioPlacement = (RadioGroup) view.findViewById(R.id.radio_placement);
        radioMethod = (RadioGroup) view.findViewById(R.id.radio_reg_method);

        edt_referral_code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    String userCode = edt_referral_code.getText().toString();

                    String[] paramProfile = new String[2];
                    paramProfile[0] = userCode;
                    paramProfile[1] = "kode_user";

                    GetProfileAsync _profileAsync = new GetProfileAsync();
                    _profileAsync.execute(paramProfile);
                }
            }
        });
        Button btnContinue = (Button) view.findViewById(R.id.btn_continue_4);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedPlacement = radioPlacement.getCheckedRadioButtonId();
                int selectedMethod = radioMethod.getCheckedRadioButtonId();


                if(edt_referral_name.getText().toString().isEmpty())
                {
                    showDialog("Gagal Melanjutkan","User tidak terdaftar");
                    return;
                }
                else if(selectedPlacement == -1)
                {
                    showDialog("Data Dibutuhkan","Placement harus dipilih");
                    return;
                }
                else if(selectedMethod == -1)
                {
                    showDialog("Data Dibutuhkan","Registration Method harus dipilih");
                    return;
                }
                if(edt_pin.getText().toString().isEmpty())
                {
                    showDialog("Gagal Melanjutkan","PIN harus diisi");
                    return;
                }
                else
                    AppGlobal._registerInfo.pin = edt_pin.getText().toString();

                if(selectedPlacement == R.id.radio_left_a)
                {
                    AppGlobal._registerInfo.placement = "L";
                }
                else if(selectedPlacement == R.id.radio_right_b)
                {
                    AppGlobal._registerInfo.placement = "R";
                }

                if(selectedMethod == R.id.radio_full)
                {
                    AppGlobal._registerInfo.registration_method = "1";
                }
                else if(selectedMethod == R.id.radio_half)
                {
                    AppGlobal._registerInfo.registration_method = "2";
                }
                if(edt_referral_code.getText().toString().isEmpty())
                {
                    showDialog("Data Dibutuhkan","Kode User harus diisi");
                    return;
                }
                else
                {
                    AppGlobal._registerInfo.country_id = "1";
                    AppGlobal._registerInfo.sponsor_id = AppGlobal._userInfo.packageDetail.get(0).id_user.toString();
                    AppGlobal._registerInfo.sponsor_by = referralId;
                    AppGlobal._registerInfo.user_id = AppGlobal._userInfo.packageDetail.get(0).id_user.toString();
                    ((RegisterChildActivity)getActivity()).jumpToPage(v);
                }
            }
        });
        return view;
    }

    public class GetProfileAsync extends AsyncTask<String, Integer, String>
    {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            progress.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... param) {

            String result = null;

            try {

                URL url = new URL(Constant.URL_GET_PROFILE_DIGI1);//(Constant.LOGIN_PAGE); //+ "?loginUrl=" + Constant.URLLOGINDIGI1);
                StringBuilder postData = new StringBuilder();
                postData.append("username=" + param[0] + "&");
                postData.append("type=" + param[1]);
                byte[] postDataBytes = postData.toString().getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000); /* milliseconds */
                conn.setConnectTimeout(15000); /* milliseconds */
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                wr.write(postData.toString());
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
                Toast.makeText(getContext(), getString(R.string.error_failed_connect), Toast.LENGTH_SHORT).show();
                return;
            } else if(result.equals("401")) {
                Toast.makeText(getContext(), "Sesi anda telah habis", Toast.LENGTH_SHORT).show();
//                logout();
                return;
            }

            try {

                JSONObject jsonObj = new JSONObject(result);
                String statusCode = jsonObj.getString("status");

                if (statusCode.equals("true")){
                    JSONArray ja = jsonObj.getJSONArray("obj");
                    JSONObject profileObj = new JSONObject(ja.getString(0));
                    referralName = profileObj.getString("name");
                    referralId = profileObj.getString("id_user");
                    edt_referral_name.setText(referralName);
                }
                else if(statusCode.equals("false"))
                {
                    edt_referral_code.requestFocus();
                }
                else {
                    String status = jsonObj.getString("msg");//(Constant.JSON_STATUS_MESSAGE);
                    Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                // TODO: handle exception
                //Log.e("oasis", e.toString());
            }
        }
    }
    private void showDialog(String title, String msg) {

        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).show();
    }

}
