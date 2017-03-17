package com.mi1.duitku.Tab3;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ardian on 11/03/2016.
 */
public class PaymentMethod {

    private String name;
    private String pgType;
    private String pgCode;
    private String pgFee;

    public PaymentMethod(String name, String pgType, String pgCode, String pgFee) {
        this.name = name;
        this.pgType = pgType;
        this.pgCode = pgCode;
        this.pgFee = pgFee;
    }
}
