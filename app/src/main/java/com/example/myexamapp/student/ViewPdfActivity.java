package com.example.myexamapp.student;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.myexamapp.R;
import com.github.barteksc.pdfviewer.PDFView;

public class ViewPdfActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          setStatusBarColor(R.color.statusbar);
        Intent intent2 = getIntent();
        String pdfUriString = intent2.getStringExtra("pdfUri");

        Uri pdfUri = Uri.parse(pdfUriString);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            // Start the activity to display the PDF
            Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Handle the case where no PDF viewer app is installed
            // You might want to display a message to the user or prompt them to install one
            Toast.makeText(this, "No PDF viewer app found", Toast.LENGTH_SHORT).show();
        }
    }
    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, color));
        }
    }
}

