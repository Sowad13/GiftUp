package com.example.giftup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {

    //Local Variables
    EditText username,name,password,mobile,email;
    Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //.xml element assigning
        username=findViewById(R.id.signup_username);
        name=findViewById(R.id.signup_name);
        password=findViewById(R.id.signup_password);
        mobile=findViewById(R.id.signup_phone);
        email=findViewById(R.id.signup_email);
    }
}
