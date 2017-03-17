package com.android.duitku.checkoutpage.presenter;

import com.android.duitku.checkoutpage.interactor.CheckoutInteractor;
import com.android.duitku.checkoutpage.view.CheckoutView;
import com.android.duitku.model.Inquiry;
import com.android.duitku.model.InquiryResponse;

import retrofit.RetrofitError;
import rx.Subscriber;

/**
 * Created by latif on 12/8/15.
 */
public class CheckoutPresenterImpl implements CheckoutPresenter {

    private CheckoutView mCheckoutView;

    private CheckoutInteractor mCheckoutInteractor;
    public CheckoutPresenterImpl(CheckoutInteractor checkoutInteractor, CheckoutView checkoutView) {
        this.mCheckoutView = checkoutView;
        this.mCheckoutInteractor = checkoutInteractor;
    }

    @Override
    public void onInitView() {
        mCheckoutView.initView();
    }

    @Override
    public void onInquiryData(Inquiry inquiry) {
        mCheckoutInteractor.fetchInquiry(new Subscriber<InquiryResponse>() {
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
                mCheckoutView.emitInquiryResponse(inquiryResponse);
            }
        }, inquiry);

    }
}
