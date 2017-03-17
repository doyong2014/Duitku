package com.android.duitku.paystatus.view;

import com.android.duitku.model.PayResponse;

/**
 * Created by latif on 12/10/15.
 */
public interface PayStatusView {
    void initView();

    void emitPayResponse(PayResponse payResponse);

    void emitResponseFromRedirect();

}
