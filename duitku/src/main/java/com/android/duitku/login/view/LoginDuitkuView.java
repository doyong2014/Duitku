package com.android.duitku.login.view;

import com.android.duitku.model.InquiryResponse;
import com.android.duitku.model.LoginResponse;

/**
 * Created by latifalbar on 11/16/2015.
 */
public interface LoginDuitkuView {

    void initView();

    void checkLoginResponse(LoginResponse loginResponse);

    void emitInquiryResponse(InquiryResponse inquiryResponse);
}
