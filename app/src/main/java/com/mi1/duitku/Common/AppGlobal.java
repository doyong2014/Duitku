package com.mi1.duitku.Common;

import java.util.List;

/**
 * Created by owner on 3/17/2017.
 */

public class AppGlobal {

    public static UserInfo _userInfo = null;
    public static UserDetailInfo _userDetailInfo = null;
    public static PackageDetailInfo _packageDetailInfo = null;
    public static RegisterInfo _registerInfo = null;
    public static List<PackageList> _packageList = null;
    public static CountryList _countryList = null;
    public static int qbID;
    public static String activationCode = null;

    public static void initData() {
        _userInfo = new UserInfo();
        _userDetailInfo = new UserDetailInfo();
        _packageDetailInfo = new PackageDetailInfo();
        _registerInfo = new RegisterInfo();
//        _packageList = new List<PackageList>();
        _countryList = new CountryList();
        activationCode = new String();
    }
}
