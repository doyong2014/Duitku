package com.android.duitku.login.interactor;

import com.android.duitku.model.Inquiry;
import com.android.duitku.model.InquiryResponse;
import com.android.duitku.model.Login;
import com.android.duitku.model.LoginResponse;
import com.android.duitku.model.Pay;
import com.android.duitku.model.PayResponse;
import com.android.duitku.network.NetworkManager;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by latifalbar on 11/16/2015.
 */
public class LoginDuitkuInteractorImpl implements LoginDuitkuInteractor {

    private Observable<NetworkManager> mNetworkManagerObservable;

    public LoginDuitkuInteractorImpl(Observable<NetworkManager> networkManagerObservable) {
        this.mNetworkManagerObservable = networkManagerObservable;
    }

    @Override
    public void doLogin(Subscriber<LoginResponse> subscriber, Login login) {
        mNetworkManagerObservable.subscribe(networkManager -> {
           networkManager.getLoginObservable(login).subscribe(subscriber);
        });
    }

    @Override
    public void fetchInquiry(Subscriber<InquiryResponse> inquiryResponseSubscriber, Inquiry inquiry) {
        mNetworkManagerObservable.subscribe(networkManager -> {
            networkManager.getInquiryObservable(inquiry).subscribe(inquiryResponseSubscriber);
        });
    }
}
