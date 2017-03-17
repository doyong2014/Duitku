package com.android.duitku.paymentmethod.presenter;

import com.android.duitku.paymentmethod.view.PaymentMethodView;

/**
 * Created by latif on 12/3/15.
 */
public class PaymentMethodPresenterImpl implements PaymentMethodPresenter {
    private PaymentMethodView mPaymentMethodView;

    public PaymentMethodPresenterImpl(PaymentMethodView paymentMethodView) {
        this.mPaymentMethodView = paymentMethodView;
    }

    @Override
    public void onInitView() {
        mPaymentMethodView.initView();
    }

    @Override
    public void onPopulateList() {
        mPaymentMethodView.populateListView();
    }
}
