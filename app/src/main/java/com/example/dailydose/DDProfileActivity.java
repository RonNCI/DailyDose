package com.example.dailydose;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DDProfileActivity extends AppCompatActivity {
    private TextView dd_textViewFullName, dd_textViewEmail, dd_textViewPwd;
    String dd_name, dd_email, dd_password;
    ImageView imageView;
    FirebaseAuth dd_authProfile;
    Button dd_mainMenuBttn, dd_logOutButton, dd_editProfileBttn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dd_textViewEmail = findViewById(R.id.profileEmail2);
        dd_textViewPwd = findViewById(R.id.profilePassword2);
        dd_textViewFullName = findViewById(R.id.profileName);
        dd_mainMenuBttn = findViewById(R.id.profileReturnBttn);
        dd_logOutButton = findViewById(R.id.logOutBttn1);
        dd_editProfileBttn = findViewById(R.id.ddProfileEdit);

        dd_authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = dd_authProfile.getCurrentUser();

        if(firebaseUser==null){
            Toast.makeText(DDProfileActivity.this,"This feature is not available at the moment",Toast.LENGTH_SHORT).show();
        }else{
            showUserProfile(firebaseUser);
        }

        dd_mainMenuBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainMenu();
            }
        });

        dd_logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginActivity();
            }
        });

        dd_editProfileBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DDProfileActivity.this, DDEditProfile.class);
                startActivity(intent);
            }
        });
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

       //Taking user details from database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserDetails");
        //ValueEventListener() keeps listening query or database
        //addListenerForSingleValueEvent() executes onDataChange()
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DDReadWriteUserDetails readUserDetails = snapshot.getValue(DDReadWriteUserDetails.class);
                if(readUserDetails!=null){
                    dd_name = firebaseUser.getDisplayName();
                    dd_email = firebaseUser.getEmail();
                    dd_password = readUserDetails.password;
                    String fullName = readUserDetails.name;

                    dd_textViewFullName.setText(fullName);
                    dd_textViewEmail.setText(dd_email);
                    dd_textViewPwd.setText(dd_password);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DDProfileActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void openLoginActivity() {
        Intent intent = new Intent(this, DDLoginActivity.class);
        startActivity(intent);
    }

    private void openMainMenu() {
        Intent intent = new Intent(this, DDMainMenu.class);
        startActivity(intent);
    }
}