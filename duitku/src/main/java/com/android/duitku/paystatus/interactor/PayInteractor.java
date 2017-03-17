package com.android.duitku.paystatus.interactor;

import com.android.duitku.model.Pay;
import com.android.duitku.model.PayResponse;

import rx.Subscriber;

/**
 * Created by latifalbar on 11/17/2015.
 */
public interface PayInteractor {
    void fetchPayResponse(Subscriber<PayResponse> subscriber, Pay pay);
}
