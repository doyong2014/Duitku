package com.mi1.duitku.Tab3;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.Common.CommonFunction;
import com.mi1.duitku.Common.Constant;
import com.mi1.duitku.Common.HeaderView;
import com.mi1.duitku.LoginActivity;
import com.mi1.duitku.Main.MainActivity;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab5.ChangePasswordActivity;
import com.mi1.duitku.Tab5.ShareCodeActivity;

import org.json.JSONException;
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
public class DompetFragment extends Fragment {

    private Context _context;
    private TwinklingRefreshLayout refresh;
    TextView tvBalance;

    public DompetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_dompet, container, false);
        _context = getContext();

        refresh = (TwinklingRefreshLayout) view.findViewById(R.id.refreshLayout);
        HeaderView headerView = (HeaderView) View.inflate(getActivity(), R.layout.header_refresh, null);
        refresh.setHeaderView(headerView);
        refresh.setEnableLoadmore(false);

        refresh.setOnRefreshListener(new RefreshListenerAdapter() {

            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                getBanlace();
            }
        });

        tvBalance = (TextView) view.findViewById(R.id.txt_balance);
        tvBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(_context, ShareCodeActivity.class);
                _context.startActivity(intent);
            }
        });
//        tvBalance.setText(CommonFunction.formatNumberingWithoutRP(AppGlobal._userInfo.userbalance));

        TextView tvTopup = (TextView) view.findViewById(R.id.txt_topup);
        tvTopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(_context, TopUpActivity.class);
                _context.startActivity(intent);
            }
        });

        TextView tvTopupBank = (TextView) view.findViewById(R.id.txt_topup_bank);
        tvTopupBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(_context, BankTransferActivity.class);
                _context.startActivity(intent);
            }
        });

        TextView tvPrePayment = (TextView) view.findViewById(R.id.txt_pre_payment);
        tvPrePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPrePaidDialog();
            }
        });

        TextView tvPostPayment = (TextView) view.findViewById(R.id.txt_post_payment);
        tvPostPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPostPaidDialog();
            }
        });

        TextView tvTransfer = (TextView) view.findViewById(R.id.txt_transfer);
        tvTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTransferDialog();
            }
        });

        TextView tvConvert = (TextView) view.findViewById(R.id.txt_convert);
        tvConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConvertDialog();
            }
        });

        TextView tvExchange = (TextView) view.findViewById(R.id.txt_exchange);
        tvExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExchangeDialog();
            }
        });

        CardView cardPayment = (CardView) view.findViewById(R.id.card_payment);
        cardPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPostPaidDialog();
            }
        });

        CardView cardBuy = (CardView) view.findViewById(R.id.card_buy);
        cardBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPrePaidDialog();
            }
        });

        CardView cardTransfer = (CardView) view.findViewById(R.id.card_transfer);
        cardTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTransferDialog();
            }
        });

        return view;
    }

    private void getBanlace(){

        String[] params = new String[2];
        params[0] = AppGlobal._userInfo.token;
        params[1] = Constant.COMMUNITY_CODE;
        GetBalanceAsync _getBalanceAsync = new GetBalanceAsync();
        _getBalanceAsync.execute(params);
    }

    public class GetBalanceAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... param) {

            String result = null;

            try {

                URL url = new URL(Constant.GET_BALANCE_PAGE);

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("token", param[0]);
                    jsonObject.put("community_code", param[1]);

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

            refresh.finishRefreshing();
            refresh.finishLoadmore();

            if (result == null){
                Toast.makeText(getActivity(), R.string.error_failed_connect, Toast.LENGTH_SHORT).show();
                return;
            }  else if(result.equals("401")) {
                Toast.makeText(_context, "Sesi anda telah habis", Toast.LENGTH_SHORT).show();
                logout();
                return;
            }

            try {
                JSONObject jsonObj = new JSONObject(result);
                String statusCode = jsonObj.getString(Constant.JSON_STATUS_CODE);
                if (statusCode.equals("00")){
                    AppGlobal._userInfo.userbalance = jsonObj.getString(Constant.JSON_BALANCE);
                    tvBalance.setText(CommonFunction.formatNumberingWithoutRP(AppGlobal._userInfo.userbalance));
                } else if(statusCode.equals("-100")){
                    Toast.makeText(_context, "Sesi anda telah habis", Toast.LENGTH_SHORT).show();
                    logout();
                }else {
                    String status = jsonObj.getString(Constant.JSON_STATUS_MESSAGE);
                    Toast.makeText(_context, status, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // TODO: handle exception
                //Log.e("oasis", e.toString());
            }
        }
    }

    private void showPrePaidDialog() {

        new MaterialDialog.Builder(_context)
                .items(R.array.prepaid)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                      /*  if(which == 0) {
                            Intent intent = new Intent(_context, PurchaseProcessPLNActivity.class);
                            startActivity(intent);
                        } else if(which == 1){
                            Intent intent = new Intent(_context, PurchaseActivity.class);
                            startActivity(intent);
                        }*/
                        return true;
                    }
                })
                .positiveText("OK")
                .positiveColorRes(R.color.colorPrimary)
                .negativeText("CANCEL")
                .negativeColorRes(R.color.colorDisable)
                .canceledOnTouchOutside(false)
                .show();
    }

    private void showPostPaidDialog() {

        new MaterialDialog.Builder(_context)
                .items(R.array.postpaid)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        Intent intent = null;
                        /*if(which == 0) {
                            intent = new Intent(_context, PaymentProcessActivity.class);
                            intent.putExtra(PaymentProcessActivity.TAG_ACTIVITYTITLE, "PLN Pasca Bayar");
                            intent.putExtra(PaymentProcessActivity.TAG_ACTIVITYPRODUCTCODE, "PLNPASCH");
                            intent.putExtra(PaymentProcessActivity.TAG_ACTIVITYPRODUCTNAME, "PLN PASCA BAYAR");
                            startActivity(intent);
                        } else {
                            intent = new Intent(_context, PaymentActivity.class);
                            String product_title = getResources().getStringArray(R.array.postpaid)[which];
                            intent.putExtra(PaymentActivity.TAG_ACTIVITYTITLE, product_title);
                            startActivity(intent);
                        }*/
                        return true;
                    }
                })
                .positiveText("OK")
                .positiveColorRes(R.color.colorPrimary)
                .negativeText("CANCEL")
                .negativeColorRes(R.color.colorDisable)
                .canceledOnTouchOutside(false)
                .show();
    }

    private void showTransferDialog() {

        new MaterialDialog.Builder(_context)
                .items(R.array.transferdigi1)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        Intent intent = null;
                        intent = new Intent(_context, TransferActivity.class);
                        intent.putExtra(PaymentProcessActivity.TAG_ACTIVITYTITLE, getResources().getStringArray(R.array.transferdigi1)[which]);
                        intent.putExtra(PaymentProcessActivity.TAG_ACTIVITYPRODUCTCODE, getResources().getStringArray(R.array.transferdigi1)[which]);
                        intent.putExtra(PaymentProcessActivity.TAG_ACTIVITYPRODUCTNAME, getResources().getStringArray(R.array.transferdigi1)[which]);
                        startActivity(intent);
                        return true;
                    }
                })
                .positiveText("OK")
                .positiveColorRes(R.color.colorPrimary)
                .negativeText("CANCEL")
                .negativeColorRes(R.color.colorDisable)
                .canceledOnTouchOutside(false)
                .show();
    }

    private void showConvertDialog()
    {
        new MaterialDialog.Builder(_context)
                .items(R.array.convertdigi1)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        Intent intent = null;
                        intent = new Intent(_context, ConvertActivity.class);
                        intent.putExtra(PaymentProcessActivity.TAG_ACTIVITYTITLE, getResources().getStringArray(R.array.convertdigi1)[which]);
                        intent.putExtra(PaymentProcessActivity.TAG_ACTIVITYPRODUCTCODE, getResources().getStringArray(R.array.convertdigi1)[which]);
                        intent.putExtra(PaymentProcessActivity.TAG_ACTIVITYPRODUCTNAME, getResources().getStringArray(R.array.convertdigi1)[which]);
                        startActivity(intent);
                        return true;
                    }
                })
                .positiveText("OK")
                .positiveColorRes(R.color.colorPrimary)
                .negativeText("CANCEL")
                .negativeColorRes(R.color.colorDisable)
                .canceledOnTouchOutside(false)
                .show();
    }

    private void showExchangeDialog()
    {
        new MaterialDialog.Builder(_context)
                .items(R.array.exchangedigi1)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        Intent intent = null;
                        intent = new Intent(_context, TransferActivity.class);
                        intent.putExtra(PaymentProcessActivity.TAG_ACTIVITYTITLE, getResources().getStringArray(R.array.exchangedigi1)[which]);
                        intent.putExtra(PaymentProcessActivity.TAG_ACTIVITYPRODUCTCODE, getResources().getStringArray(R.array.exchangedigi1)[which]);
                        intent.putExtra(PaymentProcessActivity.TAG_ACTIVITYPRODUCTNAME, getResources().getStringArray(R.array.exchangedigi1)[which]);
                        startActivity(intent);
                        return true;
                    }
                })
                .positiveText("OK")
                .positiveColorRes(R.color.colorPrimary)
                .negativeText("CANCEL")
                .negativeColorRes(R.color.colorDisable)
                .canceledOnTouchOutside(false)
                .show();
    }

    private void logout() {
        AppGlobal._userInfo = null;
        AppGlobal._userDetailInfo = null;
        Intent intent = new Intent(_context, LoginActivity.class);
        startActivity(intent);
        MainActivity._instance.finish();
    }
}
