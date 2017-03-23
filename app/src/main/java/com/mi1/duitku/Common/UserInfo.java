package com.mi1.duitku.Common;

public class UserInfo {

	public String token;
	public String email;
	public String userbalance;
	public String vaNumber;
	public String vaNumberPermata;
	public String statusCode;
	public String statusMessage;
	public String name;
	public String phoneNumber;
	public String picUrl;

	public  UserInfo(){

	}

	public UserInfo(String token, String email, String userbalance, String vaNumber, String vaNumberPermata,
					String statusCode, String statusMessage, String name, String phoneNumber, String picUrl){

		this.token = token;
		this.email = email;
		this.userbalance = userbalance;
		this.vaNumber = vaNumber;
		this.vaNumberPermata = vaNumberPermata;
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.picUrl = picUrl;
	}

	public void clear() {
		this.token = "";
		this.email = "";
		this.userbalance = "";
		this.vaNumber = "";
		this.vaNumberPermata = "";
		this.statusCode = "";
		this.statusMessage = "";
		this.name = "";
		this.phoneNumber = "";
		this.picUrl = "";
	}

}
