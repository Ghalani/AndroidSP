/*
package com.ghalani.ghalani.db;

*/
/**
 * Created by Amanze on 5/29/2016.
 *//*

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.ghalani.ghalani.pickup.Point;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ghalani.db";
    public static final String POINT_TABLE_NAME = "pickup";
    public static final String POINT_ID = "id";
    public static final String POINT_PICKUP_ID = "pickup_id";
    public static final String POINT_DRIVER_ID = "driver_id";
    public static final String POINT_LON = "lon";
    public static final String POINT_LAT = "lat";
    public static final String POINT_SPEED = "speed";
    public static final String POINT_TIME = "time";


    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("create table "+POINT_TABLE_NAME+
                " (" + POINT_ID +" INTEGER PRIMARY KEY, "
                + POINT_PICKUP_ID + " INTEGER NOT NULL, "
                + POINT_DRIVER_ID + " INTEGER NOT NULL, "
                + POINT_LON +" REAL NOT NULL, "
                + POINT_LAT + " REAL NOT NULL, "
                + POINT_SPEED +" REAL, "
                + POINT_TIME +" TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+POINT_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertPoint  (int pickup_id, int driver_id, double lon, double lat, double speed, String time)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(POINT_PICKUP_ID, pickup_id);
        contentValues.put(POINT_DRIVER_ID, driver_id);
        contentValues.put(POINT_LON, lon);
        contentValues.put(POINT_LAT, lat);
        contentValues.put(POINT_SPEED, speed);
        contentValues.put(POINT_TIME, time);
        db.insert(POINT_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getPointData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from radio where id="+id+"", null );
        return res;
    }

    public int numberOfPointRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, POINT_TABLE_NAME);
        return numRows;
    }

    public void deleteAll(){
        this.getWritableDatabase().execSQL("delete from "+POINT_TABLE_NAME);
    }

    public ArrayList<Point> getAllPoints()
    {
        ArrayList<Point> points = new ArrayList<Point>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+POINT_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Point rd = new Point();
            rd.setPickup_id(Integer.valueOf(res.getString(res.getColumnIndex(POINT_PICKUP_ID))));
            rd.setDriver_id(res.getInt(res.getColumnIndex(POINT_DRIVER_ID)));
            rd.setLon(res.getDouble(res.getColumnIndex(POINT_LON)));
            rd.setLat(res.getDouble(res.getColumnIndex(POINT_LAT)));
            rd.setSpeed(res.getDouble(res.getColumnIndex(POINT_SPEED)));
            rd.setTime(res.getString(res.getColumnIndex(POINT_TIME)));
            points.add(rd);
            res.moveToNext();
        }
        return points;
    }

    public Point getLastPoint() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM "+POINT_TABLE_NAME +
                " WHERE ID = (SELECT MAX(ID)  FROM "+POINT_TABLE_NAME +")", null );
        res.moveToLast();
        Point rd = new Point();
        rd.setPickup_id(Integer.valueOf(res.getString(res.getColumnIndex(POINT_PICKUP_ID))));
        rd.setDriver_id(res.getInt(res.getColumnIndex(POINT_DRIVER_ID)));
        rd.setLon(res.getDouble(res.getColumnIndex(POINT_LON)));
        rd.setLat(res.getDouble(res.getColumnIndex(POINT_LAT)));
        rd.setSpeed(res.getDouble(res.getColumnIndex(POINT_SPEED)));
        rd.setTime(res.getString(res.getColumnIndex(POINT_TIME)));
        return rd;
    }

    */
/*public boolean updatePoint (Integer id, String name, String url, String website, String about, String country)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("url", url);
        contentValues.put("website", website);
        contentValues.put("about", about);
        contentValues.put("country", country);
        db.update("radio", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    */
/*public Integer deletePoint (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(POINT_TABLE_NAME,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }*//*

}*/
