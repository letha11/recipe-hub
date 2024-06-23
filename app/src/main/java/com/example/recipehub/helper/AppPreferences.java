package com.example.recipehub.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    private static SharedPreferences mPrefs;
    private static SharedPreferences.Editor mPrefsEditor;

    public static boolean isUserLoggedIn(Context ctx) {
        mPrefs = ctx.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE);
        return mPrefs.getBoolean("id_logged_in", false);
    }

    public static void setUserLoggedIn(Context ctx, Boolean value) {
        mPrefs = ctx.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putBoolean("id_logged_in", value);
        mPrefsEditor.apply();
    }

    public static String getUserName(Context ctx) {
        mPrefs = ctx.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE);
        return mPrefs.getString("username", "");
    }

    public static void setUserName(Context ctx, String value) {
        mPrefs = ctx.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE);
        setUserLoggedIn(ctx, true);
        mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putString("username", value);
        mPrefsEditor.apply();
    }
}
