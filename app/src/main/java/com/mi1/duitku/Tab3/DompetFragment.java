package com.mi1.duitku.Tab3;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mi1.duitku.Common.Constant;
import com.mi1.duitku.Common.UserInfo;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab3.Common.CPPOBProduct;
import com.mi1.duitku.Tab3.Common.CPPOBProductParent;
import com.mi1.duitku.Tab3.Common.GlobalData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class DompetFragment extends Fragment {

    private Context context;
    private ProgressDialog progressDialog;

    public DompetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_dompet, container, false);

        context = getContext();

        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        if (GlobalData.m_product_payment == null || GlobalData.m_product_purchase == null) {
            new callProductList().execute();
        }

        TextView balance = (TextView) view.findViewById(R.id.txt_balance);
        balance.setText(UserInfo.mUserBalance);

        TextView topup = (TextView) view.findViewById(R.id.txt_topup);
        topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(context, TopUpActivity.class);
                context.startActivity(intent);
            }
        });

        TextView topupBank = (TextView) view.findViewById(R.id.txt_topup_bank);
        topupBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(context, BankTransferActivity.class);
                context.startActivity(intent);
            }
        });

        TextView prePayment = (TextView) view.findViewById(R.id.txt_pre_payment);
        prePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPrePaidDialog();
            }
        });

        TextView postPayment = (TextView) view.findViewById(R.id.txt_post_payment);
        postPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPostPaidDialog();
            }
        });

        return view;
    }

    private void showPrePaidDialog() {

        new MaterialDialog.Builder(getContext())
                .items(R.array.prepaid)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if(which == 0) {
                            Intent intent = new Intent(context, PLNPrepaidActivity.class);
                            startActivity(intent);
                        } else if(which == 1){
                            int b = 1;
                        }
                        return true;
                    }
                })
                .positiveText("OK")
                .positiveColorRes(R.color.colorPrimary)
                .negativeText("CANCEL")
                .negativeColorRes(R.color.colorDisable)
                .show();
    }

    private void showPostPaidDialog() {

        new MaterialDialog.Builder(getContext())
                .items(R.array.postpaid)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        Intent intent = null;
                        if(which == 0) {
                            intent = new Intent(getContext(), PLNPrepaidActivity.class);
                            startActivity(intent);
                        } else if(which == 1){
                            intent = new Intent(getContext(), PLNPrepaidActivity.class);
                            startActivity(intent);
                        }
                        return true;
                    }
                })
                .positiveText("OK")
                .positiveColorRes(R.color.colorPrimary)
                .negativeText("CANCEL")
                .negativeColorRes(R.color.colorDisable)
                .show();
    }

    public class callProductList extends AsyncTask<String, Void, String> {

//        ReturnStatus returnStatus = new ReturnStatus();

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Processing...");
            progressDialog.show();
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

            progressDialog.dismiss();

            if (result == null){
                Toast.makeText(context, R.string.error_failed_connect, Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONArray objArr = new JSONArray(result);
                for (int i = 0; i < objArr.length(); i++) {
                    JSONObject obj = objArr.getJSONObject(i);
                    if (obj.getString("name").toString().toUpperCase().equals("PPOB PRA BAYAR")) {
                        GlobalData.m_product_purchase = ConvertJsontoCCPOBProductParent(obj);
                    }
                    else if (obj.getString("name").toString().toUpperCase().equals("PPOB PASCA BAYAR")) {
                        GlobalData.m_product_payment = ConvertJsontoCCPOBProductParent(obj);
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

}
