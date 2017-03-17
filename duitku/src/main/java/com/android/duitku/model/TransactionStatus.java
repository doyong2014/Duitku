package com.android.duitku.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by latifalbar on 11/13/2015.
 */
public class TransactionStatus implements Parcelable {

    private String action;

    private String orderId;

    private String merchantCode;

    private String sign;

    public TransactionStatus(){

    }

    protected TransactionStatus(Parcel in) {
        this.action = in.readString();
        this.orderId = in.readString();
        this.merchantCode = in.readString();
        this.sign = in.readString();
    }

    public String getAction() {
        return action;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getSign() {
        return sign;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public static final Creator<TransactionStatus> CREATOR = new Creator<TransactionStatus>() {
        @Override
        public TransactionStatus createFromParcel(Parcel in) {
            return new TransactionStatus(in);
        }

        @Override
        public TransactionStatus[] newArray(int size) {
            return new TransactionStatus[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.action);
        dest.writeString(this.orderId);
        dest.writeString(this.merchantCode);
        dest.writeString(this.sign);
    }
}
