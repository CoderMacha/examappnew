package com.example.myexamapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.myexamapp.databinding.ActivityUploadQuizBinding;
import com.example.myexamapp.teacher.TeacherDashboardActivity;
import com.example.myexamapp.teacher.TeacherMainActivity;

public class UploadQuizActivity extends AppCompatActivity {

    private ActivityUploadQuizBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUploadQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
setStatusBarColor(R.color.statusbar);
        binding.cardPracticeTestUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UploadQuizActivity.this, TeacherMainActivity.class);
                startActivity(intent);
            }
        });

        binding.cardMainTestUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(UploadQuizActivity.this, MainTestActivity.class);
                startActivity(intent);
            }
        });
    }
    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, color));
        }
    }
}