package com.majkl.kitebooking;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class StudentActivity extends AppCompatActivity {

    List<Student> studentList;
    SQLiteDatabase sDatabase;
    ListView listViewStudents;
    StudentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        listViewStudents =findViewById(R.id.listViewStudents);
        studentList = new ArrayList<>();

        //opening the database
        sDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);

        //this method will display the employees in the list
        showStudentsFromDatabase();
    }

    private void showStudentsFromDatabase() {

        //we used rawQuery(sql, selectionargs) for fetching all the students
        Cursor cursorStudents = sDatabase.rawQuery("SELECT * FROM students", null);

        //if the cursor has some data
        if (cursorStudents.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the student list
                studentList.add(new Student(
                        cursorStudents.getInt(0),
                        cursorStudents.getString(1),
                        cursorStudents.getString(2),
                        cursorStudents.getString(3),
                        cursorStudents.getInt(4),
                        cursorStudents.getBlob(5)
                ));
            } while (cursorStudents.moveToNext());
        }
        //closing the cursor
        cursorStudents.close();

        //creating the adapter object
        adapter = new StudentAdapter(this,R.layout.student_list_layout,studentList, sDatabase);

        //adding the adapter to listview
        listViewStudents.setAdapter(adapter);
    }

}
