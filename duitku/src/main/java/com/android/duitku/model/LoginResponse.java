package com.android.duitku.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by latifalbar on 11/11/2015.
 */
public class LoginResponse implements Parcelable {

    private String statusCode;

    private String statusMessage;

    private String username;

    private String userBalance;

    private int userId;



    protected LoginResponse(Parcel in) {
        this.statusCode = in.readString();
        this.statusMessage = in.readString();
        this.username = in.readString();
        this.userBalance = in.readString();
        this.userId = in.readInt();
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getUserBalance() {
        return userBalance;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public static final Creator<LoginResponse> CREATOR = new Creator<LoginResponse>() {
        @Override
        public LoginResponse createFromParcel(Parcel in) {
            return new LoginResponse(in);
        }

        @Override
        public LoginResponse[] newArray(int size) {
            return new LoginResponse[size];
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
        dest.writeInt(this.userId);
    }
}
