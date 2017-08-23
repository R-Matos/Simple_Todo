package com.example.rmatos.simpletodo.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.example.rmatos.simpletodo.Task;
import com.example.rmatos.simpletodo.database.TaskDbSchema.TaskTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by RMatos on 09/08/2017.
 */

public class TaskCursorWrapper extends CursorWrapper {

    public TaskCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Task getTask() {
        String uuidString = getString(getColumnIndex(TaskTable.Cols.UUID));
        String title = getString(getColumnIndex(TaskTable.Cols.TITLE));
        String note = getString(getColumnIndex(TaskTable.Cols.NOTE));
        long dueDateLong = getLong(getColumnIndex(TaskTable.Cols.DUE_DATE));
        String reminderTypeString = getString(getColumnIndex(TaskTable.Cols.REMINDER_TYPE));
        long lastEditedLong = getLong(getColumnIndex(TaskTable.Cols.LAST_EDITED));
        long reminderLong = getLong(getColumnIndex(TaskTable.Cols.REMINDER_DATE));
        int alarmID = getInt(getColumnIndex(TaskTable.Cols.ALARM_ID));

        UUID uuid = UUID.fromString(uuidString);
        Date dueDate = (dueDateLong == 0) ? null : new Date(dueDateLong);
        Date lastEdited = (lastEditedLong == 0) ? null : new Date(lastEditedLong);
        Date reminder = (reminderLong == 0) ? null : new Date(reminderLong);
        Task.ReminderType reminderType = Task.ReminderType.NONE;
        if (reminderTypeString.equalsIgnoreCase("none")) {
            reminderType = Task.ReminderType.NONE;
        } else if (reminderTypeString.equalsIgnoreCase("alarm")) {
            reminderType = Task.ReminderType.ALARM;
        } else if (reminderTypeString.equalsIgnoreCase("notification")) {
            reminderType = Task.ReminderType.NOTIFICATION;
        } else {
            Log.e("TaskCursorWrapper", "Isn't a valid type for reminderType: " + reminderTypeString);
        }

        Task task = new Task(uuid);
        task.setTitle(title);
        task.setNote(note);
        task.setDueDate(dueDate);
        task.setLastEditted(lastEdited);
        task.setReminderType(reminderType);
        task.setReminder(reminder);
        task.setAlarmID(alarmID);

        return task;
    }
}
