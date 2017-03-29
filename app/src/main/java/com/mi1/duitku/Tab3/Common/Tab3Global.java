package com.mi1.duitku.Tab3.Common;

import com.mi1.duitku.Tab3.Common.CPPOBProductParent;

import java.util.ArrayList;

/**
 * Created by owner on 3/17/2017.
 */

public class Tab3Global {

    public static CPPOBProductParent _productPayment = null;
    public static CPPOBProductParent _productPurchase = null;
    public static DuitkuPreferences _duitkuPreferences;
    public static ArrayList<CashInfo.TransactionList> _cashInData = null;
    public static DataInfo _cashInInfo = null;
    public static ArrayList<CashInfo.TransactionList> _cashOutData = null;
    public static DataInfo _cashOutInfo = null;

    public static void initData(){

        _cashInData = new ArrayList<CashInfo.TransactionList>();
        _cashInInfo = new DataInfo();

        _cashOutData = new ArrayList<CashInfo.TransactionList>();
        _cashOutInfo = new DataInfo();

    }
}
