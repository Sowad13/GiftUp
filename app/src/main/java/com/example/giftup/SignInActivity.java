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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SignInActivity extends AppCompatActivity {

    //Member Variables
    EditText phone_number;
    Button submit;
    ProgressBar progressBar;
    TextView feedback;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks onVerificationStateChangedCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //.xml element assigning
        phone_number=findViewById(R.id.plaintext_phone_number);
        submit=findViewById(R.id.verfiy_button);
        progressBar=findViewById(R.id.progressBar);
        feedback=findViewById(R.id.feedback_textview);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_number_string=phone_number.getText().toString();
                String complete_number="+88"+phone_number_string;
                if(phone_number_string.isEmpty())
                {
                    feedback.setText("Please enter your number to continue.");
                    feedback.setVisibility(View.VISIBLE);
                }
                else
                {
                    submit.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(complete_number,60, TimeUnit.SECONDS, SignInActivity.this,onVerificationStateChangedCallbacks);

                }
            }
        });

        onVerificationStateChangedCallbacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                feedback.setText("Failed to verify, please try again.");
                feedback.setVisibility(View.VISIBLE);
                submit.setEnabled(true);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
               super.onCodeSent(verificationId, token);
                Intent verificationIntent = new Intent(SignInActivity.this, VerificationActivity.class);
                verificationIntent.putExtra("AuthCredentials", verificationId);
               startActivity(verificationIntent);
            }
        };
    }

    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                feedback.setText("An erro occurred when trying to verify..");
                                feedback.setVisibility(View.VISIBLE);
                            }
                        }
                        progressBar.setVisibility(View.VISIBLE);
                        submit.setEnabled(false);
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (currentUser != null) {
            Intent x=new Intent(SignInActivity.this,HomeActivity.class);
            startActivity(x);
            finish();
        }
    }
}
