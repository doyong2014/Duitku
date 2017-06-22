package com.mi1.duitku.Tab5.Register;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.Common.Constant;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab3.ExchangeActivity;
import com.mi1.duitku.Tab3.TransferActivity;

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
 * Activities that contain this fragment must implement the
 * {@link StepRegisterTwoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class StepRegisterTwoFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private EditText edt_nama;
    private EditText edt_username;
    private EditText edt_birthday;
    private EditText edt_passport;
    private EditText edt_address;
    private EditText edt_province;
    private EditText edt_city;
    private EditText edt_zipcode;
    private EditText edt_telpnum;
    private EditText edt_officenum;
    private EditText edt_homenum;
    private EditText edt_fax;
    private EditText edt_email;
    private RadioGroup radio_gender;
    private Button btn_step2;


    public StepRegisterTwoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_step_register_two, container, false);
        edt_nama = (EditText) view.findViewById(R.id.edt_nama);
        edt_address = (EditText) view.findViewById(R.id.edt_address);
        edt_birthday = (EditText) view.findViewById(R.id.edt_birhday);
        edt_city = (EditText) view.findViewById(R.id.edt_city);
        edt_email = (EditText) view.findViewById(R.id.edt_email);
        edt_fax = (EditText) view.findViewById(R.id.edt_fax);
        edt_homenum = (EditText) view.findViewById(R.id.edt_homenum);
        edt_officenum = (EditText) view.findViewById(R.id.edt_officenum);
        edt_passport = (EditText) view.findViewById(R.id.edt_passport);
        edt_province = (EditText) view.findViewById(R.id.edt_province);
        edt_telpnum = (EditText) view.findViewById(R.id.edt_telpnum);
        edt_username = (EditText) view.findViewById(R.id.edt_username);
        edt_zipcode = (EditText) view.findViewById(R.id.edt_zipcode);
        radio_gender = (RadioGroup) view.findViewById(R.id.radio_gender);
        btn_step2 = (Button) view.findViewById(R.id.btn_step2);
        btn_step2.setOnClickListener(new View.OnClickListener() {
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public  boolean validate()
    {
        int selectedGender = radio_gender.getCheckedRadioButtonId();
        if(edt_nama.getText().toString().isEmpty())
        {
            edt_nama.setError("Name must be filled.");
            edt_nama.requestFocus();
            return false;
        }
        else if(edt_address.getText().toString().isEmpty())
        {
            edt_address.setError("Address must be filled.");
            edt_address.requestFocus();
            return false;
        }
        else if(edt_birthday.getText().toString().isEmpty())
        {
            edt_birthday.setError("Birthdate must be filled.");
            edt_birthday.requestFocus();
            return false;
        }
        else if(edt_city.getText().toString().isEmpty())
        {
            edt_city.setError("City must be filled.");
            edt_city.requestFocus();
            return false;
        }
        else if(edt_email.getText().toString().isEmpty())
        {
            edt_email.setError("Email must be filled.");
            edt_email.requestFocus();
            return false;
        }
        else if(edt_homenum.getText().toString().isEmpty())
        {
            edt_homenum.setError("Home phone number must be filled.");
            edt_homenum.requestFocus();
            return false;
        }
        else if(edt_passport.getText().toString().isEmpty())
        {
            edt_passport.setError("License ID must be filled.");
            edt_passport.requestFocus();
            return false;
        }
        else if(edt_province.getText().toString().isEmpty())
        {
            edt_province.setError("Province must be filled.");
            edt_province.requestFocus();
            return false;
        }
        else if(edt_telpnum.getText().toString().isEmpty())
        {
            edt_telpnum.setError("Phone number must be filled.");
            edt_telpnum.requestFocus();
            return false;
        }
        else if(edt_username.getText().toString().isEmpty())
        {
            edt_username.setError("Username must be filled.");
            edt_username.requestFocus();
            return false;
        }
        else if(edt_zipcode.getText().toString().isEmpty())
        {
            edt_zipcode.setError("Zipcode must be filled.");
            edt_zipcode.requestFocus();
            return false;
        }
        else if(selectedGender == -1)
        {
            showDialog("Failed","Gender must be choosen.");
            return false;
        }
        AppGlobal._registerInfo.information_name = edt_nama.getText().toString();
        AppGlobal._registerInfo.information_address = edt_address.getText().toString();
        AppGlobal._registerInfo.information_date_birth = edt_birthday.getText().toString();
        AppGlobal._registerInfo.information_city = edt_city.getText().toString();
        AppGlobal._registerInfo.information_email = edt_email.getText().toString();
        AppGlobal._registerInfo.information_home = edt_homenum.getText().toString();
        AppGlobal._registerInfo.information_office = edt_officenum.getText().toString();
        AppGlobal._registerInfo.information_nik = edt_passport.getText().toString();
        AppGlobal._registerInfo.information_state = edt_province.getText().toString();
        AppGlobal._registerInfo.information_mobile = edt_telpnum.getText().toString();
        AppGlobal._registerInfo.information_user_name = edt_username.getText().toString();
        AppGlobal._registerInfo.information_zip = edt_zipcode.getText().toString();
        if(selectedGender == R.id.radio_male)
        {
            AppGlobal._registerInfo.information_gender = "L";
        }
        else
        {
            AppGlobal._registerInfo.information_gender = "P";
        }
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
