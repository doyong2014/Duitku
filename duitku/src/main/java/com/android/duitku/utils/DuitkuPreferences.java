package com.android.duitku.utils;

import android.content.Context;

/**
 * Created by latif on 12/9/15.
 */
public class DuitkuPreferences {


    private Context mContext;

    private final String INQUIRY = "inquiry";

    private final String INQUIRYRESPONSE = "inquiry_response";

    private final String ISLOGIN = "is_login";

    private final String LOGIN = "login";

    private final String LOGINRESPONSE = "login_response";

    private final String BANKURL = "bank_url";

    private final String USINGDUITKU = "using_duitku";

    private final String LASTPAYMENTFAILED = "last_payment_failed";

    private final String IMAGENAME = "image_name";

    private final String ADMINFEE = "admin_fee";

    private final String USINGBBM = "using_bbm";

    private final String PAYRESPONSE = "payresponse";

    public DuitkuPreferences(Context context) {
        this.mContext = context;
    }

    public void saveInquiry(String inquiry) {
        PreferencesHelper.putString(mContext, INQUIRY, inquiry);
    }

    public String getInquiry() {
        return PreferencesHelper.getString(mContext, INQUIRY, "");
    }

    public void saveInquiryResponse(String inquiryResponse) {
        PreferencesHelper.putString(mContext, INQUIRYRESPONSE, inquiryResponse);
    }
    public String getInquiryResponse() {
        return PreferencesHelper.getString(mContext, INQUIRYRESPONSE, "");
    }
    public void saveBankUrl(String bankUrl) {
        PreferencesHelper.putString(mContext, BANKURL, bankUrl);
    }

    public String getBankUrl() {
        return PreferencesHelper.getString(mContext, BANKURL, "");
    }

    public void setUsingDuitku(boolean isusingduitku) {
        PreferencesHelper.putBoolean(mContext, USINGDUITKU, isusingduitku);

    }

    public boolean isUsingDuitku() {
        return PreferencesHelper.getBoolean(mContext, USINGDUITKU, false);
    }

    public void setIsLogin(boolean isLogin) {
        PreferencesHelper.putBoolean(mContext, ISLOGIN, isLogin);
    }

    public boolean isLogin() {
        return PreferencesHelper.getBoolean(mContext, ISLOGIN, false);
    }

    public void saveLogin(String login) {
        PreferencesHelper.putString(mContext, LOGIN, login);
    }

    public String getLogin() {
        return PreferencesHelper.getString(mContext, LOGIN, "");
    }

    public void saveLoginResponse(String loginResponse) {
        PreferencesHelper.putString(mContext, LOGINRESPONSE, loginResponse);
    }

    public String getLoginResponse() {
        return PreferencesHelper.getString(mContext, LOGINRESPONSE, "");
    }


    public void setIsLastPaymentFailed(boolean isLastPaymentFailed) {
        PreferencesHelper.putBoolean(mContext, LASTPAYMENTFAILED, isLastPaymentFailed);
    }

    public boolean isLastPaymentFailed() {
        return PreferencesHelper.getBoolean(mContext, LASTPAYMENTFAILED, false);
    }

    public void saveImageName(String imageName) {
        PreferencesHelper.putString(mContext, IMAGENAME, imageName);
    }

    public String getImagename() {
        return PreferencesHelper.getString(mContext, IMAGENAME, "");
    }

    public void saveAdminFee(int adminfee){
        PreferencesHelper.putInt(mContext,ADMINFEE,adminfee);
    }

    public int getAdminFee(){
        return PreferencesHelper.getInt(mContext,ADMINFEE,0);
    }

    public void setUsingBBM(boolean isUsingBBM){
        PreferencesHelper.putBoolean(mContext,USINGBBM,isUsingBBM);
    }

    public boolean getInfoBBMUsage(){
        return PreferencesHelper.getBoolean(mContext,USINGBBM,false);
    }

    public void savePayResponse(String payResponse) {
        PreferencesHelper.putString(mContext,PAYRESPONSE,payResponse);
    }

    public String getPayResponse(){
        return PreferencesHelper.getString(mContext,PAYRESPONSE,"");
    }
}

