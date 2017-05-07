package com.example.h_dj.note.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by H_DJ on 2017/5/6.
 */

public class DBHelper extends SQLiteOpenHelper {
    /**
     * 数据库名称
     */
    public static final String DATABASES_NAME = "notes.db";
    /**
     * 表名
     */
    public final String TABLE_NAME = "notes";
    public static final int VERSION = 1;


    public DBHelper(Context context) {
        super(context, DATABASES_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE [notes](" +
                "    noteId INTEGER PRIMARY KEY NOT NULL," +
                "    noteTitle NVARCHAR NOT NULL," +
                "    noteType NVARCHAR NOT NULL," +
                "    modifyTime VARCHAR NOT NULL," +
                "    alarmTime VARCHAR," +
                "    isAlarm BOOLEAN NOT NULL,"+
                "    isMark BOOLEAN," +
                "    noteContent NVARCHAR NOT NULL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

}
