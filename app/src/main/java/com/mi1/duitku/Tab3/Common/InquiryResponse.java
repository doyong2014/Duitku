package com.mi1.duitku.Tab3.Common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by latifalbar on 11/11/2015.
 */
public class InquiryResponse implements Parcelable {

    private String statusCode;

    private String statusMessage;

    private String trxId;

    private String orderId;

    private String price;

    private String merchantName;



    protected InquiryResponse(Parcel in) {

        this.statusCode = in.readString();
        this.statusMessage = in.readString();
        this.trxId = in.readString();
        this.orderId = in.readString();
        this.price = in.readString();
        this.merchantName = in.readString();
    }


    public String getMerchantName() {
        return merchantName;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getPrice() {
        return price;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getTrxId() {
        return trxId;
    }

    public static final Creator<InquiryResponse> CREATOR = new Creator<InquiryResponse>() {
        @Override
        public InquiryResponse createFromParcel(Parcel in) {
            return new InquiryResponse(in);
        }

        @Override
        public InquiryResponse[] newArray(int size) {
            return new InquiryResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.statusCode);
        dest.writeString(this.statusMessage);
        dest.writeString(this.trxId);
        dest.writeString(this.orderId);
        dest.writeString(this.price);
        dest.writeString(this.merchantName);
    }
}
