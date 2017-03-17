package com.android.duitku.login.presenter;

import com.android.duitku.login.interactor.LoginDuitkuInteractor;
import com.android.duitku.login.view.LoginDuitkuView;
import com.android.duitku.model.Inquiry;
import com.android.duitku.model.InquiryResponse;
import com.android.duitku.model.Login;
import com.android.duitku.model.LoginResponse;

import retrofit.RetrofitError;
import rx.Subscriber;

/**
 * Created by latifalbar on 11/16/2015.
 */
public class LoginDuitkuPresenterImpl implements LoginDuitkuPresenter {

    private LoginDuitkuInteractor mLoginDuitkuInteractor;

    private LoginDuitkuView mLoginDuitkuView;

    public LoginDuitkuPresenterImpl(LoginDuitkuInteractor loginDuitkuInteractor, LoginDuitkuView loginDuitkuView) {
        this.mLoginDuitkuInteractor = loginDuitkuInteractor;
        this.mLoginDuitkuView = loginDuitkuView;
    }

    @Override
    public void onInitView() {
        mLoginDuitkuView.initView();
    }

    @Override
    public void onLoginProcess(Login login) {

        mLoginDuitkuInteractor.doLogin(new Subscriber<LoginResponse>(){

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(LoginResponse loginResponse) {
                mLoginDuitkuView.checkLoginResponse(loginResponse);
            }
        },login);
    }

    @Override
    public void onInquiryData(Inquiry inquiry) {
        mLoginDuitkuInteractor.fetchInquiry(new Subscriber<InquiryResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable error) {
                if (error instanceof RetrofitError) {
                    RetrofitError retrofitError = (RetrofitError) error;
                    retrofitError.getMessage();
                }
            }

            @Override
            public void onNext(InquiryResponse inquiryResponse) {
                mLoginDuitkuView.emitInquiryResponse(inquiryResponse);
            }
        }, inquiry);
    }

}
