package com.example.dailydose;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


public class DDMainMenu extends AppCompatActivity {
    CardView dd_cd_profile, dd_cd_alarm, dd_cd_addevent, dd_cd_logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Daily Dose");
        setContentView(R.layout.activity_mainmenu);

        dd_cd_profile = findViewById(R.id.dd_cardProfile);
        dd_cd_alarm = findViewById(R.id.dd_cardAlarm);
        dd_cd_addevent = findViewById(R.id.dd_cardAddEvent);
        dd_cd_logout = findViewById(R.id.dd_cardLogout);

        dd_cd_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DDMainMenu.this, DDProfileActivity.class);
                startActivity(intent);
            }
        });

        dd_cd_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DDMainMenu.this, DDMainActivity.class);
                startActivity(intent);
            }
        });

        dd_cd_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DDMainMenu.this, DDLoginActivity.class);
                startActivity(intent);
            }
        });

        dd_cd_addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DDMainMenu.this, DDAddEvent.class);
                startActivity(intent);
            }
        });
    }
}
