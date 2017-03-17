package com.mi1.duitku.Tab3.Common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by WORYA on 3/22/2016.
 */
public class CPaymentInfo implements Parcelable {

    public String idPelanggan1;
    public String idPelanggan2;
    public String idPelanggan3;
    public double biayaAdmin;
    public double nominal;
    public String namaPelanggan;
    public String namaOperator;
    public String dayaPln;
    public String ref1;
    public String ref2;

    public CPaymentInfo()
    {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.idPelanggan1);
        dest.writeString(this.idPelanggan2);
        dest.writeString(this.idPelanggan3);
        dest.writeDouble(this.biayaAdmin);
        dest.writeDouble(this.nominal);
        dest.writeString(this.namaPelanggan);
        dest.writeString(this.namaOperator);
        dest.writeString(this.dayaPln);
        dest.writeString(this.ref1);
        dest.writeString(this.ref2);
    }

    protected CPaymentInfo(Parcel in) {
        this.idPelanggan1 = in.readString();
        this.idPelanggan2 = in.readString();
        this.idPelanggan3 = in.readString();
        this.biayaAdmin = in.readDouble();
        this.nominal = in.readDouble();
        this.namaPelanggan = in.readString();
        this.namaOperator = in.readString();
        this.dayaPln = in.readString();
        this.ref1 = in.readString();
        this.ref2 = in.readString();
    }

    public static final Parcelable.Creator<CPaymentInfo> CREATOR = new Parcelable.Creator<CPaymentInfo>() {
        @Override
        public CPaymentInfo createFromParcel(Parcel source) {
            return new CPaymentInfo(source);
        }

        @Override
        public CPaymentInfo[] newArray(int size) {
            return new CPaymentInfo[size];
        }
    };
}
