package com.example.dailydose;

import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DDAddEvent extends AppCompatActivity {

    private CalendarView dd_calendarView;
    private EditText dd_editText;
    private String dd_nameTitle;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ddadd_event);

        //getting id from layout
        dd_calendarView = findViewById(R.id.calendarView);
        dd_editText = findViewById(R.id.editText);

        dd_calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                dd_nameTitle = Integer.toString(i) + Integer.toString(i1+1) + Integer.toString(i2);
                calendarClicked();
            }
        });
        //saves in database
        databaseReference = FirebaseDatabase.getInstance().getReference("Calendar");
    }

    //medthod to save events in calendar
    private void calendarClicked(){
        databaseReference.child(dd_nameTitle).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null){
                    dd_editText.setText(snapshot.getValue().toString());
                }else {
                    dd_editText.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void buttonSaveEvent(View view){
        databaseReference.child(dd_nameTitle).setValue(dd_editText.getText().toString());
    }
}