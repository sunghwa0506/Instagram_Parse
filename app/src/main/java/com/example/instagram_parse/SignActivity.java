package com.example.instagram_parse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class SignActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSlideRight(this);
    }

    private EditText UserName;
    private EditText loginpassword;
    private EditText confirm_login_password;
    private Button sing_btn;
    private TextView warning_username;
    private TextView warning_password;

    boolean check_UserID = false;
    boolean check_password = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        UserName = findViewById(R.id.UserName);
        loginpassword = findViewById(R.id.loginpassword);
        confirm_login_password = findViewById(R.id.confirm_login_password);
        sing_btn = findViewById(R.id.sing_btn);
        warning_username = findViewById(R.id.warning_username);
        warning_password = findViewById(R.id.warning_password);

        UserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                checkUserID();
            }
        });


        sing_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkPassword();
                checkUserID();
                if(check_password && check_UserID)
                {
                    createUser();
                }
                else
                {
                    Toast.makeText(SignActivity.this,"Please put valid UserID or Password",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void createUser() {
        ParseUser user_account = new ParseUser();
        user_account.setUsername(UserName.getText().toString());
        user_account.setPassword(loginpassword.getText().toString());

        user_account.signUpInBackground(e -> {
            if (e == null) {

                Toast.makeText(SignActivity.this,"Successfully Created an Account",Toast.LENGTH_SHORT).show();
                ParseUser.logOutInBackground();
                Intent i = new Intent(this, LoginActivity.class);

                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                Animatoo.animateSlideRight(this);

            } else {
                // Sign up didn't succeed. Look at the ParseException
                // to figure out what went wrong
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkPassword() {
        String pw1 = loginpassword.getText().toString();
        String pw2 = confirm_login_password.getText().toString();

        if( pw1.equals(pw2))
        {
            warning_password.setVisibility(View.INVISIBLE);
            check_password = true;
        }
        else
        {
            warning_password.setVisibility(View.VISIBLE);
            check_password = false;
        }

    }

    private void checkUserID() {

        String check_name = UserName.getText().toString();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("username",check_name);
        query.findInBackground((objects, e) -> {
            if(e == null){
                Log.i(String.valueOf(this), "user name in db "+objects.toString());

                if(!objects.isEmpty())
                {
                    warning_username.setVisibility(View.VISIBLE);
                    check_UserID = false;
                }
                else
                {
                    warning_username.setVisibility(View.INVISIBLE);
                    check_UserID = true;

                }
            }else{
                Toast.makeText(SignActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}