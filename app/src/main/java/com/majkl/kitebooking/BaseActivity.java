package com.majkl.kitebooking;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.majkl.kitebooking.MainActivity.DATABASE_NAME;


public class BaseActivity extends AppCompatActivity {

   protected SQLiteDatabase sDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
    }
}
