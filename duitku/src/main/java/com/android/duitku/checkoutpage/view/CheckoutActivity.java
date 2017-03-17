package com.android.duitku.checkoutpage.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.duitku.R;
import com.android.duitku.base.BaseActivity;
import com.android.duitku.base.PresenterFactory;
import com.android.duitku.checkoutpage.presenter.CheckoutPresenter;
import com.android.duitku.login.view.LoginDuitkuActivity;
import com.android.duitku.model.Inquiry;
import com.android.duitku.model.InquiryResponse;
import com.android.duitku.paymentmethod.view.PaymentMethodActivity;
import com.android.duitku.redirectbankpage.view.RedirectBankPage;
import com.android.duitku.utils.DuitkuPreferences;
import com.android.duitku.utils.constant;
import com.google.gson.Gson;

/**
 * Created by latif on 12/8/15.
 */
public class CheckoutActivity extends BaseActivity implements CheckoutView {

    private CheckoutPresenter mCheckoutPresenter;

    private TextView mItemTotal;

    private TextView mAdminFee;

    private TextView mOrderTotal;

    private Button mBtnBayar;

    private DuitkuPreferences duitkuPreferences;

    private ImageView mImgBank;

    private int orderTotal;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation_page_activity);
        initToolbarView();
        duitkuPreferences = new DuitkuPreferences(CheckoutActivity.this);
        mCheckoutPresenter = PresenterFactory.createCheckoutPresenter(this, getNetworkManager());
        mCheckoutPresenter.onInitView();
        initCheckoutInfo();

        if (!duitkuPreferences.getInquiry().isEmpty()) {
            Inquiry inquiry = new Gson().fromJson(duitkuPreferences.getInquiry(), Inquiry.class);
            inquiry.setAmount(String.valueOf(orderTotal));
            inquiry.setAmountAfterFee(String.valueOf(orderTotal));
            //handle is pay failed

            if (getIntent().getStringExtra(constant.webpaymentstatus) != null) {
                if (getIntent().getStringExtra(constant.webpaymentstatus).equals(constant.failed)) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    Intent intentRedirectBankPage = new Intent(CheckoutActivity.this, RedirectBankPage.class);
                    intentRedirectBankPage.putExtras(getIntent());
                    startActivity(intentRedirectBankPage);
                }
            } else {
                mCheckoutPresenter.onInquiryData(inquiry);
            }
        } else {
            Toast.makeText(this, "please create new order Id", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void initView() {
        mItemTotal = (TextView) findViewById(R.id.txt_item_total);
        mAdminFee = (TextView) findViewById(R.id.txt_admin_fee);
        mOrderTotal = (TextView) findViewById(R.id.txt_order_total);
        mBtnBayar = (Button) findViewById(R.id.btn_bayar);
        mImgBank = (ImageView) findViewById(R.id.img_bank);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mImgBank.setImageResource(getResources().getIdentifier(duitkuPreferences.getImagename(), "drawable", getPackageName()));
//        mBtnBayar.setOnClickListener(this);
        mBtnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSimpleProgressDialog(null);
                if (duitkuPreferences.isUsingDuitku()) {
                    Intent intentToLogin = new Intent(CheckoutActivity.this, LoginDuitkuActivity.class);
                    startActivity(intentToLogin);
                } else {
                    if (!duitkuPreferences.getInquiry().isEmpty()) {
                        Inquiry inquiry = new Gson().fromJson(duitkuPreferences.getInquiry(), Inquiry.class);
                        inquiry.setAmount(String.valueOf(orderTotal));
                        inquiry.setAmountAfterFee(String.valueOf(orderTotal));
                        //handle is pay failed

                        if (getIntent().getStringExtra(constant.webpaymentstatus) != null) {
                            if (getIntent().getStringExtra(constant.webpaymentstatus).equals(constant.failed)) {
                                mProgressBar.setVisibility(View.INVISIBLE);
                                Intent intentRedirectBankPage = new Intent(CheckoutActivity.this, RedirectBankPage.class);
                                intentRedirectBankPage.putExtras(getIntent());
                                startActivity(intentRedirectBankPage);
                            }
                        } else {
                            mCheckoutPresenter.onInquiryData(inquiry);
                        }
                    } else {
                        Toast.makeText(CheckoutActivity.this, "please create new order Id", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void initCheckoutInfo() {
        Inquiry mInquiry = new Gson().fromJson(duitkuPreferences.getInquiry(), Inquiry.class);
        mItemTotal.setText("Rp. " + mInquiry.getAmount().toString());
        int item = Integer.parseInt(mInquiry.getAmount().toString());
        int adminfee = duitkuPreferences.getAdminFee();
        orderTotal = item + adminfee;
        mAdminFee.setText("Rp. " + adminfee);
        mOrderTotal.setText("Rp. " + String.valueOf(orderTotal));
    }

    @Override
    public void emitInquiryResponse(InquiryResponse inquiryResponse) {
        if (inquiryResponse != null) {
            //if (inquiryResponse.getStatusCode().equals("00")) {
                dismissSimpleProgressDialog();
                duitkuPreferences.saveInquiryResponse(new Gson().toJson(inquiryResponse));
                Intent intentRedirectBankPage = new Intent(CheckoutActivity.this, RedirectBankPage.class);
                //info whether using bbm or other web payment
                intentRedirectBankPage.putExtras(getIntent());
                startActivity(intentRedirectBankPage);
            /*} else {
                dismissSimpleProgressDialog();
                Toast.makeText(CheckoutActivity.this, getString(R.string.data_already_inquiry), Toast.LENGTH_SHORT).show();

            }*/
        }
    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.btn_bayar:
//                showSimpleProgressDialog(null);
//                if (duitkuPreferences.isUsingDuitku()) {
//                    Intent intentToLogin = new Intent(CheckoutActivity.this, LoginDuitkuActivity.class);
//                    startActivity(intentToLogin);
//                } else {
//                    if (!duitkuPreferences.getInquiry().isEmpty()) {
//                        Inquiry inquiry = new Gson().fromJson(duitkuPreferences.getInquiry(), Inquiry.class);
//                        inquiry.setAmount(String.valueOf(orderTotal));
//                        inquiry.setAmountAfterFee(String.valueOf(orderTotal));
//                        //handle is pay failed
//
//                        if (getIntent().getStringExtra(constant.webpaymentstatus) != null) {
//                            if (getIntent().getStringExtra(constant.webpaymentstatus).equals(constant.failed)) {
//                                mProgressBar.setVisibility(View.INVISIBLE);
//                                Intent intentRedirectBankPage = new Intent(CheckoutActivity.this, RedirectBankPage.class);
//                                intentRedirectBankPage.putExtras(getIntent());
//                                startActivity(intentRedirectBankPage);
//                            }
//                        } else {
//                            mCheckoutPresenter.onInquiryData(inquiry);
//                        }
//                    } else {
//                        Toast.makeText(this, "please create new order Id", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                break;
//        }
//    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, PaymentMethodActivity.class));
    }
}
