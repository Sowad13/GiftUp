package com.example.giftup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SignInActivity extends AppCompatActivity {
    //Local Variables
    EditText username,password;
    Button btn;
    TextView signUp;
    String signup_text="Don't have an account? Sign up now!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        //.xml element assigning
        username=findViewById(R.id.signin_username_editText);
        password=findViewById(R.id.signin_password_editText);
        btn=findViewById(R.id.signin_btn);
        signUp=findViewById(R.id.sign_up_text);

        //clickable span for sign up text
        SpannableString signUpSpan= new SpannableString(signup_text);
        ClickableSpan clickableSpan= new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        };
        signUpSpan.setSpan(clickableSpan,23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        signUp.setText(signUpSpan);
        signUp.setMovementMethod(LinkMovementMethod.getInstance());

    }
}
