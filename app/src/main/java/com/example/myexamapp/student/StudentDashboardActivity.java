package com.example.myexamapp.student;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.myexamapp.Classes.NotificationReceiver;
import com.example.myexamapp.MainActivity;
import com.example.myexamapp.R;
import com.example.myexamapp.databinding.ActivityStudentDashboardBinding;

public class StudentDashboardActivity extends AppCompatActivity {

    private ActivityStudentDashboardBinding binding;
    private static final String TAG = "StudentDashboardActivity";
    private long mainTestTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
         setStatusBarColor(R.color.statusbar);
        binding.cardPracticeTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Practice Test button clicked");
                Intent intent = new Intent(StudentDashboardActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        checkMainTestSchedule();

        binding.cardSeeSchduledTestsA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StudentDashboardActivity.this, StudentScheduledTestsActivity.class);
                startActivity(intent);
            }
        });

        binding.cardClassNotes.setOnClickListener(e -> {
            Intent intent = new Intent(StudentDashboardActivity.this, ClassNotesActivity.class);
            startActivity(intent);
        });
    }


    private void checkMainTestSchedule() {
        SharedPreferences prefs = getSharedPreferences("examAppPrefs", MODE_PRIVATE);
        mainTestTimestamp = prefs.getLong("mainTestTimestamp", -1);

        if (mainTestTimestamp != -1) {
            long currentTime = System.currentTimeMillis();
            long timeDifference = mainTestTimestamp - currentTime;

            if (timeDifference <= 30 * 60 * 1000) {
                // Enable the main test button if within 30 minutes
                binding.cardMainTest.setEnabled(true);
                binding.cardMainTest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "Main Test button clicked");
                        Intent intent = new Intent(StudentDashboardActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                });
            } else {
                binding.cardMainTest.setEnabled(false);
                // Schedule a notification 30 minutes before the main test
                scheduleMainTestNotification(timeDifference - 30 * 60 * 1000);
            }
        }
    }

    private void scheduleMainTestNotification(long delay) {
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent);

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("mainTestChannel", "Main Test Notifications", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Notifications for the main test");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, color));
        }
    }
}
