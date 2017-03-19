package com.mi1.duitku.Tab3.Common;

import android.content.Context;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by easten on 23.11.16.
 */
public class AppsflyerUtil {

    private Context mContext;

    public AppsflyerUtil(Context context) {
        this.mContext = context;
    }

    public void Login(boolean status, String username){
        Map<String, Object> eventValue = new HashMap<String, Object>();
        eventValue.put(AFInAppEventParameterName.SUCCESS, status);
        eventValue.put(AFInAppEventParameterName.CUSTOMER_USER_ID, username);
        AppsFlyerLib.getInstance().trackEvent(mContext, AFInAppEventType.LOGIN, eventValue);
    }

    public void Registration(String regMethod, String customerId){
        Map<String, Object> eventValue = new HashMap<String, Object>();
        eventValue.put(AFInAppEventParameterName.REGSITRATION_METHOD, regMethod);
        eventValue.put(AFInAppEventParameterName.CONTENT_ID, customerId);
        AppsFlyerLib.getInstance().trackEvent(mContext, AFInAppEventType.COMPLETE_REGISTRATION, eventValue);
    }

    public void Purchase(float revenue, String productType, String productName, int qty) {
        Map<String, Object> eventValue = new HashMap<String, Object>();
        eventValue.put(AFInAppEventParameterName.REVENUE, revenue);
        eventValue.put(AFInAppEventParameterName.CONTENT_TYPE, productType);
        eventValue.put(AFInAppEventParameterName.CONTENT_ID, productName);
        eventValue.put(AFInAppEventParameterName.QUANTITY, qty);
        eventValue.put(AFInAppEventParameterName.CURRENCY, "IDR");
    }
}
