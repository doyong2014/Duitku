package com.android.duitku.redirectbankpage.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.UrlQuerySanitizer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.duitku.R;
import com.android.duitku.base.PresenterFactory;
import com.android.duitku.checkoutpage.view.CheckoutActivity;
import com.android.duitku.inquiry.view.InquiryActivity;
import com.android.duitku.paymentmethod.view.PaymentMethodActivity;
import com.android.duitku.paystatus.view.PayStatusActivity;
import com.android.duitku.redirectbankpage.presenter.RedirectBankPagePresenter;
import com.android.duitku.utils.DuitkuPreferences;
import com.android.duitku.utils.constant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by latif on 12/6/15.
 */
public class RedirectBankPage extends Activity implements RedirectBankPageView {

    private WebView mWebView;

    private DuitkuPreferences duitkuPreferences;

    private RedirectBankPagePresenter mRedirectBankPagePresenter;

    private ProgressBar mProgressBar;

    private String mCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                getWindow().setStatusBarColor(getResources().getColor(R.color.primaryBlue));
//            getWindow().setStatusBarColor(getResources().getColor(R.color.notification_bar_green));
        }
        else
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_redirect_web_bank);
        duitkuPreferences = new DuitkuPreferences(this);
        mRedirectBankPagePresenter = PresenterFactory.createBankPagePresenter(this);
        mRedirectBankPagePresenter.onInitView();
//        String bankUrl = duitkuPreferences.getBankUrl();
//        mRedirectBankPagePresenter.onLoadBankWebPage(bankUrl);

        try
        {
            String paymentUrl = parseUrlLocation(getIntent().getStringExtra("info"));
            mRedirectBankPagePresenter.onLoadBankWebPage(paymentUrl);
        }
        catch (JSONException e)
        {
            Log.e("RBP ERROR","Response " + e);
        }
    }

    @Override
    public void initView() {
        mWebView = (WebView) findViewById(R.id.webView);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    @Override
    public void loadBankWebPage(String bankUrl) {
        //payment using bbm
        if (getIntent().getBooleanExtra("usingbbm", false)) {
            mWebView.setWebViewClient(new MyWebViewClient());
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setAppCacheEnabled(true);
            mWebView.getSettings().setLoadWithOverviewMode(true);
            mWebView.getSettings().setAppCacheEnabled(false);
            mWebView.getSettings().setUseWideViewPort(true);
            mWebView.getSettings().setSaveFormData(false);
            mWebView.loadUrl(bankUrl);
            System.out.println(bankUrl);
        } else {
            mWebView.setWebViewClient(new MyWebViewClient());
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setAppCacheEnabled(false);
            mWebView.getSettings().setLoadWithOverviewMode(true);
            mWebView.getSettings().setAppCacheEnabled(false);
            mWebView.getSettings().setUseWideViewPort(true);
            mWebView.getSettings().setSaveFormData(false);
            mWebView.getSettings().setSavePassword(false);
            mWebView.clearCache(true);
            mWebView.loadUrl(bankUrl);
            System.out.println(bankUrl);
        }
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.toString().contains("resultCode")){
                String[] split = url.split("resultCode");
                String code = split[1].substring(1,3);
                if (code.equals("00")){
                    mCode = "00";
                    Toast.makeText(RedirectBankPage.this, "Pembayaran Berhasil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RedirectBankPage.this, PayStatusActivity.class);
                    intent.putExtra(constant.successwebpayment,true);
                    intent.putExtra("status","Success");
                    intent.putExtra("amount",getIntent().getStringExtra("amount"));
                    intent.putExtra("reference",getIntent().getStringExtra("reference"));
                    startActivity(intent);
                    finish();
                }
                else if(code.equals("01"))
                {
                    Toast.makeText(RedirectBankPage.this, "Pembayaran Gagal", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RedirectBankPage.this, PayStatusActivity.class);
                    intent.putExtra(constant.successwebpayment,false);
                    intent.putExtra("status","Failed");
                    intent.putExtra("amount",getIntent().getStringExtra("amount"));
                    intent.putExtra("reference",getIntent().getStringExtra("reference"));
                    startActivity(intent);
                    finish();
                }
            }
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            mProgressBar.setVisibility(View.GONE);
            RedirectBankPage.this.mProgressBar.setProgress(100);
            setValue(100);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mProgressBar.setVisibility(View.VISIBLE);
            RedirectBankPage.this.mProgressBar.setProgress(0);
            super.onPageStarted(view, url, favicon);
        }
    }

    private class WebViewClientUsingBBM extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }

    public void setValue(int progress) {
        this.mProgressBar.setProgress(progress);
        if (progress >= 100) // code to be added
            mProgressBar.setVisibility(View.GONE);  // code to be added
    }

    public String parseUrlLocation(String jsonData) throws JSONException{
        JSONObject Info = new JSONObject(jsonData);
        String paymentUrl = Info.getString("paymentUrl");
        paymentUrl = paymentUrl.replace("linda","192.168.0.10");
        return paymentUrl;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
