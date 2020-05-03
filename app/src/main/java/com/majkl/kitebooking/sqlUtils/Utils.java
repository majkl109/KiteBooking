package com.majkl.kitebooking.sqlUtils;

import android.database.sqlite.SQLiteDatabase;

public class Utils {

    public static void createStudentTable(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS students (\n" +
                        "    id INTEGER NOT NULL CONSTRAINT student_pk PRIMARY KEY AUTOINCREMENT,\n" +
                        "    name varchar(200) NOT NULL,\n" +
                        "    course varchar(200) NOT NULL,\n" +
                        "    joiningdate datetime NOT NULL,\n" +
                        "    dob int NOT NULL,\n" +
                        "    image BLOB NOT NULL\n" +
                        ");"

        );
    }
}
