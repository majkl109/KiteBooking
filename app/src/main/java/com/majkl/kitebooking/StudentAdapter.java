package com.majkl.kitebooking;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;


public class StudentAdapter extends ArrayAdapter<Student>{

    List<Student> studentList;
    SQLiteDatabase sDatabase;
    int listLayoutRes;
    Context sCtx;

    public StudentAdapter(Context sCtx, int listLayoutRes, List<Student> studentList, SQLiteDatabase sDatabase) {
        super(sCtx, listLayoutRes, studentList);

        this.sCtx = sCtx;
        this.listLayoutRes = listLayoutRes;
        this.studentList = studentList;
        this.sDatabase = sDatabase;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(sCtx);
        View view = inflater.inflate(listLayoutRes, null);

        final Student student = studentList.get(position);

        TextView textViewName = view.findViewById(R.id.textViewName);
        TextView textViewCourses = view.findViewById(R.id.textViewCourses);
        TextView textViewDob = view.findViewById(R.id.textViewDob);
        TextView textViewJoiningDate = view.findViewById(R.id.textViewJoiningDate);
        ImageView imageViewImage = view.findViewById(R.id.imageViewImage);


        textViewName.setText(student.getName());
        textViewCourses.setText(student.getCourse());
        textViewDob.setText(String.valueOf(student.getDob()));
        textViewJoiningDate.setText(student.getJoiningDate());
        imageViewImage.(student.getImage());


        //we will use these buttons later for update and delete operation
        Button buttonDelete = view.findViewById(R.id.buttonDeleteStudent);
        Button buttonEdit = view.findViewById(R.id.buttonEditStudent);

        //adding a clicklistener to button
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStudent(student);
            }
        });
        //the delete operation
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(sCtx);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sql = "DELETE FROM students WHERE id = ?";
                        sDatabase.execSQL(sql, new Integer[]{student.getId()});
                        reloadStudentsFromDatabase();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }

    private void updateStudent(final Student student) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(sCtx);

        LayoutInflater inflater = LayoutInflater.from(sCtx);
        View view = inflater.inflate(R.layout.update_student_layout, null);
        builder.setView(view);


        final EditText editTextName = view.findViewById(R.id.editTextName);
        final EditText editTextDob = view.findViewById(R.id.editTextDob);
        final Spinner spinnerCourse = view.findViewById(R.id.spinnerCourse);

        editTextName.setText(student.getName());
        editTextDob.setText(String.valueOf(student.getDob()));

        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.buttonUpdateStudent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String dob = editTextDob.getText().toString().trim();
                String course = spinnerCourse.getSelectedItem().toString();

                if (name.isEmpty()) {
                    editTextName.setError("Name can't be blank");
                    editTextName.requestFocus();
                    return;
                }

                if (dob.isEmpty()) {
                    editTextDob.setError("Salary can't be blank");
                    editTextDob.requestFocus();
                    return;
                }

                String sql = "UPDATE students \n" +
                        "SET name = ?, \n" +
                        "course = ?, \n" +
                        "dob = ? \n" +
                        "WHERE id = ?;\n";

                sDatabase.execSQL(sql, new String[]{name, course, dob, String.valueOf(student.getId())});
                Toast.makeText(sCtx, "Student updated", Toast.LENGTH_SHORT).show();
                reloadStudentsFromDatabase();

                dialog.dismiss();
            }
        });
    }

    private void reloadStudentsFromDatabase() {
        Cursor cursorStudents = sDatabase.rawQuery("SELECT * FROM students", null);
        if (cursorStudents.moveToFirst()) {
            studentList.clear();
            do {
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
        cursorStudents.close();
        notifyDataSetChanged();
    }

}
