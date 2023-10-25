package com.example.dailydose;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class DDForgotPassword extends AppCompatActivity {
    EditText dd_resetPass;
    Button dd_resetBttn;
    FirebaseAuth dd_mAuth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

        //declaring firebase auth and id
        dd_mAuth = FirebaseAuth.getInstance();
        dd_resetBttn = findViewById(R.id.forpassresetbut);
        dd_resetBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetpass();
            }
        });
    }

    //Reset password method
    public void resetpass() {
        dd_resetPass = findViewById(R.id.forpassmail);
        String email = dd_resetPass.getText().toString();
        if (email.isEmpty()) {
            dd_resetPass.setError("Enter an email address");
            dd_resetPass.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            dd_resetPass.setError("Enter a valid email address");
            dd_resetPass.requestFocus();
        } else {
            dd_mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Password reset link sent. Check your inbox", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(DDForgotPassword.this, DDLoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}