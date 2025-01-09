package com.example.myexamapp;

import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.myexamapp.databinding.ActivityScheduledTestsBinding;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class ScheduledTestsActivity extends AppCompatActivity {

    private ActivityScheduledTestsBinding binding;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> scheduledTestsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScheduledTestsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusbar));
        }
        scheduledTestsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, scheduledTestsList);
        binding.listViewScheduledTests.setAdapter(adapter);

        loadScheduledTests();
    }

    private void loadScheduledTests() {
        Map<String, ?> allEntries = getSharedPreferences("examAppPrefs", MODE_PRIVATE).getAll();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().startsWith("mainTestTimestamp_")) {
                long timestamp = (Long) entry.getValue();
                String formattedDate = sdf.format(new Date(timestamp));
                scheduledTestsList.add("Test: " + entry.getKey().replace("mainTestTimestamp_", "") + " - " + formattedDate);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
