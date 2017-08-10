package com.example.rmatos.simpletodo;

import java.util.ArrayList;
import java.util.Date;
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
    private ArrayList<Date> alarms = new ArrayList<Date>();

    public enum ReminderType {
        NONE,
        ALARM,
        NOTIFICATION;
    }


    public Task() {
        this(UUID.randomUUID());
    }

    public Task(UUID id) {
        mID = id;
        mNote = null;
        lastEdited = new Date();
        reminderType = ReminderType.NONE;
    }

    public UUID getID()                                 { return mID; }
    public String getTitle()                            { return mTitle; }
    public void setTitle(String title)                  { this.mTitle = title; }
    public String getNote()                             { return mNote; }
    public void setNote(String note)                    { this.mNote = note; }
    public ArrayList<Date> getAlarms()                  { return alarms; }
    public Date getLastEditted()                        { return lastEdited; }
    public void setLastEditted(Date lastEditted)        { this.lastEdited = lastEditted; }
    public Date getDueDate()                            { return dueDate; }
    public void setDueDate(Date dueDate)                { this.dueDate = dueDate; }
    public ReminderType getReminderType()               { return reminderType; }
    public void addAlarm(Date date)                     { alarms.add(date); }
    public void setReminderType(ReminderType reminderType) { this.reminderType = reminderType; }
    public void setAlarms(Date date) { alarms.remove(0); alarms.add(date); }                        //TODO: Replace with multiple alarms
}
