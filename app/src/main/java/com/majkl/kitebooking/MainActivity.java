package com.majkl.kitebooking;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String DATABASE_NAME = "studentsdatabase";


   final int REQUEST_CODE_GALLERY = 100;

    TextView textViewViewStudents;
    EditText editTextName, editTextDob;
    ImageView mImageView;
    Spinner spinnerCourse;

    SQLiteDatabase sDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewViewStudents = findViewById(R.id.textViewViewStudents);
        editTextName = findViewById(R.id.editTextName);
        editTextDob = findViewById(R.id.editTextDob);
        mImageView = findViewById(R.id.imageView);
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
                "    dob int NOT NULL,\n" +
                "    image BLOB NOT NULL\n" +
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
        byte   image = mImageView.setImageResource(R.drawable.add_photo);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String joiningDate = sdf.format(cal.getTime());

        if (inputsAreCorrect(name, dob)) {
            String sql = "INSERT INTO students \n" +
                    "(name, course, joiningdate, dob, image)\n" +
                    "VALUES \n " +
                    "(?, ?, ?, ?)";

            sDatabase.execSQL(sql, new String[]{name, course, joiningDate, dob} );

            Toast.makeText(this, "Student added successfully", Toast.LENGTH_SHORT).show();

        }


        mImageView.setOnClickListener(new View.OnClickListener() {
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
    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALLERY){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //gallery intent
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(this, "Don't have permission to access file location", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON) //enable image guidlines
                    .setAspectRatio(1,1)// image will be square
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result =CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                //set image choosed from gallery to image view
                mImageView.setImageURI(resultUri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}





