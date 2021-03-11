package com.example.instagram_parse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button btn_Login;
    private Button  btn_sign;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(ParseUser.getCurrentUser() != null)
        {
            goMainActivity();
        }


        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btn_Login = findViewById(R.id.btn_Login);
        btn_sign = findViewById(R.id.btn_sign);

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Log.i("Login","onClick Login button");
            String user = username.getText().toString();
            String pass = password.getText().toString();
            loginUser(user, pass);
            }

        });

    }

    private void loginUser(String user, String pass) {
        Log.i("Login Function","Attempting to login user" + user);
        ParseUser.logInInBackground(user, pass, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null)
                {
                    Log.e("Login","Issue with login",e);
                    Toast.makeText(LoginActivity.this,"Login Fail",Toast.LENGTH_SHORT).show();
                    return;
                }

                    Toast.makeText(LoginActivity.this,"Success!",Toast.LENGTH_SHORT).show();
                    goMainActivity();



            }


        });


    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}