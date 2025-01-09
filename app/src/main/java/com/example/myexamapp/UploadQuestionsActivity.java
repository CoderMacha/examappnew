package com.example.myexamapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myexamapp.Models.UploadQuestionModel;
import com.example.myexamapp.Models.TestDetails;
import com.example.myexamapp.databinding.ActivityUploadQuestionsBinding;

import java.util.ArrayList;
import java.util.List;

public class UploadQuestionsActivity extends AppCompatActivity {

    private ActivityUploadQuestionsBinding binding;
    private TestDetails testDetails;
    private List<UploadQuestionModel> questionsList = new ArrayList<>();
    private int currentQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadQuestionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve the TestDetails object
        testDetails = (TestDetails) getIntent().getSerializableExtra("testDetails");

        if (testDetails == null) {
            Toast.makeText(this, "Test details missing. Please restart the test.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Display test info
        binding.textViewTestInfo.setText(String.format(
                "Test Duration: %d mins\nTotal Questions: %d\nTotal Marks: %d",
                testDetails.getTestDuration(),
                testDetails.getTotalQuestions(),
                testDetails.getTotalMarks()
        ));

        // Handle "Add Question" button click
        binding.btnAddQuestion.setOnClickListener(v -> {
            String questionText = binding.editQuestion.getText().toString().trim();
            String optionA = binding.editOptionA.getText().toString().trim();
            String optionB = binding.editOptionB.getText().toString().trim();
            String optionC = binding.editOptionC.getText().toString().trim();
            String optionD = binding.editOptionD.getText().toString().trim();
            String correctOption = binding.spinnerCorrectOption.getSelectedItem().toString();

            if (TextUtils.isEmpty(questionText) || TextUtils.isEmpty(optionA) || TextUtils.isEmpty(optionB) ||
                    TextUtils.isEmpty(optionC) || TextUtils.isEmpty(optionD) || correctOption.equals("Select")) {
                Toast.makeText(this, "Please fill all fields and select the correct answer.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Add question to list
            questionsList.add(new UploadQuestionModel(questionText, optionA, optionB, optionC, optionD, correctOption));
            currentQuestionIndex++;

            // Check if we've reached the total number of questions
            if (currentQuestionIndex >= testDetails.getTotalQuestions()) {
                Toast.makeText(this, "All questions added! Test is ready.", Toast.LENGTH_SHORT).show();
                saveQuestionsToDatabase();
                finish(); // Go back to the main screen or navigate to another activity
            } else {
                Toast.makeText(this, "Question added. Add the next question.", Toast.LENGTH_SHORT).show();
                clearInputs();
            }
        });
    }

    private void clearInputs() {
        binding.editQuestion.setText("");
        binding.editOptionA.setText("");
        binding.editOptionB.setText("");
        binding.editOptionC.setText("");
        binding.editOptionD.setText("");
        binding.spinnerCorrectOption.setSelection(0);
    }

    private void saveQuestionsToDatabase() {
        // Save the list of questions to the database (placeholder logic)
        for (UploadQuestionModel question : questionsList) {
            // Example: Save each question to SQLite or Firebase
            // database.saveQuestion(question);
        }
        Toast.makeText(this, "Test questions saved successfully!", Toast.LENGTH_SHORT).show();
    }
}
