package com.example.giftup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.internal.InternalTokenProvider;

import java.util.concurrent.TimeUnit;

public class SignInActivity extends AppCompatActivity {

    //Member Variables
    EditText phone_number;
    Button submit;
    ProgressBar progressBar;
    TextView feedback;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //.xml element assigning
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        phone_number=findViewById(R.id.plaintext_phone_number);
        submit=findViewById(R.id.verfiy_button);
        progressBar=findViewById(R.id.progressBar);
        feedback=findViewById(R.id.feedback_textview);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_number_string=phone_number.getText().toString();
                String country_code="+88";
                String complete_number=country_code + phone_number_string;
                Toast.makeText(SignInActivity.this, "Phone Number: "+complete_number, Toast.LENGTH_SHORT).show();
                if(phone_number_string.isEmpty())
                {
                    feedback.setText("Please enter your number to continue.");
                    feedback.setVisibility(View.VISIBLE);
                }
                else
                {
                    submit.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(complete_number,60, TimeUnit.SECONDS, SignInActivity.this, mCallbacks);

                }
            }
        });



        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            boolean flag=false;
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                if(!flag)
                {
                    Toast.makeText(SignInActivity.this,"Not Verified",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(SignInActivity.this,"Verified",Toast.LENGTH_LONG).show();
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }


            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(SignInActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();

                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(SignInActivity.this,"SMS quota exceeded.",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(SignInActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();

                }
                //feedback.setText("Failed to verify, please try again.");
                //feedback.setVisibility(View.VISIBLE);
                //submit.setEnabled(true);
                //progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
               super.onCodeSent(verificationId, token);
                flag=true;
                Toast.makeText(SignInActivity.this,"Code Sent.",Toast.LENGTH_SHORT).show();
                Intent verificationIntent = new Intent(SignInActivity.this, VerificationActivity.class);
                verificationIntent.putExtra("AuthCredentials", verificationId);
                submit.setEnabled(true);
                progressBar.setVisibility(View.INVISIBLE);
               startActivity(verificationIntent);
               finish();

            }
        };
    }

    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent Home=new Intent(SignInActivity.this,HomeActivity.class);
                            startActivity(Home);
                            Home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            Home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                feedback.setText("An error occurred when trying to verify..");
                                feedback.setVisibility(View.VISIBLE);
                            }
                        }
                        progressBar.setVisibility(View.VISIBLE);
                        submit.setEnabled(true);
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (currentUser != null) {
            Intent homeIntent=new Intent(SignInActivity.this,HomeActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(homeIntent);
            finish();
        }
    }
}
