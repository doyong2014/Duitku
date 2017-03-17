package com.android.duitku.login.interactor;

import com.android.duitku.model.Inquiry;
import com.android.duitku.model.InquiryResponse;
import com.android.duitku.model.Login;
import com.android.duitku.model.LoginResponse;
import com.android.duitku.model.Pay;
import com.android.duitku.model.PayResponse;

import rx.Subscriber;

/**
 * Created by latifalbar on 11/16/2015.
 */
public interface LoginDuitkuInteractor {

    void doLogin(Subscriber<LoginResponse> subscriber,Login login);

    void fetchInquiry(Subscriber<InquiryResponse> inquiryResponseSubscriber, Inquiry inquiry);
}
