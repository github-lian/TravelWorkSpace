package com.example.lian.travel.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lian on 2017/12/29.
 */

public class MyDate extends SQLiteOpenHelper {

    public MyDate(Context context) {
        super(context, "myharas.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table phone_black_list(_id integer primary key autoincrement,blacklist_phone_number varchar(20),blacklist_desc varchar(100),blacklist_time varchar(40))");//手机黑名单
        db.execSQL("create table phone_white_list(_id integer primary key autoincrement,whitelist_phone_number varchar(20),whitelist_desc varchar(100),whitelist_time varchar(40))");//手机白名单
        db.execSQL("create table msg_black_list(_id integer primary key autoincrement,msg_black_key varchar(20),msg_black_time varchar(20),sign varchar(40))");//短信黑名单
        db.execSQL("create table interception_msg(_id integer primary key autoincrement,mnumber varchar(20),mmsg varchar(100),mtime varchar(40))");//拦截信息
        db.execSQL("create table interception_phone(_id integer primary key autoincrement,pnumber varchar(20),ptime varchar(40),ptimesign varchar(40))");//拦截电话
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
