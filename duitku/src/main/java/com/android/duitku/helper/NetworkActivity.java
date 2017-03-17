package com.android.duitku.helper;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

import com.android.duitku.network.NetworkManager;
import com.android.duitku.network.NetworkServiceProvider;

import java.util.ArrayList;
import java.util.Iterator;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by latifalbar on 11/12/2015.
 */
public class NetworkActivity extends AppCompatActivity implements NetworkServiceProvider, Observable.OnSubscribe<NetworkManager> {

    private Observable<NetworkManager> mNetworkServiceObservable;

    private Subscriber networkSubscriber;

    private ArrayList<Subscriber> networkSubscribers = new ArrayList<>();

    private NetworkManager mNetworkManager;

    private boolean mBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNetworkServiceObservable = Observable.create(this).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<NetworkManager> getNetworkManager() {
        return mNetworkServiceObservable;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, NetworkManager.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            networkSubscriber = null;
            unbindService(mConnection);
            mBound = false;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            NetworkManager.LocalBinder binder = (NetworkManager.LocalBinder) service;
            mNetworkManager = binder.getService();
            triggerServiceActive();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    public void call(Subscriber<? super NetworkManager> subscriber) {
        networkSubscribers.add(subscriber);
        if (mBound) {
            triggerServiceActive();
        }
    }

    private synchronized void triggerServiceActive() {
        Iterator<Subscriber> iterator = networkSubscribers.iterator();
        while (iterator.hasNext()) {
            Subscriber subscriber = iterator.next();
            subscriber.onNext(mNetworkManager);
            subscriber.onCompleted();
            iterator.remove();
        }
    }
}
