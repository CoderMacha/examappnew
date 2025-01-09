package com.example.myexamapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myexamapp.Models.TestDetails;
import com.example.myexamapp.databinding.ActivityMainTestBinding;

public class MainTestActivity extends AppCompatActivity {

    ActivityMainTestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnStartTest.setOnClickListener(v -> {
            String duration = binding.editTestDuration.getText().toString();
            String numQuestions = binding.editQuestionsCount.getText().toString();
            String totalMarks = binding.editTotalMarks.getText().toString();
            String correctMarks = binding.editCorrectAnswerMarks.getText().toString();
            String wrongMarks = binding.editWrongAnswerMarks.getText().toString();

            if (duration.isEmpty() || numQuestions.isEmpty() || totalMarks.isEmpty() ||
                    correctMarks.isEmpty() || wrongMarks.isEmpty()) {
                Toast.makeText(MainTestActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                TestDetails testDetails = new TestDetails(
                        Integer.parseInt(duration),
                        Integer.parseInt(numQuestions),
                        Integer.parseInt(totalMarks),
                        Integer.parseInt(correctMarks),
                        Integer.parseInt(wrongMarks)
                );

                Intent intent = new Intent(MainTestActivity.this, UploadQuestionsActivity.class);
                intent.putExtra("testDetails", testDetails);
                startActivity(intent);
            }
        });
    }
}
