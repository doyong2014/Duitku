package com.android.duitku.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by latifalbar on 11/13/2015.
 */
public class Login implements Parcelable {

    private String action;

    private String username;

    private String pass;

    private String merchantCode;

    private String sign;

    public Login(){

    }

    protected Login(Parcel in) {
        this.action = in.readString();
        this.username = in.readString();
        this.pass = in.readString();
        this.merchantCode = in.readString();
        this.sign = in.readString();
    }

    public String getAction() {
        return action;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public String getPass() {
        return pass;
    }

    public String getSign() {
        return sign;
    }

    public String getUsername() {
        return username;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static final Creator<Login> CREATOR = new Creator<Login>() {
        @Override
        public Login createFromParcel(Parcel in) {
            return new Login(in);
        }

        @Override
        public Login[] newArray(int size) {
            return new Login[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.action);
        dest.writeString(this.username);
        dest.writeString(this.pass);
        dest.writeString(this.merchantCode);
        dest.writeString(this.sign);
    }
}
