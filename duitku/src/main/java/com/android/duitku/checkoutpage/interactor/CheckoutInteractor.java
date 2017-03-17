package com.android.duitku.checkoutpage.interactor;

import com.android.duitku.model.Inquiry;
import com.android.duitku.model.InquiryResponse;

import rx.Subscriber;

/**
 * Created by latif on 12/10/15.
 */
public interface CheckoutInteractor {

    void fetchInquiry(Subscriber<InquiryResponse> inquiryResponseSubscriber, Inquiry inquiry);
}
