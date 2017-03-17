package com.android.duitku.login.presenter;

import com.android.duitku.model.Inquiry;
import com.android.duitku.model.Login;
import com.android.duitku.model.Pay;

/**
 * Created by latifalbar on 11/16/2015.
 */
public interface LoginDuitkuPresenter {
    void onInitView();

    void onLoginProcess(Login login);

    void onInquiryData(Inquiry inquiry);
}
