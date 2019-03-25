package com.example.courseratingapp;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private String username;
    private final String USERNAME_KEY = "username";
    private TextInputEditText inputUsername;

    private final String PASSWORD_KEY = "password";
    private EditText inputPassword;
    private Button btnSubmitLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initListeners();
    }

    private void initViews(){
        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        btnSubmitLogin = findViewById(R.id.btnSubmitLogin);
    }

    private void initListeners(){
        btnSubmitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateLoginDetails()){
                    returnToMainActivity(true);
                } else {
                    Toast.makeText(LoginActivity.this, "username or password was incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // obviously this is just a placeholder method
    private boolean validateLoginDetails(){
        username = inputUsername.getText().toString();
        return(username.toLowerCase().equals("user"));
    }

    private void returnToMainActivity(boolean loggedIn) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("loggedInExtra", true);
        intent.putExtra("username", username);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(USERNAME_KEY, inputUsername.getText().toString());
        savedInstanceState.putString(PASSWORD_KEY, inputPassword.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        inputUsername.setText(savedInstanceState.getString(USERNAME_KEY));
        inputPassword.setText(savedInstanceState.getString(PASSWORD_KEY));
        inputUsername.setSelection(inputUsername.getText().length());
        inputPassword.setSelection(inputPassword.getText().length());
    }
}
