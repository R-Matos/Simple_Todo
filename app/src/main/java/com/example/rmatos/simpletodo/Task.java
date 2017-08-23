package com.example.rmatos.simpletodo;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created by RMatos on 05/08/2017.
 */

public class Task {

    private UUID mID;
    private String mTitle;
    private String mNote;
    private Date dueDate;
    private ReminderType reminderType;
    private Date lastEdited;
    private Date reminder;
    private int alarmID;


    public enum ReminderType {
        NONE,
        ALARM,
        NOTIFICATION;
    }


    public Task() {
        this(UUID.randomUUID());
    }

    public Task(UUID id) {
        this.mID = id;
        this.mNote = null;
        this.lastEdited = new Date();
        this.reminderType = ReminderType.NONE;

        Random rand = new Random();
        this.alarmID = rand.nextInt(2147483645) + 1;
    }

    public UUID getID()                                 { return mID; }
    public String getTitle()                            { return mTitle; }
    public void setTitle(String title)                  { this.mTitle = title; }
    public String getNote()                             { return mNote; }
    public void setNote(String note)                    { this.mNote = note; }
    public Date getLastEditted()                        { return lastEdited; }
    public void setLastEditted(Date lastEditted)        { this.lastEdited = lastEditted; }
    public Date getDueDate()                            { return dueDate; }
    public void setDueDate(Date dueDate)                { this.dueDate = dueDate; }
    public ReminderType getReminderType()               { return reminderType; }
    public Date getReminder()                           { return reminder; }
    public void setReminder(Date reminder)              { this.reminder = reminder; }
    public void setAlarmID(int alarmID)                 { this.alarmID = alarmID; }
    public int getAlarmID()                             { return alarmID; }
    public void setReminderType(ReminderType reminderType) { this.reminderType = reminderType; }
}
