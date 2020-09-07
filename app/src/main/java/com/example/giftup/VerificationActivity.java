package com.example.giftup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class VerificationActivity extends AppCompatActivity {

    //Member Variables
    EditText phone_number_2;
    Button verify;
    ProgressBar progressBar2;
    TextView feedback2;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String VerificationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        //xml element assigning
        phone_number_2=findViewById(R.id.verificationcode_box);
        verify=findViewById(R.id.verfiy_button6);
        progressBar2=findViewById(R.id.progressBar2);
        feedback2=findViewById(R.id.feedback_textview2);
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        VerificationID =getIntent().getStringExtra("AuthCredentials");

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verificationcode=phone_number_2.getText().toString();
                if(verificationcode.isEmpty())
                {
                    feedback2.setText("Please enter the verification code to continue.");
                    feedback2.setVisibility(View.VISIBLE);
                    progressBar2.setVisibility(View.VISIBLE);
                    verify.setEnabled(false);
                }
                else
                {
                    PhoneAuthCredential credential= PhoneAuthProvider.getCredential(VerificationID, verificationcode);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerificationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            goHome();
                            //startActivity(new Intent(VerificationActivity.this, HomeActivity.class));
                            //finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                feedback2.setText("An error occurred when trying to verify..");
                                feedback2.setVisibility(View.VISIBLE);
                            }
                        }
                        progressBar2.setVisibility(View.VISIBLE);
                        verify.setEnabled(false);
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (currentUser != null) {
            goHome();
        }
    }

    public void goHome()
    {
        Intent Home=new Intent(VerificationActivity.this, HomeActivity.class);
        startActivity(Home);
        finish();
    }
}
