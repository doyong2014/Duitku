package com.mi1.duitku.Tab3.Common;

import android.os.Parcel;
import android.os.Parcelable;

import com.mi1.duitku.Common.CommonFunction;

/**
 * Created by easten on 28.04.16.
 */
public class CPPOBProduct implements Parcelable {
    public String productCode;
    public String productName;
    public String productType;
    public double productPrice;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productCode);
        dest.writeString(this.productName);
        dest.writeString(this.productType);
        dest.writeDouble(this.productPrice);
    }

    public CPPOBProduct() {
    }

    protected CPPOBProduct(Parcel in) {
        this.productCode = in.readString();
        this.productName = in.readString();
        this.productType = in.readString();
        this.productPrice = in.readDouble();
    }

    public static final Parcelable.Creator<CPPOBProduct> CREATOR = new Parcelable.Creator<CPPOBProduct>() {
        @Override
        public CPPOBProduct createFromParcel(Parcel source) {
            return new CPPOBProduct(source);
        }

        @Override
        public CPPOBProduct[] newArray(int size) {
            return new CPPOBProduct[size];
        }
    };

    @Override
    public String toString() {
        return CommonFunction.CapitalizeSentences(this.productName);
    }
}
