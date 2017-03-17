package com.android.duitku.network;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.android.duitku.BuildConfig;
import com.android.duitku.model.Inquiry;
import com.android.duitku.model.InquiryResponse;
import com.android.duitku.model.Login;
import com.android.duitku.model.LoginResponse;
import com.android.duitku.model.Pay;
import com.android.duitku.model.PayResponse;
import com.android.duitku.model.TransactionStatus;
import com.android.duitku.model.TransactionStatusResponse;
import com.squareup.okhttp.OkHttpClient;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by latifalbar on 11/11/2015.
 */
public class NetworkManager extends Service {

    private final IBinder mBinder = new LocalBinder();

    private NetworkService mNetworkService;


    @Override
    public IBinder onBind(Intent intent) {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://202.59.166.3").build();//BuildConfig.SERVER_URL).build();
        mNetworkService = restAdapter.create(NetworkService.class);
        return null;//mBinder;
    }


    public Observable<InquiryResponse> getInquiryObservable(Inquiry inquiry) {
        return mNetworkService.getInquiryObservable(inquiry.getAction(), inquiry.getMerchantCode(), inquiry.getOrderId()
                , inquiry.getAmount(), inquiry.getProductDetail(), inquiry.getAdditionalParam(), inquiry.getSign())
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<LoginResponse> getLoginObservable(Login login) {
        return mNetworkService.getLoginObservable(login.getAction(), login.getUsername(), login.getPass(), login.getMerchantCode(),login.getSign())
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<PayResponse> getPayObservable(Pay pay) {
        return mNetworkService.getPayObservable(pay.getAction(),pay.getUserId(),pay.getOrderId(),pay.getMerchantCode(),pay.getSign())
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<TransactionStatusResponse> getTransactionStatusObservable(TransactionStatus transactionStatus) {
        return mNetworkService.getTransactionStatusObservable(transactionStatus.getAction(),transactionStatus.getOrderId()
                ,transactionStatus.getMerchantCode(),transactionStatus.getSign())
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public class LocalBinder extends Binder {
        public NetworkManager getService() {
            return NetworkManager.this;
        }
    }
}
