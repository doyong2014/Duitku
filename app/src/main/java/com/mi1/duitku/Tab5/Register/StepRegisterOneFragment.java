package com.mi1.duitku.Tab5.Register;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StepRegisterOneFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StepRegisterOneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepRegisterOneFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private MaterialBetterSpinner spinner_package;

    String[] SPINNERLIST = {"a","b","c","d","e"};//getActivity().getApplicationContext().

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String selectedPackage;
    private String selectedPackageValue;
    private int RWNeeded;
    private int currentRW;

    private OnFragmentInteractionListener mListener;

    public StepRegisterOneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StepRegisterOneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StepRegisterOneFragment newInstance(String param1, String param2) {
        StepRegisterOneFragment fragment = new StepRegisterOneFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        SPINNERLIST = getResources().getStringArray(R.array.packagelist);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_step_register_one, container, false);
        spinner_package = (MaterialBetterSpinner) view.findViewById(R.id.spinner_package);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, SPINNERLIST);
        spinner_package.setAdapter(arrayAdapter);
        spinner_package.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPackage = getResources().getStringArray(R.array.packagelist)[position];
                selectedPackageValue = getResources().getStringArray(R.array.packagelistvalue)[position];
                switch (selectedPackage)
                {
                    case "Community":
                        RWNeeded = 8000;
                        break;
                    case "Group":
                        RWNeeded = 4000;
                        break;
                    case "Family":
                        RWNeeded = 2100;
                        break;
                    case "Personal":
                        RWNeeded = 500;
                        break;
                    case "Trial":
                        RWNeeded = 140;
                        break;
                }
            }
        });
//        spinner_package.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//        {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        currentRW = Integer.parseInt(AppGlobal._userInfo.packageDetail.get(0).rv.replace(".00",""));
        Button btnContinue = (Button) view.findViewById(R.id.btn_login);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate())
                {
                    AppGlobal._registerInfo.package_id = selectedPackageValue;
                    ((RegisterChildActivity)getActivity()).jumpToPage(v);
                }
                else
                {
                    showDialog();
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

    public boolean validate()
    {
        if(currentRW < RWNeeded)
            return false;
        else
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

    private void showDialog() {

        new AlertDialog.Builder(getContext())
                .setTitle("RW Tidak Cukup")
                .setMessage("Jumlah Register Wallet Kurang")
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).show();
    }
}
