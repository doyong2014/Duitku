package com.android.duitku.checkoutpage.view;

import com.android.duitku.model.InquiryResponse;

/**
 * Created by latif on 12/8/15.
 */
public interface CheckoutView {
    void initView();

    void emitInquiryResponse(InquiryResponse inquiryResponse);
}
