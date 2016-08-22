package com.ghalani.ghalani.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FarmerDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ghalani.db";
    public static final String TABLE_NAME = "farmers";
    public static final String ID = "id";
    public static final String PHONE = "phone";
    public static final String FNAME = "fname";
    public static final String LNAME = "lname";
    public static final String GENDER = "gender";
    public static final String DOB = "dob";
    public static final String IMAGE_URL = "image_url";


    public FarmerDBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("create table "+TABLE_NAME+
                " (" + ID +" INTEGER PRIMARY KEY, "
                + PHONE + " STRING NOT NULL, "
                + FNAME + " STRING NOT NULL, "
                + LNAME +" STRING NOT NULL, "
                + GENDER + " STRING, "
                + DOB +" STRING, "
                + IMAGE_URL +" TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addFarmer (int id, String phone, String fname, String lname, String gender, String dob, String img)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, id);
        contentValues.put(PHONE, phone);
        contentValues.put(FNAME, fname);
        contentValues.put(LNAME, lname);
        contentValues.put(GENDER, gender);
        contentValues.put(DOB, dob);
        contentValues.put(IMAGE_URL, img);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getFarmer(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_NAME +" where id="+id+"", null );
        return res;
    }
    public JSONObject getFarmer(String phone){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_NAME +" where phone='"+ phone +"'", null );
        //res.moveToFirst();
        JSONObject jObj = null;
        if (res != null && res.moveToFirst()){
            jObj = new JSONObject();
            try {
                jObj.put(ID, res.getInt(res.getColumnIndex(ID)));
                jObj.put(PHONE, res.getString(res.getColumnIndex(PHONE)));
                jObj.put(FNAME, res.getString(res.getColumnIndex(FNAME)));
                jObj.put(LNAME, res.getString(res.getColumnIndex(LNAME)));
                jObj.put(GENDER, res.getString(res.getColumnIndex(GENDER)));
                jObj.put(DOB, res.getString(res.getColumnIndex(DOB)));
                jObj.put(IMAGE_URL, res.getString(res.getColumnIndex(IMAGE_URL)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jObj;
    }

    public int numberOfPointRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public void deleteAll(){
        this.getWritableDatabase().execSQL("delete from " + TABLE_NAME);
    }

    public JSONArray getAllfarmers()
    {
        JSONArray jAr = new JSONArray();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            JSONObject jObj = new JSONObject();
            try {
                jObj.put(ID, res.getInt(res.getColumnIndex(ID)));
                jObj.put(PHONE, res.getString(res.getColumnIndex(PHONE)));
                jObj.put(FNAME, res.getString(res.getColumnIndex(FNAME)));
                jObj.put(LNAME, res.getString(res.getColumnIndex(LNAME)));
                jObj.put(GENDER, res.getString(res.getColumnIndex(GENDER)));
                jObj.put(DOB, res.getString(res.getColumnIndex(DOB)));
                jObj.put(IMAGE_URL, res.getString(res.getColumnIndex(IMAGE_URL)));
                jAr.put(jObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            res.moveToNext();
        }
        return jAr;
    }

    /*public Point getLastPoint() {
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

    public boolean updatePoint (Integer id, String name, String url, String website, String about, String country){
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

    public Integer deletePoint (Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(POINT_TABLE_NAME,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }*/

}
