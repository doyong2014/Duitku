package com.android.duitku.network;

import com.android.duitku.model.InquiryResponse;
import com.android.duitku.model.LoginResponse;
import com.android.duitku.model.PayResponse;
import com.android.duitku.model.TransactionStatusResponse;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by latifalbar on 11/11/2015.
 */
public interface NetworkService {

    @FormUrlEncoded
    @POST("/rbsnewwebservice/api.aspx/")
    Observable<InquiryResponse> getInquiryObservable(@Field("action") String action
            ,@Field("merchantCode") String merchantCode,@Field("orderId") String orderId
            ,@Field("amount")String amount,@Field("productDetail") String productDetail,@Field("additionalParam") String additionalParam
            ,@Field("sign") String sign);

    @FormUrlEncoded
    @POST("/rbsnewwebservice/api.aspx/")
    Observable<LoginResponse> getLoginObservable(@Field("action") String action
            ,@Field("username") String username,@Field("pass") String pass,@Field("merchantCode") String merchantCode
            ,@Field("sign") String sign);

    @FormUrlEncoded
    @POST("/rbsnewwebservice/api.aspx/")
    Observable<PayResponse> getPayObservable(@Field("action") String action,@Field("userId") int userId,@Field("orderId") String orderId
            ,@Field("merchantCode") String merchantCode,@Field("sign") String sign);

    @FormUrlEncoded
    @POST("/rbsnewwebservice/api.aspx/")
    Observable<TransactionStatusResponse> getTransactionStatusObservable(@Field("action") String action
            ,@Field("orderId") String orderId,@Field("merchantCode") String merchantCode,@Field("sign") String sign);


}
