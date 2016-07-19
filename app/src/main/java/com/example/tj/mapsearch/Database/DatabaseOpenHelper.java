package com.example.tj.mapsearch.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by TJ on 2016-07-19.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private final static String TAG = "DatabaseOpenHelper";
    private final static String TABLE_NAME = "makerlist";
    SQLiteDatabase sqLiteDatabase;
    Context context;
    String databaseName;
    int version;
    int factory;
    boolean databaseCreated;

    public DatabaseOpenHelper(Context context, String name, int factory, int version) {
        super(context, name, null, version);
        this.context = context;
        this.databaseName = name;
        this.version = version;
        this.factory = factory;
        sqLiteDatabase = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,"try 진입전");
        // 오류나면 new 생성
        try {
            this.sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(databaseName, null, null);
            databaseCreated = true;
            Log.d(TAG,"databases is createdOrOpen.");
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG,"databases is not created.");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG,"Upgrading database from version "+oldVersion + " to " + newVersion);
    }

    public void createTable(){
        /*
        try {
            String DROP_SQL = "drop table if exists "+TABLE_NAME;
            sqLiteDatabase.execSQL(DROP_SQL);
        }catch (Exception e){
            Log.d(TAG,"EXCEPTION IN DROP_SQL.",e);
        }*/

        String CREATE_SQL = "create table "+TABLE_NAME+"("
                +"_id integer PRIMARY KEY autoincrement, "
                +"address_name text, "
                +"latitude text, "
                +"longitude text);";

        try {
            sqLiteDatabase.execSQL(CREATE_SQL);
        }catch (Exception e){
            Log.d(TAG,"EXCEPTION IN CREATE_SQL.",e);
        }
    }

    public SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabase;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getDatabaseName() {
        return databaseName;
    }

    public void setSqLiteDatabase(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }
}
