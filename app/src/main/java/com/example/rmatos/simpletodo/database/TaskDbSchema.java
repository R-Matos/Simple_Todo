package com.example.rmatos.simpletodo.database;

/**
 * Created by RMatos on 08/08/2017.
 */

public class TaskDbSchema {

    public static final class TaskTable {
        public static final String NAME  = "tasks";


        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String NOTE = "note";
            public static final String DUE_DATE = "due_date";
            public static final String REMINDER_TYPE = "reminder_type";
            public static final String REMINDER_DATE = "reminder";
            public static final String LAST_EDITED = "last_edited";
        }


    }


}
