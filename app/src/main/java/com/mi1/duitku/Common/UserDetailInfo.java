package com.mi1.duitku.Common;

/**
 * Created by owner on 3/24/2017.
 */

public class UserDetailInfo {

    public UserExternalService userExternalServices[];
    public String birthday;
    public String gender;
    public String addr;
    public String addrDetail;
    public String zipcode;

    public UserDetailInfo() {}

    public static class UserExternalService {
        public String id;
        public String name;

    }
}