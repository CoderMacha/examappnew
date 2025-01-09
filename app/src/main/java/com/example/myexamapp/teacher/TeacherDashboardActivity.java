package com.example.myexamapp.teacher;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.myexamapp.R;
import com.example.myexamapp.ScheduledTestsActivity;
import com.example.myexamapp.UploadQuizActivity;
import com.example.myexamapp.databinding.ActivityTeacherDashboardBinding;
import com.example.myexamapp.student.ClassNotesActivity;
import java.util.Calendar;

public class TeacherDashboardActivity extends AppCompatActivity {

    private ActivityTeacherDashboardBinding binding;
    private long mainTestTimestamp;
    private static final String TAG = "TeacherDashboardActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeacherDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setStatusBarColor(R.color.statusbar);

        binding.cardUploadNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Upload Notes button clicked");
                Intent intent = new Intent(TeacherDashboardActivity.this, AddClassNotes.class);
                startActivity(intent);
            }
        });

        binding.cardUploadQuizz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Upload Quiz button clicked");
                Intent intent = new Intent(TeacherDashboardActivity.this, UploadQuizActivity.class);
                startActivity(intent);
            }
        });

        binding.cardSeeSchduledTests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TeacherDashboardActivity.this, ScheduledTestsActivity.class);
                startActivity(intent);
            }
        });


        binding.btnScheduleMainTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

    }

    private void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();

        new DatePickerDialog(TeacherDashboardActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(TeacherDashboardActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        mainTestTimestamp = date.getTimeInMillis();
                        saveMainTestSchedule();
                        launchCategoryActivity();
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

//    private void saveMainTestSchedule() {
//
//        getSharedPreferences("examAppPrefs", MODE_PRIVATE).edit()
//                .putLong("mainTestTimestamp", mainTestTimestamp)
//                .apply();
//
//        Toast.makeText(this, "Main Test Scheduled", Toast.LENGTH_SHORT).show();
//    }

    private void saveMainTestSchedule() {
        String testKey = "mainTestTimestamp_" + System.currentTimeMillis();
        getSharedPreferences("examAppPrefs", MODE_PRIVATE).edit()
                .putLong(testKey, mainTestTimestamp)
                .apply();

        Toast.makeText(this, "Main Test Scheduled", Toast.LENGTH_SHORT).show();
    }


    private void launchCategoryActivity() {
        Intent intent = new Intent(this, TeacherMainActivity.class);
        intent.putExtra("mainTestTimestamp", mainTestTimestamp);
        startActivity(intent);
    }
    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, color));
        }
        }
}
