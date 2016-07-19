package com.ghalani.ghalani.map;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
//import com.google.android.gms.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.ghalani.ghalani.R;
//import com.ghalani.ghalani.TrackActivity;

public class MyLocationListener implements LocationListener {
    final double MIN_DIFF = 0.000001;
    public Context context;
    TextView lonTV,latTV,statusTV;
    Boolean isMoving = false;
    //Double oldLon,oldLat,latDiff,lonDiff;

    public MyLocationListener(Context ctx){
        context = ctx;
        lonTV = (TextView)((Activity)context).findViewById(R.id.lon_tv);
        latTV = (TextView)((Activity)context).findViewById(R.id.lat_tv);
        //statusTV = (TextView)((Activity)context).findViewById(R.id.status_tv);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            /*if((TrackActivity.mydb.numberOfPointRows() == 0)) {
                //  if new and the device is moving
                if((location.getSpeed() > 0))
                    loadPoint(location);
            }else{ //   the DB is not empty
                Point old = TrackActivity.mydb.getLastPoint();//TrackActivity.points.get(TrackActivity.points.size() - 1);
                oldLon = old.getLon();
                oldLat = old.getLat();
                latDiff =  Math.abs(oldLat - location.getLatitude());
                lonDiff =  Math.abs(oldLon - location.getLongitude());
                Log.e("ghalani", "latDiff: "+latDiff+" | lonDiff: "+lonDiff);

                if((latDiff >= MIN_DIFF) || (lonDiff >= MIN_DIFF) ) {
                    loadPoint(location);
                    isMoving = true;
                }else{
                    isMoving = false;
                }
            }*/
            //modifyTV(location.getLongitude(), location.getLatitude(), location.getSpeed(), isMoving);
            modifyTV(location.getLongitude(), location.getLatitude());
        }
    }

    /*private void loadPoint(Location location) {
        Log.e("ghalani", "Adding point to array");
        Point pt = new Point();
        pt.setPickup_id(1);
        pt.setDriver_id(1);
        pt.setLat(location.getLatitude());
        pt.setLon(location.getLongitude());
        pt.setSpeed(location.getSpeed());
        pt.setTime(location.getTime() + "");
        TrackActivity.points.add(pt);
        TrackActivity.mydb.insertPoint(1, 1, location.getLongitude(), location.getLatitude(), location.getSpeed(), location.getTime()+"");
    }*/

    /*public void modifyTV(double lon, double lat, double speed, Boolean isMoving) {
        latTV.setText(lat+"");
        lonTV.setText(lon+"");
        //statusTV.setText(isMoving ? "Moving at "+speed+" m/s" : "Stopped");
    }*/
    public void modifyTV(double lon, double lat) {
        latTV.setText(lat+"");
        lonTV.setText(lon+"");
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
    @Override
    public void onProviderEnabled(String provider) {
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}