package com.example.rmatos.simpletodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.rmatos.simpletodo.database.TaskBaseHelper;
import com.example.rmatos.simpletodo.database.TaskCursorWrapper;
import com.example.rmatos.simpletodo.database.TaskDbSchema;
import com.example.rmatos.simpletodo.database.TaskDbSchema.TaskTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Singleton container obj that persists to store and access Task objects
 */

public class TaskStore {
    private static TaskStore sTaskStore;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    //Only creates TaskStore obj if not created yet. Part of singleton pattern.
    //Passes in context as ref will prevent GC
    public static TaskStore get(Context context) {
        if (sTaskStore == null)
            sTaskStore = new TaskStore(context);

        return sTaskStore;
    }

    //Private constructor part of singleton pattern
    private TaskStore(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new TaskBaseHelper(mContext).getWritableDatabase();
    }

    public void addTask(Task task) {
        ContentValues values = getContentValues(task);
        mDatabase.insert(TaskTable.NAME, null, values);
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();

        TaskCursorWrapper cursor = queryTasks(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                tasks.add(cursor.getTask());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return tasks;
    }

    //Retrieval of a single row from SQL
    public Task getTask(UUID id) {

        TaskCursorWrapper cursor = queryTasks(
                TaskTable.Cols.UUID + " = ?",
                new String[] { id.toString() }                                                      //Prevents SQL Injection
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getTask();
        } finally {
            cursor.close();
        }
    }

    public void updateTask(Task task) {
        String uuidString = task.getID().toString();
        ContentValues values = getContentValues(task);

        mDatabase.update(TaskTable.NAME, values,
                TaskTable.Cols.UUID + " = ?",
                new String[] { uuidString });                                                       //Prevents SQL Injection
    }

    private TaskCursorWrapper queryTasks(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                TaskTable.NAME,
                null,                                                                               //Columns (null = all)
                whereClause,
                whereArgs,
                null,                                                                               //GroupBy
                null,                                                                               //Having
                null                                                                                //OrderBy
        );

        return new TaskCursorWrapper(cursor);
    }

    //Map to place in database, a single entry
    private static ContentValues getContentValues(Task task) {
        long dueDate = (task.getDueDate() == null) ? 0 : task.getDueDate().getTime();
        long lastEdited = (task.getLastEditted() == null) ? 0 : task.getLastEditted().getTime();
        long reminder = (task.getReminder() == null) ? 0 : task.getReminder().getTime();

        ContentValues values = new ContentValues();
        values.put(TaskTable.Cols.UUID, task.getID().toString());
        values.put(TaskTable.Cols.TITLE, task.getTitle());
        values.put(TaskTable.Cols.NOTE, task.getNote());
        values.put(TaskTable.Cols.DUE_DATE, dueDate);
        values.put(TaskTable.Cols.REMINDER_TYPE, task.getReminderType().name());
        values.put(TaskTable.Cols.REMINDER_DATE, reminder);
        values.put(TaskTable.Cols.LAST_EDITED, lastEdited);

        return values;
    }

}
