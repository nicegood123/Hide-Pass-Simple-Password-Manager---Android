package com.example.hidepass;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(MainActivity.this);

        if (!databaseHelper.checkUser("app@hidepass.com")) {
            Intent intentPasswordRegistration = new Intent(MainActivity.this, PasswordRegistrationActivity.class);
            startActivity(intentPasswordRegistration);
        } else {
            Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intentLogin);
        }

    }
}