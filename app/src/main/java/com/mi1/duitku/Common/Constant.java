package com.mi1.duitku.Common;

public class Constant {

	//production server
//	private static final String root_url = "https://passport.duitku.com/";
	//sandbox server
	//private static final String root_url = "http://sandbox.duitku.com/";
	//local development
	private static final String root_url = "http://testapidigi1.ngrok.io/";
	public static final String LOGIN_PAGE = root_url + "api/digi1login";
//	public static final String LOGIN_PAGE = root_url + "webapi/api/loginduitku";
	public static final String SIGNUP_PAGE = root_url + "webapi/api/user/registermi1";
	public static final String RECOVERY_PASSWORD_PAGE = root_url + "webapi/api/user/forgotpasswordduitku";
	public static final String VERIFY_CODE_PAGE = root_url + "webapi/api/user/verifyotpphoneduitku";
	public static final String RESUBMIT_CODE_PAGE = root_url + "webapi/api/user/resendotpphoneduitku";
	public static final String NEWS_PAGE = "http://renemo.com/komunitas/api/get_category_posts/?category_slug=DUITKU";
	public static final String PROMOV_PAGE = "http://renemo.com/komunitas/api/get_category_posts/?category_slug=PROMO";
	public static final String EVENTS_PAGE = "http://renemo.com/komunitas/api/get_category_posts/?category_slug=EVENT";
//	public static final String PRODUCT_LIST_PAGE = root_url + "webapi/api/ppob/listv2";
	public static final String PRODUCT_LIST_PAGE = root_url + "api/ppob/listv2";
//	public static final String INQUIRY_BILL_PAGE = root_url +  "webapi/api/ppob/inquiry";
//	public static final String PAYMENT_BILL_PAGE = root_url + "webapi/api/ppob/payment";
//	public static final String PURCHASE_BILL_PAGE = root_url + "webapi/api/ppob/voucher";
	public static final String INQUIRY_BILL_PAGE = root_url +  "api/ppob/inquiry";
	public static final String PAYMENT_BILL_PAGE = root_url + "api/ppob/payment";
	public static final String PURCHASE_BILL_PAGE = root_url + "api/ppob/voucher";
	public static final String BANK_LIST_PAGE = root_url + "webapi/api/topup/paymentmethod";
	public static final String TOPUP_INQUIRY_PAGE = root_url + "webapi/api/topup/request";
	public static final String CASH_IN_PAGE = root_url + "webapi/api/history/historyCashInPaginationDuitku";
	public static final String CASH_OUT_PAGE = root_url + "webapi/api/history/historyCashOutPaginationDuitku";
	public static final String GET_BALANCE_PAGE = root_url + "webapi/api/user/balanceduitku";
	public static final String GET_PROFILE_PAGE = root_url + "webapi/api/user/";
	public static final String UPDATE_PROFILE_PAGE = root_url + "webapi/api/user/updateprofileduitku";
	public static final String UPLOAD_IMAGE_PAGE = root_url + "webapi/api/user/uploadprofile/";
	public static final String CHANGE_PASSWORD_PAGE = root_url + "webapi/api/user/changepasswordduitku";
	public static final String SEARCH_NEWS_PAGE = "http://renemo.com/komunitas/api/get_search_results/?search=";
	public static final String GIFT_PAGE = root_url + "webapi/api/Gift";

	public static final String URLLOGINDIGI1 = "http://103.236.201.138:88/MLM/index.php/api/login";
	public static final String URL_GET_PROFILE_DIGI1 = "http://103.236.201.138:88/MLM/index.php/api/profile";
	public static final String CHANGE_PASSWORD_DIGI1 = "http://103.236.201.138:88/MLM/index.php/api/change_password";
	public static final String FORGOT_PASSWORD_DIGI1 = "http://103.236.201.138:88/MLM/index.php/api/forgot_password";
	public static final String FORGOT_PASSWORD_PROCESS_DIGI1 = "http://103.236.201.138:88/MLM/index.php/api/forgot_process";
	public static final String FORGOT_PASSWORD_NEW_PASSWORD_DIGI1 = "http://103.236.201.138:88/MLM/index.php/api/newpassword";
	public static final String URL_TRANSFER_LP_DIGI1 = "http://103.236.201.138:88/MLM/index.php/api/transfer_lp";
	public static final String URL_TRANSFER_WP_DIGI1 = "http://103.236.201.138:88/MLM/index.php/api/transfer_wp";
	public static final String URL_TRANSFER_PP_DIGI1 = "http://103.236.201.138:88/MLM/index.php/api/transfer_pp";
	public static final String URL_TRANSFER_MP_DIGI1 = "http://103.236.201.138:88/MLM/index.php/api/transfer_mp";
	
	public static final String JSON_STATUS_MESSAGE = "statusMessage";
	public static final String JSON_STATUS_CODE = "statusCode";
	public static final String JSON_PHONE_NUM = "phoneNumber";
	public static final String JSON_BALANCE = "userBalance";
	public static final String JSON_PIC_URL = "profPicUrl";

	public static final String COMMUNITY_CODE = "DK";
	public static final String LOGIN_TYPE = "telpnum";

	public static final String STATUS_INQUIRY = "inquiry";
	public static final String API_KEY = "BB224EFF62CB9207965CD5B320496595";
	public static final String MERCHANT_CODE = "D0011";
	public static final String SUCCESS_WEB_PAYMENT = "success_web_payment";

	public static class PICTURE_TYPE {
		public static final String PROFILE = "PROFILE";
		public static final String KTP = "KTP";
		public static final String NPWP = "NPWP";
	}

	public static class TRANSACTION_TYPE {
		public static final int TOPUP = 1;
		public static final int GAME_CASH_EXCHANGE = 2;
		public static final int GIFT = 3;
		public static final int TRANSACTION_TRANSFER = 4;
		public static final int TRANSACTION_ITEM = 5;
		public static final int GAME_CASH_EXCHANGE_TEMP = 6;
		public static final int EXTERNAL_SERVICE = 7;
		public static final int TRANSACTION_WITHDRAW = 8;
		public static final int TRANSACTION_PPOB = 9;
	}

	public static final String QB_APP_ID = "55347"; //"55347";	//"55550"
	public static final String QB_AUTH_KEY = "9CtR4LDJXnw2dHB"; //"9CtR4LDJXnw2dHB"		"YNPyyuAOL5V-dKD"
	public static final String QB_AUTH_SECRET = "CLyEZMXtnBZ54Xr"; //"CLyEZMXtnBZ54Xr"		"LZQXnbtb3FMKkwv"
	public static final String QB_ACCOUNT_KEY = "Fp1obhGtQmMaA6zxVJyM"; //"Fp1obhGtQmMaA6zxVJyM"	"qRdk1s73exBkUXFqoEWV"
	public static final String QB_ACCOUNT_PASS = "12345678";

}
