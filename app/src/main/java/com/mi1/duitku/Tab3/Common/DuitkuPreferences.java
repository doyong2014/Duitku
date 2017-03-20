package com.mi1.duitku.Tab3.Common;

/**
 * Created by owner on 3/16/2017.
 */

public class DuitkuPreferences {

    public String inquiry;
    public String inquiryResponse;
    public String isLogin;
    public String login;
    public String loginResponse;
    public String bankUrl;
    public boolean usingDuitku;
    public boolean lastPaymentFailed;
    public String imageName;
    public double adminFee;
    public String usingBbm;
    public String payResponse;

    public void init() {
        login = "";
        loginResponse = "";
        payResponse = "";
        usingDuitku = false;
        imageName = "";
        adminFee = 0;
        inquiryResponse = "";
        inquiry = "";
        lastPaymentFailed = false;
    }
}
