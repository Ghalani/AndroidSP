package com.ghalani.ghalani.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.ghalani.ghalani.activity.MainActivity;
import com.ghalani.ghalani.activity.SmsActivity;
import com.ghalani.ghalani.helper.TextLogHelper;

/**
 * Created by Ravi on 08/07/15.
 */
public class Config {
    // server URL configuration
    static Context ctx = (MainActivity.rootCtx == null) || (MainActivity.rootCtx.equals(null)) ? SmsActivity.rootCtx : MainActivity.rootCtx;
    static SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(ctx);
    static String server = SP.getString("server", "http://www.ghalani.com/api/v1");
    public static String URL_ROOT = server.length() < 1 ? "http://www.ghalani.com/api/v1" : server;
    //public static final String URL_ROOT = "http://10.248.83.71:3000/api/v1";
    //public static final String URL_ROOT = "http://www.ghalani.com/api/v1";
    //public static final String URL_ROOT = "http://10.0.2.2:3000/api/v1";
    public static final String URL_REQUEST_SMS = URL_ROOT+"/sp_sessions/new";
    public static final String URL_VERIFY_OTP = URL_ROOT+"/sp_sessions/";
    public static final String URL_TEAM_ACTIVITY_REPORT = URL_ROOT+"/team_activity_reports";

    // SMS provider identification
    // It should match with your SMS gateway origin
    // You can use  MSGIND, TESTER and ALERTS as sender ID
    // If you want custom sender Id, approve MSG91 to get one
    public static final String SMS_ORIGIN = "12562977245";

    // special character to prefix the otp. Make sure this character appears only once in the sms
    //public static final String OTP_DELIMITER = ":";
    public static final String OTP_DELIMITER = ", Enter";
}
