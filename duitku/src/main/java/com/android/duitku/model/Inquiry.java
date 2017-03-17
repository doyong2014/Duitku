package com.android.duitku.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by latifalbar on 11/12/2015.
 */
public class Inquiry implements Parcelable {

    private String action;

    private String merchantCode;

    private String orderId;

    private String amount;

    private String productDetail;

    private String additionalParam;

    private String sign;

    private String amoutnAfterFee;

    public Inquiry(){

    }

    public Inquiry(Parcel in) {
        this.action = in.readString();
        this.merchantCode = in.readString();
        this.orderId = in.readString();
        this.amount = in.readString();
        this.productDetail = in.readString();
        this.additionalParam = in.readString();
        this.sign = in.readString();
        this.amoutnAfterFee = in.readString();
    }

    public String getAction() {
        return action;
    }

    public String getAdditionalParam() {
        return additionalParam;
    }

    public String getAmount() {
        return amount;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getProductDetail() {
        return productDetail;
    }

    public String getSign() {
        return sign;
    }

    public String getAmountAfterFee(){return amoutnAfterFee;}

    public void setAction(String action) {
        this.action = action;
    }

    public void setAdditionalParam(String additionalParam) {
        this.additionalParam = additionalParam;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setProductDetail(String productDetail) {
        this.productDetail = productDetail;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setAmountAfterFee(String amountAfterFee){this.amoutnAfterFee = amountAfterFee;}

    public static final Creator<Inquiry> CREATOR = new Creator<Inquiry>() {
        @Override
        public Inquiry createFromParcel(Parcel in) {
            return new Inquiry(in);
        }

        @Override
        public Inquiry[] newArray(int size) {
            return new Inquiry[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.action);
        dest.writeString(this.merchantCode);
        dest.writeString(this.orderId);
        dest.writeString(this.amount);
        dest.writeString(this.productDetail);
        dest.writeString(this.additionalParam);
        dest.writeString(this.sign);
        dest.writeString(this.amoutnAfterFee);
    }
}
