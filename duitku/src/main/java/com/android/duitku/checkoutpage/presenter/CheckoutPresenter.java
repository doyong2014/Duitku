package com.android.duitku.checkoutpage.presenter;

import com.android.duitku.model.Inquiry;

/**
 * Created by latif on 12/8/15.
 */
public interface CheckoutPresenter {
    void onInitView();

    void onInquiryData(Inquiry inquiry);
}
