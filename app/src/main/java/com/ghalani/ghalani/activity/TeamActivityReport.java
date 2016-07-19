/*
package com.ghalani.ghalani.activity;

import android.*;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ghalani.ghalani.accounts.TeamReport;
import com.ghalani.ghalani.map.MyLocationListener;
import com.ghalani.ghalani.helper.TextLogHelper;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class TeamActivityReport extends AppCompatActivity implements View.OnClickListener {
    final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 0;
    private DatabaseReference myRef;
    LocationManager lm;
    LocationListener ll;
    private TextView lonTV, latTV;
    Button submitBut;
    EditText msgET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_activity_report);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ll = new MyLocationListener(this);

        lonTV = (TextView)findViewById(R.id.lon_tv);
        latTV = (TextView)findViewById(R.id.lat_tv);
        submitBut = (Button) findViewById(R.id.submit);
        submitBut.setOnClickListener(this);
        msgET = (EditText) findViewById(R.id.messageET);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("team_activities/1/reports");
        checkGPSpermission();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:
                sendReport();
                break;
        }
    }

    private void checkGPSpermission(){
        if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.INTERNET
                }, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
                return;
            }
        }else{
            startGPS();
        }
    }

    private void sendReport(){
        Calendar c = Calendar.getInstance();
        TeamReport tr = new TeamReport();
        tr.setDatetime(c.getTime().toString());
        tr.setLon(Double.valueOf(lonTV.getText().toString()));
        tr.setLat(Double.valueOf(latTV.getText().toString()));
        tr.setSp_id(1);
        tr.setMessage(msgET.getText().toString());
        myRef.push().setValue(tr, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                msgET.setText("");
                TextLogHelper.toast(TeamActivityReport.this, "Submitted", true);
            }
        });
    }

    @SuppressWarnings({"MissingPermission"})
    public void startGPS(){
        Log.d("beacon","START GPS");
        // (provider, mintime, mindistance, locationListener)
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, ll);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
*/
