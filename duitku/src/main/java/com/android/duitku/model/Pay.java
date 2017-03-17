package com.android.duitku.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by latifalbar on 11/13/2015.
 */
public class Pay implements Parcelable {

    private String action;

    private int userId;

    private String orderId;

    private String merchantCode;

    private String sign;

    public Pay(){

    }

    protected Pay(Parcel in) {
        this.action = in.readString();
        this.userId = in.readInt();
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

    public int getUserId() {
        return userId;
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

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public static final Creator<Pay> CREATOR = new Creator<Pay>() {
        @Override
        public Pay createFromParcel(Parcel in) {
            return new Pay(in);
        }

        @Override
        public Pay[] newArray(int size) {
            return new Pay[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.action);
        dest.writeInt(this.userId);
        dest.writeString(this.orderId);
        dest.writeString(this.merchantCode);
        dest.writeString(this.sign);
    }
}
