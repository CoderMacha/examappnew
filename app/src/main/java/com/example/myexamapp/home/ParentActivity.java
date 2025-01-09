package com.example.myexamapp.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.example.myexamapp.R;
import com.example.myexamapp.auth.LoginActivity;
import com.example.myexamapp.auth.SignUpActivity;
import com.example.myexamapp.onboarding.NavigationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ParentActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private AppCompatTextView login, signup;

    final String PREFS_NAME = "MyPrefsFile";
    boolean my_first_time = true;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null && currentUser.isEmailVerified()){
            //TODO: User is currently signed in
            startActivity(new Intent(ParentActivity.this, HomeScreen.class));
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true)) {
            // first time task
            // have the onboarding activity for the user
            settings.edit().putBoolean("my_first_time", false).apply();
            startActivity(new Intent(ParentActivity.this, NavigationActivity.class));
            // record the fact that the app has been started at least once
            finish();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusbar));
        }
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);

        login.setOnClickListener(v -> {
            //start the Login Activity for the user to get log in back to activity
            startActivity(new Intent(ParentActivity.this, LoginActivity.class));
            //finish();
        });

        signup.setOnClickListener(v -> {
            //start the Login Activity for the user to get log in back to activity
            startActivity(new Intent(ParentActivity.this, SignUpActivity.class));
            //finish();
        });
    }
}
