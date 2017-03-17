package com.android.duitku.paystatus.presenter;

import com.android.duitku.model.Pay;
import com.android.duitku.model.PayResponse;
import com.android.duitku.paystatus.interactor.PayInteractor;
import com.android.duitku.paystatus.view.PayStatusView;

import rx.Subscriber;

/**
 * Created by latifalbar on 11/17/2015.
 */
public class PayPresenterImpl implements PayPresenter {

    private PayInteractor mPayInteractor;

    private PayStatusView mPayStatusView;

    public PayPresenterImpl(PayInteractor payInteractor, PayStatusView payStatusView) {
        this.mPayInteractor = payInteractor;
        this.mPayStatusView = payStatusView;
    }

    @Override
    public void onInitView() {
        mPayStatusView.initView();
    }

    @Override
    public void onProsesBayar(Pay pay, Boolean isUsingDuitku) {
        if (isUsingDuitku){
            mPayInteractor.fetchPayResponse(new Subscriber<PayResponse>() {

                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(PayResponse payResponse) {
                    mPayStatusView.emitPayResponse(payResponse);
                }
            }, pay);
        }else {
            mPayStatusView.emitResponseFromRedirect();

        }

    }

}
