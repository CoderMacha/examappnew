package com.example.myexamapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import com.example.myexamapp.Models.QuestionModelA;
import com.example.myexamapp.databinding.ActivityQuestionsActivityBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuestionsActivityA extends AppCompatActivity {

    ActivityQuestionsActivityBinding binding;
    private ArrayList<QuestionModelA> list;
    private int count = 0;
    private int position = 0;
    private int score = 0;
    CountDownTimer timer;

    FirebaseDatabase database;
    String categoryName;
    private int set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        database = FirebaseDatabase.getInstance();
        categoryName = getIntent().getStringExtra("categoryName");
        set = getIntent().getIntExtra("setNum", 1);
        list = new ArrayList<>();

        resetTimer();
        timer.start();

        database.getReference().child("Sets").child(categoryName).child("questions")
                .orderByChild("setNum").equalTo(set)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                QuestionModelA modelA = dataSnapshot.getValue(QuestionModelA.class);
                                list.add(modelA);
                            }

                            if (!list.isEmpty()) {
                                for (int i = 0; i < 4; i++) {
                                    binding.optionContainers.getChildAt(i).setOnClickListener(view -> checkAnsw((Button) view));
                                }

                                playAnimation(binding.question, 0, list.get(position).getQuestion());

                                binding.btnNext.setOnClickListener(v -> {
                                    binding.btnNext.setEnabled(false);
                                    binding.btnNext.setAlpha(0.3f);
                                    enableOption(true);
                                    position++;

                                    if (position == list.size()) {
                                        Intent intent = new Intent(QuestionsActivityA.this, ScoreActivityA.class);
                                        intent.putExtra("correctAnsw", score);
                                        intent.putExtra("totalQuestion", list.size());
                                        startActivity(intent);
                                        finish();
                                        return;
                                    }

                                    count = 0;
                                    playAnimation(binding.question, 0, list.get(position).getQuestion());
                                });
                            } else {
                                Toast.makeText(QuestionsActivityA.this, "No questions available.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(QuestionsActivityA.this, "No questions available.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(QuestionsActivityA.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void resetTimer() {
        timer = new CountDownTimer(30000, 1000) {
            private boolean alertShown = false;

            @Override
            public void onTick(long l) {
                long secondsRemaining = l / 1000;
                binding.timer.setText(String.valueOf(secondsRemaining));

                if (secondsRemaining <= 10 && !alertShown) {
                    new AlertDialog.Builder(QuestionsActivityA.this)
                            .setTitle("Warning")
                            .setMessage("Only " + secondsRemaining + " seconds remaining!")
                            .setPositiveButton("OK", null)
                            .show();

                    alertShown = true;
                }
            }

            @Override
            public void onFinish() {
                Toast.makeText(QuestionsActivityA.this, "Time's Up!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(QuestionsActivityA.this, ScoreActivityA.class);
                intent.putExtra("correctAnsw", score);
                intent.putExtra("totalQuestion", list.size());
                startActivity(intent);
                finish();
            }
        };
    }

    private void enableOption(boolean enable) {
        for (int i = 0; i < 4; i++) {
            binding.optionContainers.getChildAt(i).setEnabled(enable);

            if (enable) {
                binding.optionContainers.getChildAt(i).setBackgroundResource(R.drawable.btn_option_back);
            }
        }
    }

    private void playAnimation(View view, int value, String data) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animation) {
                        if (value == 0 && count < 4) {
                            String option = "";

                            if (count == 0) {
                                option = list.get(position).getOptionA();
                            } else if (count == 1) {
                                option = list.get(position).getOptionB();
                            } else if (count == 2) {
                                option = list.get(position).getOptionC();
                            } else if (count == 3) {
                                option = list.get(position).getOptionD();
                            }
                            playAnimation(binding.optionContainers.getChildAt(count), 0, option);
                            count++;
                        }
                    }

                    @Override
                    public void onAnimationEnd(@NonNull Animator animation) {
                        if (value == 0) {
                            try {
                                ((TextView) view).setText(data);
                                binding.numIndicator.setText(position + 1 + "/" + list.size());
                            } catch (Exception e) {
                                ((Button) view).setText(data);
                            }

                            view.setTag(data);
                            playAnimation(view, 1, data);
                        }
                    }

                    @Override
                    public void onAnimationCancel(@NonNull Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(@NonNull Animator animation) {
                    }
                });
    }

    private void checkAnsw(Button selectedOption) {
        enableOption(false);
        binding.btnNext.setEnabled(true);
        binding.btnNext.setAlpha(1);

        if (selectedOption.getText().toString().equals(list.get(position).getCorrectAnsw())) {
            score++;
            selectedOption.setBackgroundResource(R.drawable.right_answ);
        } else {
            selectedOption.setBackgroundResource(R.drawable.wrong_answ);

            Button correctOption = (Button) binding.optionContainers.findViewWithTag(list.get(position).getCorrectAnsw());
            correctOption.setBackgroundResource(R.drawable.right_answ);
        }
    }
}
