package com.ghalani.ghalani.app;

/**
 * Created by Ravi on 08/07/15.
 */
public class Config {
    // server URL configuration
    public static final String URL_ROOT = "http://10.248.83.71:3000/api/v1";
    //public static final String URL_ROOT = "http://10.0.2.2:3000/api/v1";
    public static final String URL_REQUEST_SMS = URL_ROOT+"/sp_sessions/new";
    public static final String URL_VERIFY_OTP = URL_ROOT+"/sp_sessions/";

    // SMS provider identification
    // It should match with your SMS gateway origin
    // You can use  MSGIND, TESTER and ALERTS as sender ID
    // If you want custom sender Id, approve MSG91 to get one
    public static final String SMS_ORIGIN = "ANHIVE";

    // special character to prefix the otp. Make sure this character appears only once in the sms
    public static final String OTP_DELIMITER = ":";
}
