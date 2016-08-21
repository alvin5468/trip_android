package com.example.mlj.mylocaljourney2;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by alvin on 2016/8/20.
 */
public class TripInfoDB {
    private final String STRING_SUCCESS="SUCCESS";
    private Activity activity;
    // constructor
    public TripInfoDB(Activity activity) {
        this.activity = activity;
    }
    private static final String db_name = "MyLocalJourneyDB"; // database name
    private static final String DBTableNameJourneyBegin = "AllJourneyBeginByUserId"; // table name
    private static final String DBTableNameJDetailourney = "DetailJourneyByJourneyBeginId"; // table name
    //private static final String DBTableName = "AllJourneyBeginByUserId"; // table name
    private SQLiteDatabase SQliteDB; //database object
    private ContentValues DatabaseCV;

    public int InitJourneyBegin(){
        // open or create database
        SQliteDB = activity.openOrCreateDatabase(db_name, android.content.Context.MODE_PRIVATE, null);
        // create table
        String sql_cmd = "CREATE TABLE IF NOT EXISTS " + DBTableNameJourneyBegin + "(userId VARCHAR(8), " + "JSON VARCHAR(2048))";
        SQliteDB.execSQL(sql_cmd);
        return 0;
    }

    public int InitJourney(){
        // open or create database
        SQliteDB = activity.openOrCreateDatabase(db_name, android.content.Context.MODE_PRIVATE, null);
        // create table
        String sql_cmd = "CREATE TABLE IF NOT EXISTS " + DBTableNameJDetailourney + "(journeyId VARCHAR(12), " + "JSON TEXT)";
        SQliteDB.execSQL(sql_cmd);
        return 0;
    }

    public int AddJourneyBeginData(String stUserId, String stJSON){
        //insert data
        Utils.l("Libo debug stUserId : " + stUserId);
        Utils.l("Libo debug stJSON : " + stJSON);

        DatabaseCV = new ContentValues(1);
        DatabaseCV.put("userId", stUserId);
        DatabaseCV.put("JSON",stJSON);
        SQliteDB.insert(DBTableNameJourneyBegin, null, DatabaseCV);

        return 0;
    }
    public int UpdateJourneyBeginData(String stUserId, String stJSON){
        //insert data
        Utils.l("Libo debug stUserId : " + stUserId);
        Utils.l("Libo debug stJSON : " + stJSON);

        DatabaseCV = new ContentValues(1);
        DatabaseCV.put("JSON",stJSON);
        SQliteDB.update(DBTableNameJourneyBegin, DatabaseCV, "userId=" + stUserId, null);

        return 0;
    }

    public int AddJourneyData(String journeyId, String stJSON){
        //insert data

        DatabaseCV = new ContentValues(1);
        DatabaseCV.put("journeyId", journeyId);
        DatabaseCV.put("JSON",stJSON);
        SQliteDB.insert(DBTableNameJDetailourney, null, DatabaseCV);

        return 0;
    }

    public int UpdateJourneyData(String journeyId, String stJSON){
        //insert data

        if (GetJourneyCount(journeyId) == 0) {
            AddJourneyData(journeyId, stJSON);
        } else {
            DatabaseCV = new ContentValues(1);
            DatabaseCV.put("JSON",stJSON);

            SQliteDB.update(DBTableNameJDetailourney, DatabaseCV, "journeyId=" + journeyId, null);
        }



        return 0;
    }
    public int DeleteAllJourneyBegid(){
        String sql_cmd = "delete from " + DBTableNameJourneyBegin ;
        SQliteDB.execSQL(sql_cmd);
        return 0;
    }
    public int GetJourneyBeginCount(){
        int JourneyBeginCount = 0;
        Cursor c = SQliteDB.rawQuery("SELECT * FROM " + DBTableNameJourneyBegin, null);
        JourneyBeginCount = c.getCount();
        return JourneyBeginCount;
    }

    public int GetJourneyCount(String journeyId){
        int JourneyBeginCount = 0;
        Cursor c = SQliteDB.rawQuery("SELECT * FROM " + DBTableNameJDetailourney + " WHERE journeyId=\""+journeyId + "\"", null);
        JourneyBeginCount = c.getCount();
        return JourneyBeginCount;
    }
    public String GetJourneyBeginData(){
        String JSON_Obj_str = null;
        //get data
        Cursor c = SQliteDB.rawQuery("SELECT * FROM " + DBTableNameJourneyBegin, null);
        if(c.getCount()==0)
        {
            Utils.l("Libo debug : NO data in the SQLiteDB");
        }
        else
        {
            c.moveToFirst();
            Utils.l("Libo debug : There are total " + c.getCount() + " data");
            do {
                Utils.l("Libo debug : userId : " + c.getString(0) );
                Utils.l("Libo debug : JSON : " + c.getString(1) );
                JSON_Obj_str = c.getString(1);
            }while(c.moveToNext());
        }
        return JSON_Obj_str;
    }



    public String GetJourneyData(String journeyId){
        String JSON_Obj_str = null;
        //get data
        Cursor c = SQliteDB.rawQuery("SELECT * FROM " + DBTableNameJDetailourney + " WHERE journeyId=\"" + journeyId + "\"", null);
        if(c.getCount()==0)
        {
            Utils.l("Libo debug : NO data in the SQLiteDB");
        }
        else
        {
            c.moveToNext();
            System.out.println("index is " + c.getColumnIndex("JSON"));
            JSON_Obj_str = c.getString(c.getColumnIndex("JSON"));
//            c.moveToFirst();
//            Utils.l("Libo debug : There are total " + c.getCount() + " data");
//            do {
//                Utils.l("Libo debug : userId : " + c.getString(0) );
//                Utils.l("Libo debug : LIYUAN : " + c.getString(1) );
//                JSON_Obj_str = c.getString(1);
//            } while(c.moveToNext());
        }
        return JSON_Obj_str;
    }

//    public int InitDetailJourney(){
//        // open or create database
//        SQliteDB = activity.openOrCreateDatabase(db_name, android.content.Context.MODE_PRIVATE, null);
//        // create table
//        String sql_cmd = "CREATE TABLE IF NOT EXISTS " + DBTableNameJDetailourney + "(JourneyBeginId VARCHAR(8), " + "JSON VARCHAR(2048))";
//        SQliteDB.execSQL(sql_cmd);
//        return 0;
//    }
}
