package com.android.duitku.paystatus.interactor;

import com.android.duitku.model.Pay;
import com.android.duitku.model.PayResponse;
import com.android.duitku.network.NetworkManager;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by latifalbar on 11/17/2015.
 */
public class PayInteractorImpl implements PayInteractor {

    private Observable<NetworkManager> mNetworkManagerObservable;

    public PayInteractorImpl(Observable<NetworkManager> networkManagerObservable) {
        this.mNetworkManagerObservable = networkManagerObservable;
    }

    @Override
    public void fetchPayResponse(Subscriber<PayResponse> subscriber, Pay pay) {
        mNetworkManagerObservable.subscribe(networkManager -> {
           networkManager.getPayObservable(pay).subscribe(subscriber);
        });
    }
}
