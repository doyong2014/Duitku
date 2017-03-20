package com.mi1.duitku.Tab3.Common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by latifalbar on 11/11/2015.
 */
public class PayResponse implements Parcelable {

    private String statusCode;

    private String statusMessage;

    private String username;

    private String userBalance;

    private String trxId;

    private String orderId;


    protected PayResponse(Parcel in) {
        this.statusCode = in.readString();
        this.statusMessage = in.readString();
        this.username= in.readString();
        this.userBalance= in.readString();
        this.trxId= in.readString();
        this.orderId= in.readString();
    }

    public String getOrderId() {
        return orderId;
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

    public String getUserBalance() {
        return userBalance;
    }

    public String getUsername() {
        return username;
    }

    public static final Creator<PayResponse> CREATOR = new Creator<PayResponse>() {
        @Override
        public PayResponse createFromParcel(Parcel in) {
            return new PayResponse(in);
        }

        @Override
        public PayResponse[] newArray(int size) {
            return new PayResponse[size];
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
        dest.writeString(this.username);
        dest.writeString(this.userBalance);
        dest.writeString(this.trxId);
        dest.writeString(this.orderId);
    }
}
