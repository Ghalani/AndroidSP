package com.ghalani.ghalani.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.ghalani.ghalani.activity.MainActivity;
import com.ghalani.ghalani.activity.SmsActivity;
import com.ghalani.ghalani.app.Config;
import com.ghalani.ghalani.app.MyApplication;
import com.ghalani.ghalani.helper.PrefManager;
import com.ghalani.ghalani.helper.TextLogHelper;

/**
 * Created by Ravi on 04/04/15.
 */
public class HttpService extends IntentService {

    private static String TAG = HttpService.class.getSimpleName();

    public HttpService() {
        super(HttpService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String otp = intent.getStringExtra("otp");
            String phone = intent.getStringExtra("phone");
            verifyOtp(otp, phone);
        }
    }

    /**
     * Posting the OTP to server and activating the user
     *
     * @param otp otp received in the SMS
     */
    private void verifyOtp(final String otp, final String phone) {
        try {
            JSONObject inParams = new JSONObject();
            JSONObject params = new JSONObject();
            inParams.put("pin", otp);
            inParams.put("phone", phone);
            params.put("sp_session", inParams);
            final String mRequestBody = params.toString();

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    Config.URL_VERIFY_OTP, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        Log.d(TAG, response.toString());
                        JSONObject responseObj = new JSONObject(response);
                        TextLogHelper.log("JSON: " + responseObj.toString());
                        try{
                            String error = responseObj.getString("error");
                            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                        }catch (Exception e) {
                            String message = responseObj.getString("message");
                            // parsing the user profile information
                            JSONObject profileObj = responseObj.getJSONObject("profile");

                            /*String name = profileObj.getString("name");
                            String email = profileObj.getString("email");*/
                            String mobile = profileObj.getString("phone");
                            String accessToken = profileObj.getString("access_token");
                            String id = String.valueOf(profileObj.getInt("id"));

                            PrefManager pref = new PrefManager(getApplicationContext());
//                            pref.createLogin(name, email, mobile);
                            pref.createLogin(mobile, accessToken, id);

                            Intent intent = new Intent(HttpService.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }
                    }catch (JSONException e){
                        Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                mRequestBody, "utf-8");
                        return null;
                    }
                }
            };
            // Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(strReq);

        }catch(JSONException e){}

    }
}
