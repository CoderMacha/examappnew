package com.example.myexamapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.myexamapp.Adapters.CategoryAdapterA;
import com.example.myexamapp.Models.CategoryModelA;
import com.example.myexamapp.databinding.ActivityMainBinding;
import com.example.myexamapp.home.HomeScreen;
import com.example.myexamapp.user.DashboardActivity;
import com.example.myexamapp.user.UpdateProfile;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity  {

    ActivityMainBinding binding;
    FirebaseDatabase database;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    ArrayList<CategoryModelA> list;
    CategoryAdapterA adapter;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.d(TAG, "MainActivity created");

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        database= FirebaseDatabase.getInstance();
        list = new ArrayList<>();

        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        binding.recyCategoryA.setLayoutManager(layoutManager);
        adapter = new CategoryAdapterA(this,list);
        binding.recyCategoryA.setAdapter(adapter);

        database.getReference().child("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    list.clear();
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){

//                        CategoryModel model = dataSnapshot.getValue(CategoryModel.class);
//                        list.add(model);

                        list.add(new CategoryModelA(

                                dataSnapshot.child("categoryName").getValue().toString(),
                                dataSnapshot.child("categoryImage").getValue().toString(),
                                dataSnapshot.getKey(),
                                Integer.parseInt(dataSnapshot.child("setNum").getValue().toString())
                        ));
                    }

                    adapter.notifyDataSetChanged();

                } else {

                    Toast.makeText(MainActivity.this, "category not exits", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

}