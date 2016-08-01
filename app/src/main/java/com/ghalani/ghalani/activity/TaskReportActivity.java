package com.ghalani.ghalani.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.FloatingActionButton;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.ghalani.ghalani.R;
import com.ghalani.ghalani.app.Config;
import com.ghalani.ghalani.app.MyApplication;
import com.ghalani.ghalani.helper.PrefManager;
import com.ghalani.ghalani.helper.TextLogHelper;
import com.ghalani.ghalani.item.task.TeamTask;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskReportActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout reportFormHolder;
    TeamTask tt;
    JSONArray fields;
    ArrayList fieldList;
    FloatingActionButton submitBut;
    PrefManager pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_report);
        Bundle b = getIntent().getExtras();
        tt = (TeamTask) b.getSerializable("team_task_detail");
        submitBut = (FloatingActionButton) findViewById(R.id.submit_report_but);
        try {
            fields = new JSONArray(tt.getFields());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        reportFormHolder = (LinearLayout) findViewById(R.id.report_form_holder);
        appendFields();
        submitBut.setOnClickListener(this);
        pref = new PrefManager(getApplicationContext());
    }

    private void appendFields() {
        int i = 0;
        fieldList = new ArrayList();
        while (i < fields.length()){
            try {
                JSONObject f = fields.getJSONObject(i);
                String name = f.getString("name");
                String field_type = f.getString("field_type");
                Boolean required = f.getBoolean("required");
                if (field_type.equals("text_field")) {
                    final EditText ed = new EditText(this);
                    ed.setHint(name);
                    fieldList.add(ed);
                    //ed.setText("" + i);ed.setInputType(2);ed.setLayoutParams(lparams);
                    reportFormHolder.addView(ed);
                } else{
                    final TextView tv = new TextView(this);
                    tv.setText(name);
                    final CheckBox cb = new CheckBox(this);
                    LinearLayout holder = new LinearLayout(this);

                    holder.addView(cb);
                    holder.addView(tv);

                    fieldList.add(cb);
                    reportFormHolder.addView(holder);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            i++;
        }
    }

    private void readFields(){
        JSONObject hm = new JSONObject();
        int i = 0;
        while (i < fields.length()){
            JSONObject field = new JSONObject();
            String activeField = "";
            try {
                field = fields.getJSONObject(i);
                String val = "";
                activeField = field.getString("field_type");
                if (activeField.equals("text_field")) {
                    val =  ((EditText) fieldList.get(i)).getText().toString();
                    if (field.getBoolean("required") && val.length() < 1)
                        throw new Exception(String.valueOf(i));
                }else{
                    val = String.valueOf(((CheckBox) fieldList.get(i)).isChecked());
                }

                hm.put(field.getString("name"), val);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                //e.printStackTrace();
                TextLogHelper.toast(this, "This field is required", false);
                if (activeField.equals("text_field")) {
                    ((EditText) fieldList.get(i)).setBackgroundColor(Color.argb(55, 40, 0, 0));
                }else{
                    ((CheckBox) fieldList.get(i)).setBackgroundColor(Color.BLUE);
                }
                return;
            }
            i++;
        }

        TextLogHelper.log("OUT: "+ hm.toString());
        //  localhost:3000/api/v1/team_activity_reports?access_token=f1c9e777458aaf45e09f89810c576944
        JSONObject out = new JSONObject();
        JSONObject tsr = new JSONObject();
        try {
            out.put("service_provider_id", pref.getSPId());
            out.put("team_activity_id", tt.getId());
            out.put("report", hm);
            out.put("datetime", (new DateTime()).toString());
            tsr.put("team_activity_report", out);
            sendReport(tsr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*
        {
            "team_activity_report":{
                "service_provider_id": pref.getSPId(),
                "team_activity_id":tt.getId(),
                "report":{
                    "range":"sduys",
                    "house":"sdsjhdjshd"
                },
                "datetime":""
            }
        }
         */
    }

    private void sendReport(final JSONObject out) {
        String url = Config.URL_TEAM_ACTIVITY_REPORT+"?access_token="+pref.getAccessToken();
        TextLogHelper.log(url);
        JsonObjectRequest strReq = new JsonObjectRequest (Request.Method.POST,
                url, out, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject responseObj = response;
                TextLogHelper.log("RESPONSE: " + responseObj.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                TextLogHelper.log("Error: " + error.getMessage());
                TextLogHelper.toast(getApplicationContext(), error.getMessage(), false);
            }
        });

        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_report_but:
                readFields();
                break;
        }
    }
}
