package com.android.duitku.utils;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;

import com.android.duitku.redirectbankpage.view.RedirectBankPage;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Ardian on 10/03/2016.
 */
public class InquiryTransaction
{
    public String merchantCode;
    public String paymentAmount;
    public String paymentMethod;
    public String merchantOrderId;
    public String productDetails;
    public String additionalParam;
    public String merchantUserInfo;
    public String callbackUrl;
    public String returnUrl;
    public String signature;
    public String jsonData;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = InquiryTransaction.class.getSimpleName();

    //region getter and setter
    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    public String getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public String getAdditionalParam() {
        return additionalParam;
    }

    public void setAdditionalParam(String additionalParam) {
        this.additionalParam = additionalParam;
    }

    public String getMerchantUserInfo() {
        return merchantUserInfo;
    }

    public void setMerchantUserInfo(String merchantUserInfo) {
        this.merchantUserInfo = merchantUserInfo;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
    //endregion

    public String getInquiryTransaction(InquiryTransaction inquiry, Context context)
    {
        String url = constant.inquiryUrl;
        String signature = Util.getMD5(constant.merchantCode + inquiry.merchantOrderId + inquiry.paymentAmount + constant.apiKey);

        try
        {
            JSONObject jsonInquiry = new JSONObject();
            jsonInquiry.put("merchantCode",inquiry.merchantCode);
            jsonInquiry.put("paymentAmount",inquiry.paymentAmount);
            jsonInquiry.put("paymentMethod",inquiry.paymentMethod);
            jsonInquiry.put("merchantOrderId",inquiry.merchantOrderId);
            jsonInquiry.put("productDetails",inquiry.productDetails);
            jsonInquiry.put("additionalParam",inquiry.additionalParam);
            jsonInquiry.put("merchantUserInfo",inquiry.merchantUserInfo);
            jsonInquiry.put("callbackUrl",inquiry.callbackUrl);
            jsonInquiry.put("returnUrl",inquiry.returnUrl);
            jsonInquiry.put("signature",signature);

            RequestBody body = RequestBody.create(JSON, jsonInquiry.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try
                    {
                        jsonData = response.body().string();
                        Log.i(TAG, "Respons " + jsonData);
                    }
                    catch (IOException e)
                    {
                        Log.e(TAG,"Response " + e);
                    }
                }
            });
        }
        catch (JSONException e)
        {
            Log.e(TAG,"Response " + e);
        }
        return jsonData;
    }
}
