package com.example.dailydose;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dailydose.databinding.ActivityMainBinding;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

public class DDMainActivity extends AppCompatActivity {

    private ActivityMainBinding dd_binding;
    private MaterialTimePicker dd_timePicker;
    private Calendar dd_calendar;
    private AlarmManager dd_alarmManager;
    private PendingIntent dd_pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dd_binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(dd_binding.getRoot());

        //starts method
        createNotificationChannel();

        //sets timer when clicked
        dd_binding.selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dd_timePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(12)
                        .setMinute(0)
                        .setTitleText("Select Alarm Time")
                        .build();

                dd_timePicker.show(getSupportFragmentManager(), "androidknowledge");
                //changes time after user sets time
                dd_timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dd_timePicker.getHour() > 12){
                            dd_binding.selectTime.setText(
                                    String.format("%02d",(dd_timePicker.getHour()-12)) +":"+ String.format("%02d", dd_timePicker.getMinute())+"PM"
                            );
                        } else {
                            dd_binding.selectTime.setText(dd_timePicker.getHour()+":" + dd_timePicker.getMinute()+ "AM");
                        }
                        dd_calendar = Calendar.getInstance();
                        dd_calendar.set(Calendar.HOUR_OF_DAY, dd_timePicker.getHour());
                        dd_calendar.set(Calendar.MINUTE, dd_timePicker.getMinute());
                        dd_calendar.set(Calendar.SECOND, 0);
                        dd_calendar.set(Calendar.MILLISECOND, 0);
                    }
                });
            }
        });

        dd_binding.setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dd_alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(DDMainActivity.this, DDAlarmReceiver.class);
                dd_pendingIntent = PendingIntent.getBroadcast(DDMainActivity.this, 0, intent, 0);

                dd_alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, dd_calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, dd_pendingIntent);
                Toast.makeText(DDMainActivity.this, "Alarm Set", Toast.LENGTH_SHORT).show();
            }
        });

        dd_binding.cancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DDMainActivity.this, DDAlarmReceiver.class);
                dd_pendingIntent = PendingIntent.getBroadcast(DDMainActivity.this, 0, intent, 0);

                if (dd_alarmManager == null){
                    dd_alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                }
                dd_alarmManager.cancel(dd_pendingIntent);
                Toast.makeText(DDMainActivity.this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "akchannel";
            String desc = "Channel for Alarm Manager";
            int imp = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("androidknowledge", name, imp);
            channel.setDescription(desc);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}