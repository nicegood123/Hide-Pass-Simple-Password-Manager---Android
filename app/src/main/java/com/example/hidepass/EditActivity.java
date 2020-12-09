package com.example.hidepass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = EditActivity.this;
    private NestedScrollView nestedScrollView;
    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private AppCompatButton appCompatButtonSaveChanges;
    private AppCompatButton appCompatButtonBack;
    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private User user;
    public static int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        getSupportActionBar().hide();
        initViews();
        initListeners();
        initObjects();
        initEditTextViewsValues();

    }

    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputEditTextName = (TextInputEditText) findViewById(R.id.textInputEditTextName);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        appCompatButtonSaveChanges = (AppCompatButton) findViewById(R.id.appCompatButtonSaveChanges);
        appCompatButtonBack = (AppCompatButton) findViewById(R.id.appCompatButtonBack);

    }

    private void initListeners() {
        appCompatButtonSaveChanges.setOnClickListener(this);
        appCompatButtonBack.setOnClickListener(this);
    }

    private void initObjects() {
        inputValidation = new InputValidation(activity);
        databaseHelper = new DatabaseHelper(activity);
        user = new User();
    }

    private void initEditTextViewsValues() {
        Bundle values = getIntent().getExtras();
        if (values == null) {
            return;
        }
        int user_id = values.getInt("user_id");
        User user = new User();
        user.setId(user_id);
        id = user_id;

        SQLiteDatabase db = getApplicationContext().openOrCreateDatabase("UserManager.db", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM user WHERE user_id=" + user_id, null);
        while (cursor.moveToNext()) {
            int name = cursor.getColumnIndex("user_name");
            int email = cursor.getColumnIndex("user_email");
            int password = cursor.getColumnIndex("user_password");

            textInputEditTextName.setText("" + cursor.getString(name));
            textInputEditTextEmail.setText("" + cursor.getString(email));
            textInputEditTextPassword.setText("" + cursor.getString(password));
        }
        cursor.close();
        db.close();
    }

    public void updateUser() {


        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }

        try {
            user.setId(id);
            user.setName(textInputEditTextName.getText().toString().trim());
            user.setEmail(textInputEditTextEmail.getText().toString().trim());
            user.setPassword(textInputEditTextPassword.getText().toString().trim());
            databaseHelper.update(user);
            Intent intentUsersList = new Intent(activity, UsersListActivity.class);
            startActivity(intentUsersList);
            toastMessage("Data Successfully Updated");
        } catch (Exception e) { toastMessage("" + e.getMessage()); }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonSaveChanges:
                updateUser();
                break;
            case R.id.appCompatButtonBack:
                Intent intentUsersList = new Intent(activity, UsersListActivity.class);
                startActivity(intentUsersList);
                break;
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }
}