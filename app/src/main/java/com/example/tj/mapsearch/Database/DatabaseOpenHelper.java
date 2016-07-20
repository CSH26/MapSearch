package com.example.tj.mapsearch.Database;

import android.content.Context;
import android.database.Cursor;
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

        checkTableIsCreated(sqLiteDatabase);
        /*
        try {
            String DROP_SQL = "drop table if exists "+TABLE_NAME;
            sqLiteDatabase.execSQL(DROP_SQL);
        }catch (Exception e){
            Log.d(TAG,"EXCEPTION IN DROP_SQL.",e);
        }

        String CREATE_SQL = "create table "+TABLE_NAME+"("
                +"_id integer PRIMARY KEY autoincrement, "
                +"address_name text, "
                +"latitude text, "
                +"longitude text);";

        try {
            sqLiteDatabase.execSQL(CREATE_SQL);
        }catch (Exception e){
            Log.d(TAG,"EXCEPTION IN CREATE_SQL.",e);
        }*/
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

    private void checkTableIsCreated(SQLiteDatabase db){
        String CREATE_SQL = "create table "+TABLE_NAME+"("
                +"_id integer PRIMARY KEY autoincrement, "
                +"address_name text, "
                +"latitude text, "
                +"longitude text);";

        Cursor c=db.query("sqlite_master", new String[]{"count(*)"}, "name=?",
                new String[]{"makerlist"}, null, null, null);  //시스템 카탈로그에서 커서로 테이블 이름을 확인
        Integer cnt=0;
        c.moveToFirst();
        while(c.isAfterLast()==false){
            cnt=c.getInt(0);
            c.moveToNext();
        }
        c.close();

        if(cnt==0){
            db.execSQL(CREATE_SQL); //DB에 테이블이 없으면 테이블 생성 쿼리 실행
        }
    }
}
