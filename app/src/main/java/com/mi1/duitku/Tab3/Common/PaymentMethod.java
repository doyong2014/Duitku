package com.mi1.duitku.Tab3.Common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ardian on 11/03/2016.
 */
public class PaymentMethod implements Parcelable
{
    private String mName;
    private String mCode;
    private String mFee;

    public PaymentMethod()
    {

    }

    protected PaymentMethod(Parcel in) {
        mName = in.readString();
        mCode = in.readString();
        mFee = in.readString();
    }

    public static final Creator<PaymentMethod> CREATOR = new Creator<PaymentMethod>() {
        @Override
        public PaymentMethod createFromParcel(Parcel in) {
            return new PaymentMethod(in);
        }

        @Override
        public PaymentMethod[] newArray(int size) {
            return new PaymentMethod[size];
        }
    };

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public String getFee() {
        return mFee;
    }

    public void setFee(String fee) {
        mFee = fee;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeString(this.mCode);
        dest.writeString(this.mFee);
    }
}
