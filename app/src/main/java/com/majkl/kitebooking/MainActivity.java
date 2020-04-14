package com.majkl.kitebooking;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String DATABASE_NAME = "studentsdatabase";

    SQLiteDatabase sDatabase;

    TextView textViewViewStudents;
    EditText editTextName, editTextDob;
    Spinner spinnerCour;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        createStudentTable();

        textViewViewStudents = findViewById(R.id.textViewViewStudents);
        editTextName = findViewById(R.id.editTextName);
        editTextDob = findViewById(R.id.editTextDob);
        spinnerCour = findViewById(R.id.spinnerCourse);

        findViewById(R.id.buttonAddStudent).setOnClickListener(this);
        findViewById(R.id.textViewViewStudents).setOnClickListener(this);


    }


    private void createStudentTable() {

        String sql ="CREATE TABLE IF NOT EXISTS students (\n" +
                        "    id int NOT NULL CONSTRAINT student_pk PRIMARY KEY,\n" +
                        "    name varchar(200) NOT NULL,\n" +
                        "    course varchar(200) NOT NULL,\n" +
                        "    joiningdate datetime NOT NULL,\n" +
                        "    dob double NOT NULL\n" +
                        ");";

        sDatabase.execSQL(sql);

    }


    private void addStudent() {

        String name = editTextName.getText().toString().trim();
        String dob = editTextDob.getText().toString().trim();
        String cour = spinnerCour.getSelectedItem().toString();

        if (name.isEmpty()) {
            editTextName.setError("Please enter a name");
            editTextName.requestFocus();
            return;

        }

        if (dob.isEmpty()) {
            editTextDob.setError("Please enter dob");
            editTextDob.requestFocus();
            return;

        }

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String joiningDate = sdf.format(cal.getTime());


            String sql = "INSERT INTO students ( name, course, joiningdate, dob)" +
                    "VALUES (?, ?, ?, ?)";

            sDatabase.execSQL(sql, new String[]{name, cour, joiningDate, dob});

            Toast.makeText(this, "Student added successfully", Toast.LENGTH_SHORT).show();

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


