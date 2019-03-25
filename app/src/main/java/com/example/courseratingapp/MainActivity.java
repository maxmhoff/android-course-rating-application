package com.example.courseratingapp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";

    // the username is expected to be a unique identifier in this case,
    // but normally you'd have an id to send along
    private String username;

    private boolean loggedIn = false;

    private TextView title;
    private Button btnRateACourse;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");
        setContentView(R.layout.activity_main);
        initViews();
        initListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 1) {
            if(data != null) {
                if(data.getBooleanExtra("loggedInExtra", false)){
                    setLoggedIn();
                }
                username = data.getStringExtra("username");
            }
        }
    }

    private void initViews(){
        title = findViewById(R.id.title);
        btnRateACourse = findViewById(R.id.btnRateACourse);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void initListeners(){
        btnRateACourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivitySelectCourse();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityLogin();
            }
        });
    }

    private void openActivityLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, 1);
    }

    private void openActivitySelectCourse(){
        Intent intent = new Intent(this, SelectCourseActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    //placeholder login - only works one way at the moment; you can't logout
    public void setLoggedIn(){
        loggedIn = true;
        btnRateACourse.setEnabled(true);
        btnLogin.setText(getString(R.string.btnLoginAlt));
    }
}
