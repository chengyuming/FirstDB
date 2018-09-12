package com.cym.pactera.scheduleappdemo.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by pactera on 2018/9/3.
 */

public class DataStorage extends SQLiteOpenHelper {
    private static final String TAG = "schedule";
    public static final int VERSION = 1;
    private String name;
    private String sql;

    //必须要有构造函数
    public DataStorage(Context context, String name, SQLiteDatabase.CursorFactory factory,
                       int version) {
        super(context, name, factory, version);
        this.name = name;

    }

    // 当第一次创建数据库的时候，调用该方法
    public void onCreate(SQLiteDatabase db) {
        if(name.equals("schedule_table"))
        sql = "create table schedule_table(_id integer primary key autoincrement," +
                "time text,goTime text,arriveTime text,address text,distance text,city text)";
        if(name.equals("saveUse_table"))
            sql = "create table saveUse_table(_id integer primary key autoincrement," +
                    "time text,address text,distance text,city text)";
        //输出创建数据库的日志信息
        Log.i(TAG, "create Database------------->");
        //execSQL函数用于执行SQL语句
        db.execSQL(sql);
    }

         //当更新数据库的时候执行该方法
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
          //输出更新数据库的日志信息
        Log.i(TAG, "update Database------------->");
    }
}
