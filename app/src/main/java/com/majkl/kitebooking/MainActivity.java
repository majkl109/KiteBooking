package com.majkl.kitebooking;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String DATABASE_NAME = "studentsdatabase";




    TextView textViewViewStudents;
    EditText editTextName, editTextDob;
    Spinner spinnerCourse;

    SQLiteDatabase sDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewViewStudents = findViewById(R.id.textViewViewStudents);
        editTextName = findViewById(R.id.editTextName);
        editTextDob = findViewById(R.id.editTextDob);

        spinnerCourse = findViewById(R.id.spinnerCourses);


        findViewById(R.id.buttonAddStudent).setOnClickListener(this);
        textViewViewStudents.setOnClickListener(this);

        sDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        createStudentTable();

    }

    private void createStudentTable() {

        sDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS students (\n" +
                "    id INTEGER NOT NULL CONSTRAINT student_pk PRIMARY KEY AUTOINCREMENT,\n" +
                "    name varchar(200) NOT NULL,\n" +
                "    course varchar(200) NOT NULL,\n" +
                "    joiningdate datetime NOT NULL,\n" +
                "    dob int NOT NULL\n" +
                ");"

        );
    }

    private boolean inputsAreCorrect(String name, String dob) {
        if (name.isEmpty()) {
            editTextName.setError("Please enter a name");
            editTextName.requestFocus();
            return false;

        }

        if (dob.isEmpty()|| Integer.parseInt(dob) <= 0) {
            editTextDob.setError("Please enter dob");
            editTextDob.requestFocus();
            return false;
        }
        return true;
    }

    private void addStudent() {

        String name = editTextName.getText().toString().trim();
        String dob = editTextDob.getText().toString().trim();
        String course = spinnerCourse.getSelectedItem().toString();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String joiningDate = sdf.format(cal.getTime());

        if(inputsAreCorrect(name, dob)) {
            String sql = "INSERT INTO students \n" +
                    "(name, course, joiningdate, dob)\n" +
                    "VALUES \n " +
                    "(?, ?, ?, ?)";

            sDatabase.execSQL(sql, new String[]{name, course, joiningDate, dob});

            Toast.makeText(this, "Student added successfully", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAddStudent:

                addStudent();

                break;
            case R.id.textViewViewStudents:


                startActivity(new Intent(this, StudentActivity.class));

                break;
        }

    }

}



