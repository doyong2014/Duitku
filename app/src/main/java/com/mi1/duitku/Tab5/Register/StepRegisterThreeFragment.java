package com.mi1.duitku.Tab5.Register;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.Common.Constant;
import com.mi1.duitku.R;

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
public class StepRegisterThreeFragment extends Fragment {

    private EditText edt_bank_name;
    private EditText edt_bank_branch;
    private EditText edt_bank_acc_number;
    private EditText edt_bank_address;
    private EditText edt_beneficiary_name;
    private EditText edt_beneficiary_id;
    private EditText edt_beneficiary_relationship;
    private EditText edt_spouse_name;
    private EditText edt_spouse_passport;
    private Button btn_last_step;

    private ProgressDialog progress;

    public StepRegisterThreeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_step_register_three, container, false);

        edt_bank_name = (EditText) view.findViewById(R.id.edt_bank_name);
        edt_bank_branch = (EditText) view.findViewById(R.id.edt_bank_branch);
        edt_bank_acc_number = (EditText) view.findViewById(R.id.edt_bank_acc_number);
        edt_bank_address = (EditText) view.findViewById(R.id.edt_bank_address);
        edt_beneficiary_name = (EditText) view.findViewById(R.id.edt_beneficiary_name);
        edt_beneficiary_id = (EditText) view.findViewById(R.id.edt_beneficiary_id);
        edt_beneficiary_relationship = (EditText) view.findViewById(R.id.edt_beneficiary_relationship);
        edt_spouse_name = (EditText) view.findViewById(R.id.edt_spouse_name);
        edt_spouse_passport = (EditText) view.findViewById(R.id.edt_spouse_passport);
        btn_last_step = (Button) view.findViewById(R.id.btn_last_step);
        progress = new ProgressDialog(getContext());
        progress.setMessage(getString(R.string.wait));
        progress.setCanceledOnTouchOutside(false);
        btn_last_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate())
                {
                    ((RegisterChildActivity)getActivity()).jumpToPage(v);
                }
                else
                {
                    return;
                }
            }
        });

        return view;
    }
    public  boolean validate()
    {
        if(edt_bank_name.getText().toString().isEmpty())
        {
            edt_bank_name.setError("Bank Name must be filled.");
            edt_bank_name.requestFocus();
            return false;
        }
        else if(edt_bank_branch.getText().toString().isEmpty())
        {
            edt_bank_branch.setError("Bank Branch must be filled.");
            edt_bank_branch.requestFocus();
            return false;
        }
        else if(edt_bank_acc_number.getText().toString().isEmpty())
        {
            edt_bank_acc_number.setError("Bank Account Number must be filled.");
            edt_bank_acc_number.requestFocus();
            return false;
        }
        else if(edt_bank_address.getText().toString().isEmpty())
        {
            edt_bank_address.setError("Bank address must be filled.");
            edt_bank_address.requestFocus();
            return false;
        }
        else if(edt_beneficiary_name.getText().toString().isEmpty())
        {
            edt_beneficiary_name.setError("Benerficiary name must be filled.");
            edt_beneficiary_name.requestFocus();
            return false;
        }
        else if(edt_beneficiary_id.getText().toString().isEmpty())
        {
            edt_beneficiary_id.setError("Benerficiary id number must be filled.");
            edt_beneficiary_id.requestFocus();
            return false;
        }
        else if(edt_beneficiary_relationship.getText().toString().isEmpty())
        {
            edt_beneficiary_relationship.setError("Benerficiary relationship must be filled.");
            edt_beneficiary_relationship.requestFocus();
            return false;
        }
        else if(edt_spouse_name.getText().toString().isEmpty())
        {
            edt_spouse_name.setError("Spouse name must be filled.");
            edt_spouse_name.requestFocus();
            return false;
        }
        else if(edt_spouse_passport.getText().toString().isEmpty())
        {
            edt_spouse_passport.setError("Spouse passport must be filled.");
            edt_spouse_passport.requestFocus();
            return false;
        }
        AppGlobal._registerInfo.bank_id = edt_bank_name.getText().toString();
        AppGlobal._registerInfo.bank_branch = edt_bank_branch.getText().toString();
        AppGlobal._registerInfo.bank_account = edt_bank_acc_number.getText().toString();
        AppGlobal._registerInfo.bank_address = edt_bank_address.getText().toString();
        AppGlobal._registerInfo.beneficiary_name = edt_beneficiary_name.getText().toString();
        AppGlobal._registerInfo.beneficiary_license = edt_beneficiary_id.getText().toString();
        AppGlobal._registerInfo.beneficiary_relationship = edt_beneficiary_relationship.getText().toString();
        AppGlobal._registerInfo.spouse_name = edt_spouse_name.getText().toString();
        AppGlobal._registerInfo.spouse_license = edt_spouse_passport.getText().toString();

        RegisterAsync registerAsync = new RegisterAsync();
        registerAsync.execute("");

        return true;
    }
    public class RegisterAsync extends AsyncTask<String, Integer, String> {

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

                URL url = new URL(Constant.URL_REGISTER_DIGI1);//(Constant.LOGIN_PAGE); //+ "?loginUrl=" + Constant.URLLOGINDIGI1);

                StringBuilder postData = new StringBuilder();
                postData.append("country_id=" + AppGlobal._registerInfo.country_id + "&");
                postData.append("package_id=" + AppGlobal._registerInfo.package_id + "&");
                postData.append("sponsor_id=" + AppGlobal._registerInfo.sponsor_id + "&");
                postData.append("sponsor_by=" + AppGlobal._registerInfo.sponsor_by + "&");
                postData.append("pin=" + AppGlobal._registerInfo.pin + "&");
                postData.append("information_nik=" + AppGlobal._registerInfo.information_nik + "&");
                postData.append("information_date_birth=" + AppGlobal._registerInfo.information_date_birth + "&");
                postData.append("information_name=" + AppGlobal._registerInfo.information_name + "&");
                postData.append("information_user_name=" + AppGlobal._registerInfo.information_user_name + "&");
                postData.append("information_address=" + AppGlobal._registerInfo.information_address + "&");
                postData.append("information_country=" + AppGlobal._registerInfo.information_country + "&");
                postData.append("information_state=" + AppGlobal._registerInfo.information_state + "&");
                postData.append("information_city=" + AppGlobal._registerInfo.information_city + "&");
                postData.append("information_zip=" + AppGlobal._registerInfo.information_zip + "&");
                postData.append("information_mobile=" + AppGlobal._registerInfo.information_mobile + "&");
                postData.append("information_email=" + AppGlobal._registerInfo.information_email + "&");
                postData.append("bank_id=" + AppGlobal._registerInfo.bank_id + "&");
                postData.append("bank_account=" + AppGlobal._registerInfo.bank_account + "&");
                postData.append("user_id=" + AppGlobal._registerInfo.user_id + "&");
                postData.append("registration_method=" + AppGlobal._registerInfo.registration_method + "&");
                postData.append("information_gender=" + AppGlobal._registerInfo.information_gender + "&");
                postData.append("information_home=" + AppGlobal._registerInfo.information_home + "&");
                postData.append("information_office=" + AppGlobal._registerInfo.information_office + "&");
                postData.append("information_other_state=" + AppGlobal._registerInfo.information_other_state + "&");
                postData.append("bank_branch=" + AppGlobal._registerInfo.bank_branch + "&");
                postData.append("bank_address=" + AppGlobal._registerInfo.bank_address + "&");
                postData.append("bank_account=" + AppGlobal._registerInfo.bank_account + "&");
                postData.append("benerficiary_name=" + AppGlobal._registerInfo.beneficiary_name + "&");
                postData.append("benerficiary_license=" + AppGlobal._registerInfo.beneficiary_license + "&");
                postData.append("benerficiary_relationship=" + AppGlobal._registerInfo.beneficiary_relationship + "&");
                postData.append("spouse_name=" + AppGlobal._registerInfo.spouse_name + "&");
                postData.append("spouse_license=" + AppGlobal._registerInfo.spouse_license + "&");
                postData.append("sponsor_id=" + AppGlobal._registerInfo.sponsor_id + "&");
                postData.append("placement=" + AppGlobal._registerInfo.placement);
                byte[] postDataBytes = postData.toString().getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000); /* milliseconds */
                conn.setConnectTimeout(15000); /* milliseconds */
                //conn.setRequestProperty("content-type", "application/json");
                //conn.setRequestProperty("Accept", "application/json");
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

            if (result == null){
                progress.dismiss();
                return;
            }

            try {

                JSONObject jsonObj = new JSONObject(result);
                boolean status = (boolean)jsonObj.get("status");

                if (status){
                    AppGlobal.activationCode = jsonObj.getString("activation");
                }
                else {
                    progress.dismiss();
                    String error = jsonObj.getString("errors");
                }

            } catch (Exception e) {
                // TODO: handle exception
                progress.dismiss();
                Log.e("oasis", e.getMessage());
            }
        }
    }
}
