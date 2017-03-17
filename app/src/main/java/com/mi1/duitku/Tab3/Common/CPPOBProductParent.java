package com.mi1.duitku.Tab3.Common;

import android.os.Parcel;
import android.os.Parcelable;

import com.mi1.duitku.Common.CommonFunction;

import java.util.ArrayList;

/**
 * Created by easten on 28.04.16.
 */
public class CPPOBProductParent implements Parcelable {
    public int id;
    public String name;
    public ArrayList<CPPOBProduct> productList;
    public ArrayList<CPPOBProductParent> child;

    public CPPOBProductParent() {
        this.productList = new ArrayList<>();
        this.child = new ArrayList<>();
    }

    @Override
    public String toString() {
        return CommonFunction.CapitalizeSentences(name.toString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeTypedList(productList);
        dest.writeTypedList(child);
    }

    protected CPPOBProductParent(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.productList = in.createTypedArrayList(CPPOBProduct.CREATOR);
        this.child = in.createTypedArrayList(CPPOBProductParent.CREATOR);
    }

    public static final Creator<CPPOBProductParent> CREATOR = new Creator<CPPOBProductParent>() {
        @Override
        public CPPOBProductParent createFromParcel(Parcel source) {
            return new CPPOBProductParent(source);
        }

        @Override
        public CPPOBProductParent[] newArray(int size) {
            return new CPPOBProductParent[size];
        }
    };
}
