package com.example.giftup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    //local variables
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //set variables
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        logout=findViewById(R.id.logout_btn);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent singedOut=new Intent(HomeActivity.this,SignInActivity.class);
                startActivity(singedOut);
                finish();
                Toast.makeText(getApplicationContext(),"Logged out.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if(currentUser==null)
        {
            startActivity(new Intent(HomeActivity.this, SignInActivity.class));
            finish();
        }
    }

}
