package com.ghalani.ghalani.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.ghalani.ghalani.app.Config;
import com.ghalani.ghalani.helper.PrefManager;
import com.ghalani.ghalani.helper.TextLogHelper;
import com.ghalani.ghalani.service.HttpService;

/**
 * Created by Ravi on 09/07/15.
 */
public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = SmsReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        TextLogHelper.log("SMS RECEIVED");
        PrefManager pref = new PrefManager(context);
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object aPdusObj : pdusObj) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                    String senderAddress = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();

                    Log.e(TAG, "Received SMS: " + message + ", Sender: " + senderAddress);

                    // if the SMS is not from our gateway, ignore the message
                    if (!senderAddress.toLowerCase().contains(Config.SMS_ORIGIN.toLowerCase())) {
                        Log.e(TAG, "SMS is not for our app!");
                        return;
                    }

                    // verification code from sms
                    String verificationCode = getVerificationCode(message);

                    Log.e(TAG, "OTP received: " + verificationCode);

                    Intent hhtpIntent = new Intent(context, HttpService.class);
                    hhtpIntent.putExtra("otp", verificationCode);
                    hhtpIntent.putExtra("phone", pref.getMobileNumber());
                    context.startService(hhtpIntent);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Getting the OTP from sms message body
     * ':' is the separator of OTP from the message
     *
     * @param message
     * @return
     */
    private String getVerificationCode(String message) {
        String code = null;
        int index = message.indexOf(Config.OTP_DELIMITER) + Config.OTP_DELIMITER.length();

        if (index != -1) {
            int start = index + 1;
            int length = 6;
            code = message.substring(start, start + length);
            return code;
        }

        return code;
    }
}
