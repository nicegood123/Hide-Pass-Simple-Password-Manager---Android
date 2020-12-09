package com.example.hidepass;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import java.util.List;

public class UsersListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {
    private final AppCompatActivity activity = UsersListActivity.this;
    private DatabaseHelper databaseHelper;
    private ListView lvUser;
    SearchView svSearchUser;
    ArrayAdapter userArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        databaseHelper = new DatabaseHelper(this);

        getSupportActionBar().hide();
        initViews();
        initListeners();
        initObjects();

    }

    public void onClickAdd(View view) {
        Intent intentAdd = new Intent(activity, AddActivity.class);
        startActivity(intentAdd);
    }

    private void initViews() {
        lvUser = (ListView) findViewById(R.id.lvUser);
        svSearchUser = (SearchView) findViewById(R.id.svSearchUser);
    }

    private void initListeners() {
        svSearchUser.setOnQueryTextListener(this);
        lvUser.setOnItemClickListener(this);
    }

    private void initObjects() {
        displayUsers();
    }

    private void displayUsers() {
        try {
            List<User> users_list = databaseHelper.getAllUser();
            userArrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, users_list);
            lvUser.setAdapter(userArrayAdapter);
        } catch (Exception e) { toastMessage(e.getMessage()); }
    }

    private void toastMessage(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) { return false; }

    @Override
    public boolean onQueryTextChange(String newText) {
        try {
            userArrayAdapter.getFilter().filter(newText);
            return false;
        }catch (Exception e){
            displayUsers();
            return false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        User user = (User) parent.getItemAtPosition(position);
        String title = "Account Info";
        String message = "Name: " + user.getName() + "\n"
                + "Email: " + user.getEmail() + "\n"
                + "Password: " + user.getPassword();

        showDialog(user, title, message);
    }

    private void showDialog(User user, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialog);
        builder.setPositiveButton("EDIT", (dialog, which) -> {

            Intent intentEdit = new Intent(activity, EditActivity.class);
            intentEdit.putExtra("user_id", user.getId());
            startActivity(intentEdit);

        }).setNegativeButton("DELETE", (dialog, which) -> {
            databaseHelper.delete(user);
            toastMessage("Data Successfully Deleted");
            displayUsers();
        });

        AlertDialog alert = builder.create();
        alert.setTitle(title);
        alert.setMessage(message);
        alert.show();
    }
}