package com.mi1.duitku.Tab5.Register;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.Common.CommonFunction;
import com.mi1.duitku.Common.Constant;
import com.mi1.duitku.Common.PackageDetailInfo;
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
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StepRegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StepRegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepRegisterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private TextView lblPage;
    private TextView lblActivation;
    private ProgressDialog progress;
    private String activationCode;

    private OnFragmentInteractionListener mListener;

    public static StepRegisterFragment newInstance(int page, boolean isLast) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        if (isLast)
            args.putBoolean("isLast", true);
        final StepRegisterFragment fragment = new StepRegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final int page = getArguments().getInt("page", 0);
        if (getArguments().containsKey("isLast"))
        {
            lblPage.setText("Kode Aktivasi:");
            String param = "";
//            RegisterAsync registerAsync = new RegisterAsync();
//            registerAsync.execute(param);
        }
        else
        {
            lblPage.setText(Integer.toString(page));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_step_register, container, false);
        lblPage = (TextView) view.findViewById(R.id.lbl_page);
        lblActivation = (TextView) view.findViewById(R.id.lblActivation);
        progress = new ProgressDialog(getContext());
        progress.setMessage(getString(R.string.wait));
        progress.setCanceledOnTouchOutside(false);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
      /*  if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
                    activationCode = jsonObj.getString("activation");
                    lblActivation.setText(activationCode);
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
