package com.mi1.duitku.Tab3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mi1.duitku.Common.Constant;
import com.mi1.duitku.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RedirectBankActivity extends AppCompatActivity {

    private ProgressBar progress;
    private WebView mWebView;
    private String mCode = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirect_bank);

        mWebView = (WebView) findViewById(R.id.webView);
        progress = (ProgressBar) findViewById(R.id.progress_bar);

        try
        {
            String paymentUrl = parseUrlLocation(getIntent().getStringExtra("info"));
            loadBankWebPage(paymentUrl);
        }
        catch (JSONException e)
        {
            Log.e("RBP ERROR","Response " + e);
        }
    }

    public String parseUrlLocation(String jsonData) throws JSONException{
        JSONObject Info = new JSONObject(jsonData);
        String paymentUrl = Info.getString("paymentUrl");
        paymentUrl = paymentUrl.replace("linda","192.168.0.10");
        return paymentUrl;
    }

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
                    Toast.makeText(RedirectBankActivity.this, "Pembayaran Berhasil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RedirectBankActivity.this, PayStatusActivity.class);
                    intent.putExtra(Constant.SUCCESS_WEB_PAYMENT, true);
                    intent.putExtra("status", "Success");
                    intent.putExtra("amount", getIntent().getStringExtra("amount"));
                    intent.putExtra("reference", getIntent().getStringExtra("reference"));
                    startActivity(intent);
                    finish();
                }
                else if(code.equals("01"))
                {
                    Toast.makeText(RedirectBankActivity.this, "Pembayaran Gagal", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RedirectBankActivity.this, PayStatusActivity.class);
                    intent.putExtra(Constant.SUCCESS_WEB_PAYMENT, false);
                    intent.putExtra("status","Failed");
                    intent.putExtra("amount", getIntent().getStringExtra("amount"));
                    intent.putExtra("reference", getIntent().getStringExtra("reference"));
                    startActivity(intent);
                    finish();
                }
            }
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progress.setVisibility(View.GONE);
            progress.setProgress(100);
            setValue(100);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(0);
            super.onPageStarted(view, url, favicon);
        }
    }

    public void setValue(int progress) {
        this.progress.setProgress(progress);
        if (progress >= 100) // code to be added
            this.progress.setVisibility(View.GONE);  // code to be added
    }
}
