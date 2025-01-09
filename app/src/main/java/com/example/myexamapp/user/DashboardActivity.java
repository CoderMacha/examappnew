package com.example.myexamapp.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myexamapp.R;
import com.example.myexamapp.home.HomeScreen;
import com.example.myexamapp.student.StudentDashboardActivity;
import com.example.myexamapp.teacher.TeacherDashboardActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav_bar, R.string.close_nav_bar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_dashboard);

        gotoDashboard();
    }


    private void gotoDashboard() {
        progressDialog.show();

        String targetEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();


        CollectionReference studentCollection = FirebaseFirestore.getInstance().collection("student");
        studentCollection.whereEqualTo("email", targetEmail).limit(1).get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
//                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(getApplicationContext(), StudentDashboardActivity.class));
                    }

                    progressDialog.dismiss();
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error getting student: " + e.getMessage());
                    progressDialog.dismiss();
                });


        CollectionReference teacherCollection = FirebaseFirestore.getInstance().collection("teacher");
        teacherCollection.whereEqualTo("email", targetEmail).limit(1).get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
//                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_SHORT).show();
                    } else {
                            startActivity(new Intent(getApplicationContext(), TeacherDashboardActivity.class));
                    }

                    progressDialog.dismiss();
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error getting student: " + e.getMessage());
                    progressDialog.dismiss();
                });

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_dashboard) {}
        else if( id == R.id.nav_home) {
            startActivity(new Intent(getApplicationContext(), HomeScreen.class));
        }
        return true;
    }
}
