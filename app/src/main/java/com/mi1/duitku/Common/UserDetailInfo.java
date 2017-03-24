package com.mi1.duitku.Common;

/**
 * Created by owner on 3/24/2017.
 */

public class UserDetailInfo {

    public UserExternalService userExternalServices[];
    public String fullName;
    public String birthday;
    public String gender;
    public String addr;
    public String addrDetail;
    public String zipcode;


    public UserDetailInfo() {}
    public UserDetailInfo(UserExternalService[] userExternalServices, String username, String fullName, String userBalance, String email,
               String phoneNumber, String birthday, String gender, String addr, String addrDetail, String zipcode, String picUrl){
        this.userExternalServices = userExternalServices;
        this.fullName = fullName;
        this.birthday = birthday;
        this.gender = gender;
        this.addr = addr;
        this.addrDetail = addrDetail;
        this.zipcode = zipcode;
    }

    public static class UserExternalService {
        public String id;
        public String name;

        public UserExternalService(String id, String name){
            this.id = id;
            this.name = name;
        }
    }
}