package com.example.dailydose;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
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

public class DDLoginActivity extends AppCompatActivity {
    String dd_getPassword;
    private EditText dd_editTextLoginEmail, dd_editTextLoginPwd;
    private FirebaseAuth dd_authProfile;
    Button dd_signinButton, dd_signupButton;
    private CheckBox dd_logpasschk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dd_editTextLoginEmail = findViewById(R.id.dd_loginEmail);
        dd_editTextLoginPwd = findViewById(R.id.dd_loginPassword);
        dd_signinButton = findViewById(R.id.signinButton);
        dd_authProfile = FirebaseAuth.getInstance();

        //invoking methods
        hidepass();
        valideEmail();
        validatePassword();
        forgotPassword();
        sendHelp();


        dd_signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textEmail = dd_editTextLoginEmail.getText().toString();
                String textPwd = dd_editTextLoginPwd.getText().toString();

                //display error message if conditions are not met
                if(TextUtils.isEmpty(textEmail)){
                    Toast.makeText(DDLoginActivity.this, "Please enter your email",Toast.LENGTH_SHORT).show();
                    dd_editTextLoginEmail.setError("Email is Required");
                    dd_editTextLoginEmail.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(DDLoginActivity.this, "Please re-enter your email",Toast.LENGTH_SHORT).show();
                    dd_editTextLoginEmail.setError("Valid email is Required");
                    dd_editTextLoginEmail.requestFocus();
                }else if(TextUtils.isEmpty(textPwd)){
                    Toast.makeText(DDLoginActivity.this, "Please enter your password",Toast.LENGTH_SHORT).show();
                    dd_editTextLoginPwd.setError("password is required");
                    dd_editTextLoginPwd.requestFocus();
                }else{
                    loginUser(textEmail, textPwd);
                }
            }
        });

        //transfers to signup interface
        dd_signupButton = findViewById(R.id.registerButton);
        dd_signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DDLoginActivity.this, DDSignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser(String email, String password) {
        dd_authProfile.signInWithEmailAndPassword(email, password).addOnCompleteListener(DDLoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(DDLoginActivity.this, "Login Successfull",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DDLoginActivity.this, DDMainMenu.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(DDLoginActivity.this, "Login Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //checks if email is valid
    public Boolean valideEmail(){
        dd_editTextLoginEmail = findViewById(R.id.dd_loginEmail);
        String val = dd_editTextLoginEmail.getText().toString();
        if(val.isEmpty()){
            dd_editTextLoginEmail.setError("Email is empty");
            return false;
        }else{
            dd_editTextLoginEmail.setError(null);
            return true;
        }
    }

    //checks if password is valid
    public Boolean validatePassword(){
        dd_editTextLoginPwd = findViewById(R.id.dd_loginPassword);
        String val = dd_editTextLoginPwd.getText().toString();
        if(val.isEmpty()){
            dd_editTextLoginPwd.setError("Password is empty");
            return false;
        }else{
            dd_editTextLoginPwd.setError(null);
            return true;
        }
    }

    //hides password
    public void hidepass() {
        dd_logpasschk = findViewById(R.id.logpasschk);
        dd_logpasschk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                EditText loginPassword = findViewById(R.id.dd_loginPassword);
                dd_getPassword = loginPassword.getText().toString();
                if (b) {
                    loginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    loginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    //Forgot password method
    public void forgotPassword(){
        TextView fpText = findViewById(R.id.logintrouble);
        String text = "Forgot password? Click here";
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DDLoginActivity.this, DDForgotPassword.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };
        ss.setSpan(clickableSpan2, 11, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        fpText.setText(ss);
        fpText.setMovementMethod(LinkMovementMethod.getInstance());
    }
    //emails admin if user requires help
    public void sendHelp(){
        TextView helpText = findViewById(R.id.dd_Help);
        String text = "Trouble signing in? Click here";
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                sendmail();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };
        ss.setSpan(clickableSpan2, 11, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        helpText.setText(ss);
        helpText.setMovementMethod(LinkMovementMethod.getInstance());
    }
    //sends email to admin
    public void sendmail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String[] recipients = {"atronnelynot@gmail.com"};
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Sign In/Up Problem");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_CC, "mailcc@gmail.com");
        intent.setType("text/html");
        intent.setPackage("com.google.android.gm");
        startActivity(Intent.createChooser(intent, "Send mail"));
    }
}