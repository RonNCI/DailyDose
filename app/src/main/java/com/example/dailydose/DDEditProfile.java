package com.example.dailydose;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DDEditProfile extends AppCompatActivity {

    private EditText dd_editTextUpdateName, dd_editTextUpdateEmail, dd_editTextUpdatePassword;
    private Button dd_saveButton;
    private String dd_name, dd_email, dd_password;
    private FirebaseAuth dd_authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ddedit_profile);

        //getting id from layout
        dd_editTextUpdateName = findViewById(R.id.dd_editname);
        dd_editTextUpdateEmail = findViewById(R.id.dd_editemail);
        dd_editTextUpdatePassword = findViewById(R.id.dd_editpassword);
        dd_saveButton = findViewById(R.id.dd_editSaveBttn);

        //current user
        dd_authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = dd_authProfile.getCurrentUser();

        showProfile(firebaseUser);

        //update profile
        dd_saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upateProfile(firebaseUser);
            }
        });
    }

    //method to update user profile
    private void upateProfile(FirebaseUser firebaseUser) {
        String textFullName = dd_editTextUpdateName.getText().toString();
        String textEmail = dd_editTextUpdateEmail.getText().toString();
        String textPwd = dd_editTextUpdatePassword.getText().toString();

        //error for textfields
        if(TextUtils.isEmpty(textFullName)){
            Toast.makeText(DDEditProfile.this, "Please enter your full name", Toast.LENGTH_SHORT).show();
            dd_editTextUpdateName.setError("Full Name is required");
            dd_editTextUpdateName.requestFocus();
        } else if(TextUtils.isEmpty(textEmail)){
            Toast.makeText(DDEditProfile.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            dd_editTextUpdateEmail.setError("Email is required");
            dd_editTextUpdateEmail.requestFocus();
        } else if(TextUtils.isEmpty(textPwd)){
            Toast.makeText(DDEditProfile.this, "Please enter a password", Toast.LENGTH_SHORT).show();
            dd_editTextUpdatePassword.setError("Password is required");
            dd_editTextUpdatePassword.requestFocus();
        }else{
            DDReadWriteUserDetails writeUserDetails = new DDReadWriteUserDetails(dd_name, dd_email, dd_password);
            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("UserDetails");

            String userID = firebaseUser.getUid();

            referenceProfile.child(userID).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                        //updates user details
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().build();
                        firebaseUser.updateProfile(profileUpdates);

                        Toast.makeText(DDEditProfile.this, "Update Succesfull", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DDEditProfile.this, DDProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }else
                        try{
                            throw task.getException();
                        }catch (Exception e){
                            Toast.makeText(DDEditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                }
            });
        }
    }

    private void showProfile(FirebaseUser firebaseUser) {
        String userIDofRegistered = firebaseUser.getUid();

        //get user details
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("UserDetails");

        referenceProfile.child(userIDofRegistered).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DDReadWriteUserDetails readUserDetails = snapshot.getValue(DDReadWriteUserDetails.class);
                if(readUserDetails!=null){
                    dd_name = firebaseUser.getDisplayName();
                    dd_email = firebaseUser.getEmail();
                    dd_password = readUserDetails.password;
                    String fullName = readUserDetails.name;

                    dd_editTextUpdateName.setText(fullName);
                    dd_editTextUpdateEmail.setText(dd_email);
                    dd_editTextUpdatePassword.setText(dd_password);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DDEditProfile.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

}