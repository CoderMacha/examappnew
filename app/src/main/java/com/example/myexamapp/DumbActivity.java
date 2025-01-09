package com.example.myexamapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.myexamapp.databinding.ActivityDumbBinding;
import com.example.myexamapp.home.HomeScreen;
import com.example.myexamapp.teacher.TeacherMainActivity;
import com.example.myexamapp.user.DashboardActivity;
import com.example.myexamapp.user.UpdateProfile;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.FirebaseDatabase;

public class DumbActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityDumbBinding binding;
    FirebaseDatabase database;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityDumbBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav_bar, R.string.close_nav_bar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
//        navigationView.setCheckedItem(R.id.nav_exam_section);

        binding.btnstdnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(DumbActivity.this,MainActivity.class);
                startActivity(intent);


            }
        });

        binding.btntechr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DumbActivity.this, TeacherMainActivity.class);
                startActivity(intent);
            }
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
        if (id == R.id.nav_home) {
            startActivity(new Intent(getApplicationContext(), HomeScreen.class));
        } else if (id == R.id.nav_dashboard) {
            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
        } else if (id == R.id.nav_update_profile) {
            startActivity(new Intent(getApplicationContext(), UpdateProfile.class));
        }
        return true;
    }
}