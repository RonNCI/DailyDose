package com.example.dailydose;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DDSignupActivityTest {
    @Mock
    private FirebaseAuth mAuth;

    @Mock
    private FirebaseUser mUser;

    @Mock
    private Task<AuthResult> mTask;

    @Mock
    private DDReadWriteUserDetails mDetails;

    @Mock
    private DatabaseReference mDatabaseRef;

    private DDSignupActivity mActivity;

    @Before
    public void setUp() {
        mActivity = new DDSignupActivity();
        mActivity.firebaseAuth = mAuth;
    }

    @Test
    public void registerUser_WithValidInput_CallsCreateUserWithEmailAndPassword() {
        // Arrange
        String name = "John Doe";
        String email = "johndoe@example.com";
        String password = "password123";

        when(mAuth.getCurrentUser()).thenReturn(mUser);
        when(mAuth.createUserWithEmailAndPassword(email, password)).thenReturn(mTask);
        when(mTask.isSuccessful()).thenReturn(true);

        // Act
        mActivity.registerUser(name, email, password);

        // Assert
        verify(mAuth).createUserWithEmailAndPassword(email, password);
    }

    @Test
    public void registerUser_WithValidInput_CallsUpdateProfile() {
        // Arrange
        String name = "John Doe";
        String email = "johndoe@example.com";
        String password = "password123";

        when(mAuth.getCurrentUser()).thenReturn(mUser);
        when(mAuth.createUserWithEmailAndPassword(email, password)).thenReturn(mTask);
        when(mTask.isSuccessful()).thenReturn(true);

        // Act
        mActivity.registerUser(name, email, password);

        // Assert
        verify(mUser).updateProfile(any(UserProfileChangeRequest.class));
    }

    @Test
    public void registerUser_WithValidInput_SavesUserDetails() {
        // Arrange
        String name = "John Doe";
        String email = "johndoe@example.com";
        String password = "password123";

        when(mAuth.getCurrentUser()).thenReturn(mUser);
        when(mAuth.createUserWithEmailAndPassword(email, password)).thenReturn(mTask);
        when(mTask.isSuccessful()).thenReturn(true);

        // Act
        mActivity.registerUser(name, email, password);

        // Assert
        verify(mDetails).save();
    }

    @Test
    public void registerUser_WithValidInput_SendsEmailVerification() {
        // Arrange
        String name = "John Doe";
        String email = "johndoe@example.com";
        String password = "password123";

        when(mAuth.getCurrentUser()).thenReturn(mUser);
        when(mAuth.createUserWithEmailAndPassword(email, password)).thenReturn(mTask);
        when(mTask.isSuccessful()).thenReturn(true);

        // Act
        mActivity.registerUser(name, email, password);

        // Assert
        verify(mUser).sendEmailVerification();
    }
}