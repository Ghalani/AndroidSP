package com.ghalani.ghalani.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ghalani.ghalani.R;
import com.ghalani.ghalani.helper.TextLogHelper;
import com.ghalani.ghalani.item.task.TeamTask;
import com.ghalani.ghalani.item.team.Team;
import com.google.android.gms.vision.text.Text;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TeamTaskActivity extends AppCompatActivity implements View.OnClickListener{
    TeamTask tt;
    TextView nameTV, descTV, commentTV, farmNameTV;
    JSONObject farm;
    TextView start, end;
    FloatingActionButton reportBut;
    int startDate, endDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_task);

        getSupportActionBar().setTitle("Activity details");  // provide compatibility to all the versions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        tt = (TeamTask) b.getSerializable("team_task_detail");

        nameTV = (TextView) findViewById(R.id.team_task_name);
        descTV = (TextView) findViewById(R.id.team_task_description);
        commentTV = (TextView) findViewById(R.id.team_task_comment);
        farmNameTV = (TextView) findViewById(R.id.team_task_farm);
        start = (TextView) findViewById(R.id.team_task_start);
        end = (TextView) findViewById(R.id.team_task_end);
        reportBut = (FloatingActionButton) findViewById(R.id.report_but);

        nameTV.setText(tt.getName());
        descTV.setText(tt.getDescription());
        commentTV.setText(tt.getComment());
        try {
            farm = new JSONObject(tt.getFarm());
            TextLogHelper.log("FARM: " + farm.getString("name"));
            farmNameTV.setText(farm.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //DateFormat format = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
        final DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
        startDate = daysBetween(LocalDate.now(), dtf.parseLocalDate(tt.getStart()));
        endDate = daysBetween(LocalDate.now(), dtf.parseLocalDate(tt.getEnd()));
        start.setText((startDate == 0 ? "today" : Math.abs(startDate) + " days ") + (startDate < 0 ? "ago" : ""));
        end.setText((endDate == 0 ? "today" : endDate + "days ") + (endDate < 0 ? "ago" : ""));
        /*TextLogHelper.log("Start: " + daysBetween(LocalDate.now(), dtf.parseLocalDate(tt.getStart())));
        TextLogHelper.log("End: " + daysBetween(LocalDate.now(), dtf.parseLocalDate(tt.getEnd())));*/
        reportBut.setOnClickListener(this);
    }

    public int daysBetween(LocalDate d1, LocalDate d2){
        TextLogHelper.log("DIFF: "+d1.toString()+" : "+d2.toString());
        //return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
        return Days.daysBetween(d1, d2).getDays();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.report_but:
                Intent intent = new Intent(this, TaskReportActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("team_task_detail", tt);
                intent.putExtras(b);
                if (startDate <= 0 && endDate >= 0)
                    startActivity(intent);
                else
                    TextLogHelper.toast(this, "This activity has not started", false);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
