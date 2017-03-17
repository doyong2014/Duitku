package com.android.duitku.base;

import com.android.duitku.checkoutpage.interactor.CheckoutInteractor;
import com.android.duitku.checkoutpage.interactor.CheckoutInteractorImpl;
import com.android.duitku.checkoutpage.presenter.CheckoutPresenter;
import com.android.duitku.checkoutpage.presenter.CheckoutPresenterImpl;
import com.android.duitku.checkoutpage.view.CheckoutView;
import com.android.duitku.inquiry.presenter.InquiryPresenter;
import com.android.duitku.inquiry.presenter.InquiryPresenterImpl;
import com.android.duitku.inquiry.view.InquiryActivityView;
import com.android.duitku.login.interactor.LoginDuitkuInteractor;
import com.android.duitku.login.interactor.LoginDuitkuInteractorImpl;
import com.android.duitku.login.presenter.LoginDuitkuPresenter;
import com.android.duitku.login.presenter.LoginDuitkuPresenterImpl;
import com.android.duitku.login.view.LoginDuitkuView;
import com.android.duitku.network.NetworkManager;
import com.android.duitku.paystatus.interactor.PayInteractor;
import com.android.duitku.paystatus.interactor.PayInteractorImpl;
import com.android.duitku.paystatus.presenter.PayPresenter;
import com.android.duitku.paystatus.presenter.PayPresenterImpl;
import com.android.duitku.paymentmethod.presenter.PaymentMethodPresenter;
import com.android.duitku.paymentmethod.presenter.PaymentMethodPresenterImpl;
import com.android.duitku.paymentmethod.view.PaymentMethodView;
import com.android.duitku.paystatus.view.PayStatusView;
import com.android.duitku.redirectbankpage.presenter.RedirectBankPagePresenter;
import com.android.duitku.redirectbankpage.presenter.RedirectWebPagePresenterImpl;
import com.android.duitku.redirectbankpage.view.RedirectBankPageView;

import rx.Observable;

/**
 * Created by latifalbar on 11/12/2015.
 */
public class PresenterFactory {


    public static InquiryPresenter createInquiryFactory(InquiryActivityView mainActivityView){
        InquiryPresenter inquiryPresenter = new InquiryPresenterImpl(mainActivityView);
        return inquiryPresenter;
    }

    public static LoginDuitkuPresenter createLoginFactory(LoginDuitkuView loginDuitkuView,Observable<NetworkManager> networkManagerObservable){
        LoginDuitkuInteractor loginDuitkuInteractor = new LoginDuitkuInteractorImpl(networkManagerObservable);
        LoginDuitkuPresenter loginDuitkuPresenter = new LoginDuitkuPresenterImpl(loginDuitkuInteractor,loginDuitkuView);
        return loginDuitkuPresenter;
    }

    public static PayPresenter createPayFactory(PayStatusView payStatusView,Observable<NetworkManager> networkManagerObservable){
        PayInteractor payInteractor = new PayInteractorImpl(networkManagerObservable);
        PayPresenter payPresenter = new PayPresenterImpl(payInteractor,payStatusView);
        return payPresenter;
    }

    public static PaymentMethodPresenter createPaymentMethodFactory(PaymentMethodView paymentMethodView){
        PaymentMethodPresenter paymentMethodPresenter = new PaymentMethodPresenterImpl(paymentMethodView);
        return paymentMethodPresenter;
    }

    public static RedirectBankPagePresenter createBankPagePresenter(RedirectBankPageView redirectBankPageView){
        RedirectBankPagePresenter redirectBankPagePresenter = new RedirectWebPagePresenterImpl(redirectBankPageView);
        return  redirectBankPagePresenter;
    }

    public static CheckoutPresenter createCheckoutPresenter(CheckoutView checkoutView, Observable<NetworkManager> networkManager){
        CheckoutInteractor checkoutInteractor = new CheckoutInteractorImpl(networkManager);
        CheckoutPresenter checkoutPresenter = new CheckoutPresenterImpl(checkoutInteractor,checkoutView);
        return checkoutPresenter;
    }



}
