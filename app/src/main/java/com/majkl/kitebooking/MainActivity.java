package com.majkl.kitebooking;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.majkl.kitebooking.sqlUtils.Utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final String DATABASE_NAME = "studentsdatabase";


    final int REQUEST_CODE_GALLERY = 100;

    TextView textViewViewStudents;
    EditText editTextName, editTextDob;
    ImageView gallery;
    Spinner spinnerCourse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewViewStudents = findViewById(R.id.textViewViewStudents);
        editTextName = findViewById(R.id.editTextName);
        editTextDob = findViewById(R.id.editTextDob);
        gallery = findViewById(R.id.gallery);
        spinnerCourse = findViewById(R.id.spinnerCourses);

        findViewById(R.id.buttonAddStudent).setOnClickListener(this);
        textViewViewStudents.setOnClickListener(this);

        Utils.createStudentTable(sDatabase);

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //read external storage permission to select image from gallery
                //runtime permission for devices android 6.0 and above
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

    }



    private boolean inputsAreCorrect(String name, String dob) {
        if (name.isEmpty()) {
            editTextName.setError("Please enter a name");
            editTextName.requestFocus();
            return false;

        }

        if (dob.isEmpty() || Integer.parseInt(dob) <= 0) {
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

        if (inputsAreCorrect(name, dob)) {
            String sql = "INSERT INTO students \n" +
                    "(name, course, joiningdate, dob, image)\n" +
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //gallery intent
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 10101);
            } else {
                Toast.makeText(this, "Don't have permission to access file location", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10101)
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    // TODO: 5/2/2020 Prvo compresirati na nesto normalno

                    // TODO: 5/2/2020 pretvoriti bitmap u base64




                    gallery.setImageBitmap(selectedImage);
                    Toast.makeText(this, "Tu je dliks sam se ne vidi", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            } else {

            }
        super.onActivityResult(requestCode, resultCode, data);
    }
}





