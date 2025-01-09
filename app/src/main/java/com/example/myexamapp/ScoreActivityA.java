package com.example.myexamapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.myexamapp.databinding.ActivityScoreActivityBinding;
import com.example.myexamapp.student.StudentDashboardActivity;

public class ScoreActivityA extends AppCompatActivity {

    ActivityScoreActivityBinding binding;
    private static final String TAG = "ScoreActivityA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScoreActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Get data from intent
        int correct = getIntent().getIntExtra("correctAnsw", 0);
        int totalQuestion = getIntent().getIntExtra("totalQuestion", 0);

        Log.d(TAG, "Correct answers: " + correct);
        Log.d(TAG, "Total questions: " + totalQuestion);

        int wrong = totalQuestion - correct;
        double percentageCorrect = totalQuestion > 0 ? ((double) correct / totalQuestion) * 100 : 0;

        // Set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusbar));
        }

        // Update UI elements
        binding.totalRight.setText(String.valueOf(correct));
        binding.totalWrong.setText(String.valueOf(wrong));
        binding.totalQuestion.setText(String.valueOf(totalQuestion));

        // Update the image based on score
        if (totalQuestion == 0) {
            binding.imageView4.setImageResource(R.drawable.placeholder);
        } else if (correct == totalQuestion) {
            binding.imageView4.setImageResource(R.drawable.trophy_win);
        } else if (percentageCorrect < 50) {
            binding.imageView4.setImageResource(R.drawable.sad_emoji);
        } else if (percentageCorrect >= 50) {
            binding.imageView4.setImageResource(R.drawable.happiness_emoji);
        } else {
            binding.imageView4.setImageResource(R.drawable.placeholder);
        }

        // Button click listeners
        binding.btnRetry.setOnClickListener(v -> {
            Log.d(TAG, "Retry button clicked");
            Intent intent = new Intent(ScoreActivityA.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        binding.btnQuit.setOnClickListener(v -> {
            Log.d(TAG, "Quit button clicked");
            Intent intent = new Intent(ScoreActivityA.this, StudentDashboardActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
