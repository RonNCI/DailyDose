package com.example.dailydose;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DDSignupActivity extends AppCompatActivity {
    public FirebaseAuth firebaseAuth;
    private EditText dd_editTextRegisterFullName;
    private EditText dd_editTextRegisterEmail;
    private EditText dd_editTextRegisterPwd;
    private String dd_getPassword;
    private static  final String TAG="DDSignupActivity";

    TextView loginRedirectText;
    Button signupBttn;
    CheckBox regpasschk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ddsignup);

        hidepass(); //hides password

        dd_editTextRegisterFullName = findViewById(R.id.dd_name);
        dd_editTextRegisterEmail = findViewById(R.id.dd_email);
        dd_editTextRegisterPwd = findViewById(R.id.dd_password);
        signupBttn = findViewById(R.id.dd_signupBttn);
        loginRedirectText = findViewById(R.id.dd_signinRedirect);

        signupBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get user data input

                String textFullName = dd_editTextRegisterFullName.getText().toString().trim();
                String textEmail = dd_editTextRegisterEmail.getText().toString().trim();
                String textPwd = dd_editTextRegisterPwd.getText().toString().trim();

                if(TextUtils.isEmpty(textFullName)){
                    Toast.makeText(DDSignupActivity.this, "Please enter your full name", Toast.LENGTH_SHORT).show();
                    dd_editTextRegisterFullName.setError("Full Name is required");
                    dd_editTextRegisterFullName.requestFocus();
                } else if(TextUtils.isEmpty(textEmail)){
                    Toast.makeText(DDSignupActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    dd_editTextRegisterEmail.setError("Email is required");
                    dd_editTextRegisterEmail.requestFocus();
                } else if(TextUtils.isEmpty(textPwd)){
                    Toast.makeText(DDSignupActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                    dd_editTextRegisterPwd.setError("Password is required");
                    dd_editTextRegisterPwd.requestFocus();
                }else{
                    registerUser(textFullName, textEmail, textPwd);
                }
            }

        });
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DDSignupActivity.this, DDLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    void registerUser(String textFullName, String textEmail, String textPwd){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textEmail, textPwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(DDSignupActivity.this, "User Succesfully Registered!", Toast.LENGTH_SHORT).show();
                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    //saves user data
                    DDReadWriteUserDetails saveDetails = new DDReadWriteUserDetails(textFullName, textEmail, textPwd);

                    //store data into "user" database
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserDetails");
                    //display user name in database
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textFullName).build();
                    firebaseUser.updateProfile(profileChangeRequest);

                    databaseReference.child(firebaseUser.getUid()).setValue(saveDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                //send email verification
                                firebaseUser.sendEmailVerification();

                                Toast.makeText(DDSignupActivity.this, "Account is registered, please verify email", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(DDSignupActivity.this, DDLoginActivity.class);
                                //stops user form going back to register
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(DDSignupActivity.this, "Account is registered, please verify email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        dd_editTextRegisterPwd.setError("This password is too weak.");
                        dd_editTextRegisterPwd.requestFocus();
                    } catch (FirebaseAuthUserCollisionException e){
                        dd_editTextRegisterPwd.setError("Email already registered, please use a new one");
                        dd_editTextRegisterPwd.requestFocus();
                    } catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(DDSignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    //Hide password method
    public void hidepass() {
        regpasschk = findViewById(R.id.regpasschk);
        regpasschk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dd_editTextRegisterEmail = findViewById(R.id.dd_password);
                dd_getPassword = dd_editTextRegisterEmail.getText().toString();
                if (b) {
                    dd_editTextRegisterEmail.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    dd_editTextRegisterEmail.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }
}
