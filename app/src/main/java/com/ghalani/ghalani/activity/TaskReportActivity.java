package com.ghalani.ghalani.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.FloatingActionButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ghalani.ghalani.R;
import com.ghalani.ghalani.helper.TextLogHelper;
import com.ghalani.ghalani.item.task.TeamTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskReportActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout reportFormHolder;
    TeamTask tt;
    JSONArray fields;
    List<EditText> fieldList;
    FloatingActionButton submitBut;
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
    }

    private void appendFields() {
        int i = 0;
        fieldList = new ArrayList<EditText>();
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
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            i++;
        }
    }

    private void readFields(){
        HashMap<String, String> hm = new HashMap<>();
        int i = 0;
        while (i < fields.length()){
            try {
                JSONObject field = fields.getJSONObject(i);
                String val = "";
                if (field.getString("field_type").equals("text_field")) {
                    val = fieldList.get(i).getText().toString();
                    if (field.getBoolean("required") && val.length() < 2)
                        throw new Exception(String.valueOf(i));
                }

                hm.put(field.getString("name"), val);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                //e.printStackTrace();
                TextLogHelper.toast(this, "This field is required", false);
                fieldList.get(Integer.valueOf(i)).setBackgroundColor(Color.BLUE);
            }
            i++;
        }

        TextLogHelper.log("OUT: "+ (new JSONObject(hm)).toString());
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
