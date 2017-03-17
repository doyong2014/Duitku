package com.android.duitku.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesHelper {

    public static void putBoolean(Context context, String key, boolean value) {
        getEditor(context).putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return getPreferences(context).getBoolean(key, defValue);
    }

    public static void putInt(Context context, String key, int value) {
        getEditor(context).putInt(key, value).commit();

    }

    public static int getInt(Context context, String key, int defValue) {
        return getPreferences(context).getInt(key, defValue);
    }

    public static void putString(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();
    }

    public static String getString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }

    public static void putFloat(Context context, String key, float value) {
        getEditor(context).putFloat(key, value).commit();
    }

    public static float getFloat(Context context, String key, float defValue) {
        return getPreferences(context).getFloat(key, defValue);
    }

    public static void putLong(Context context, String key, long value) {
        getEditor(context).putLong(key, value).commit();
    }

    public static long getLong(Context context, String key, long defValue) {
        return getPreferences(context).getLong(key, defValue);
    }

    public static void removePreference(Context context, String key) {
        getEditor(context).remove(key).commit();
    }

    private static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    public static SharedPreferences getPreferences(Context context, String preferencesName){
        return context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
    }
}
