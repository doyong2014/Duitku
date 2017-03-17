package com.android.duitku.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by latifalbar on 11/11/2015.
 */
public class TransactionStatusResponse implements Parcelable {

    private String statusCode;

    private String statusMessage;

    private String reference;

    private int amount;


    protected TransactionStatusResponse(Parcel in) {
        this.statusCode = in.readString();
        this.statusMessage = in.readString();
        this.reference = in.readString();
        this.amount = in.readInt();
    }

    public int getAmount() {
        return amount;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getReference() {
        return reference;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public static final Creator<TransactionStatusResponse> CREATOR = new Creator<TransactionStatusResponse>() {
        @Override
        public TransactionStatusResponse createFromParcel(Parcel in) {
            return new TransactionStatusResponse(in);
        }

        @Override
        public TransactionStatusResponse[] newArray(int size) {
            return new TransactionStatusResponse[size];
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
        dest.writeString(this.reference);
        dest.writeInt(this.amount);
    }

}
