//package com.example.myexamapp;
//
//
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.widget.Toast;
//
//import com.example.myexamapp.Adapters.GrideAdapter;
//import com.example.myexamapp.databinding.ActivitySetsBinding;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class SetsActivity extends AppCompatActivity {
//
//    ActivitySetsBinding binding;
//    FirebaseDatabase database;
//    GrideAdapter adapter;
//    int a=1;
//    String key;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivitySetsBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        // getSupportActionBar().hide();
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().hide();
//        }
//
//        database = FirebaseDatabase.getInstance();
//        key = getIntent().getStringExtra("key");
//        adapter=new GrideAdapter(getIntent().getIntExtra("sets", 0),
//                getIntent().getStringExtra("category"), key, new GrideAdapter.GridListener() {
//            @Override
//            public void addSets() {
//
//                database.getReference().child("categories").child(key)
//                        .child("setNum").setValue(getIntent().getIntExtra("sets",0)+a++).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//
//                                if(task.isSuccessful()){
//                                    adapter.sets++;
//                                    adapter.notifyDataSetChanged();
//                                }
//
//                                else{
//                                    Toast.makeText(SetsActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
//                                }
//
//                            }
//                        });
//
//            }
//        });
//
//        binding.gridView.setAdapter(adapter);
//    }
//}

package com.example.myexamapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.myexamapp.Adapters.GrideAdapter;
import com.example.myexamapp.teacher.TeacherDashboardActivity;
import com.example.myexamapp.databinding.ActivitySetsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class SetsActivity extends AppCompatActivity implements GrideAdapter.LongPressListener {

    ActivitySetsBinding binding;
    FirebaseDatabase database;
    GrideAdapter adapter;
    int a = 1;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusbar));
        }
        database = FirebaseDatabase.getInstance();
        key = getIntent().getStringExtra("key");

        adapter = new GrideAdapter(getIntent().getIntExtra("sets", 0),
                getIntent().getStringExtra("category"), key, new GrideAdapter.GridListener() {
            @Override
            public void addSets() {
                database.getReference().child("categories").child(key)
                        .child("setNum").setValue(getIntent().getIntExtra("sets", 0) + a++).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    adapter.sets++;
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(SetsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }, this); // Pass this as the LongPressListener

        binding.gridView.setAdapter(adapter);
    }

    @Override
    public void onSetLongPressed(int setNumber) {
        showScheduleDialog(setNumber);
    }

    private void showScheduleDialog(final int setNumber) {
        new AlertDialog.Builder(this)
                .setTitle("Schedule Test")
                .setMessage("Do you want to schedule set " + setNumber + " for the main test?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scheduleTest(setNumber);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SetsActivity.this, "No set selected", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void scheduleTest(int setNumber) {
        Intent intent = new Intent(SetsActivity.this, TeacherDashboardActivity.class);
        intent.putExtra("setNumber", setNumber);
        startActivity(intent);
    }
}
