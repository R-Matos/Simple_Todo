package com.example.rmatos.simpletodo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.rmatos.simpletodo.database.TaskDbSchema.TaskTable;

/**
 * Created by RMatos on 08/08/2017.
 */

public class TaskBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "taskBase.db";

    public TaskBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Creates the database table
        sqLiteDatabase.execSQL("create table " + TaskTable.NAME
                + "(" + " _id integer primary key autoincrement, " +
                TaskTable.Cols.UUID + ", " +
                TaskTable.Cols.TITLE + ", " +
                TaskTable.Cols.NOTE + ", " +
                TaskTable.Cols.DUE_DATE + ", " +
                TaskTable.Cols.REMINDER_TYPE + ", " +
                TaskTable.Cols.ALARMS + ", " +
                TaskTable.Cols.LAST_EDITED +
                ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
