package com.android.duitku.checkoutpage.interactor;

import com.android.duitku.model.Inquiry;
import com.android.duitku.model.InquiryResponse;
import com.android.duitku.network.NetworkManager;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by latif on 12/10/15.
 */
public class CheckoutInteractorImpl implements CheckoutInteractor {

    private Observable<NetworkManager> networkManagerObservable;

    public CheckoutInteractorImpl(Observable<NetworkManager> networkManager) {
        this.networkManagerObservable = networkManager;
    }

    @Override
    public void fetchInquiry(Subscriber<InquiryResponse> inquiryResponseSubscriber, Inquiry inquiry) {
        networkManagerObservable.subscribe(networkManager -> {
            networkManager.getInquiryObservable(inquiry).subscribe(inquiryResponseSubscriber);
        });
    }
}
