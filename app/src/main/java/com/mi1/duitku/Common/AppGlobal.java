package com.mi1.duitku.Common;

/**
 * Created by owner on 3/17/2017.
 */

public class AppGlobal {

    public static UserInfo _userInfo = null;
    public static UserDetailInfo _userDetailInfo = null;
    public static int qbID;

    public static void initData() {
        _userInfo = new UserInfo();
        _userDetailInfo = new UserDetailInfo();
    }
}
