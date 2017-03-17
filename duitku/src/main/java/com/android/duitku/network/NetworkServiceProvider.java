package com.android.duitku.network;

import rx.Observable;

/**
 * Created by latifalbar on 11/12/2015.
 */
public interface NetworkServiceProvider {
    Observable<NetworkManager> getNetworkManager();
}
