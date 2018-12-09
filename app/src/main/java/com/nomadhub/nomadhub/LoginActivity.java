package com.nomadhub.nomadhub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    EditText pwdEdittext;
    EditText userEdittext;
    Button logInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(ParseUser.getCurrentUser() != null){
            Toast.makeText(LoginActivity.this, "Welcome back " + ParseUser.getCurrentUser().getUsername() + " !", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,MainActivity.class));
        }

        logInButton = findViewById(R.id.logInButton);
        pwdEdittext = findViewById(R.id.pwdEdittext);
        userEdittext = findViewById(R.id.userEdittext);
    }

    public void parseLogIn (View view){

        final Intent intent = new Intent(this,MainActivity.class);

        ParseUser.logInInBackground(userEdittext.getText().toString(), pwdEdittext.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null){
                    Toast.makeText(LoginActivity.this, "Welcome back " + user.getUsername() + " !", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}