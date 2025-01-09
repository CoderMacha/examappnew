package com.example.myexamapp.pdf;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myexamapp.DumbActivity;
import com.example.myexamapp.R;
import com.example.myexamapp.user.DashboardActivity;
import com.example.myexamapp.user.UpdateProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PdfReportGenerator extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();

    private String userName, userEmail;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    AppCompatButton downloadButton;
    private ProgressDialog progressDialog;

    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        downloadButton = findViewById(R.id.download_button);
        downloadButton.setOnClickListener(v -> fetchUserDataAndGeneratePdf());

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.create();

        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav_bar, R.string.close_nav_bar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE
            );

        }
    }

    private void fetchUserDataAndGeneratePdf() {
        progressDialog.show();
        String targetEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        assert targetEmail != null;
        DocumentReference userDocRef = db.collection("student").document(targetEmail);
        userDocRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                userName = documentSnapshot.getString("name");
                userEmail = documentSnapshot.getString("email");
                generatePdf();
            }
            else {
                progressDialog.dismiss();
                Toast.makeText(this, "Error. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void generatePdf() {
        try {

            // Create a new PDF document
            PdfDocument document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            canvas.drawText("Name: " + userName, 50, 50, paint);
            canvas.drawText("Email: " + userEmail, 50, 100, paint);

            String pdfFileName = userEmail + ".pdf";

            document.finishPage(page);

            // Write the document content to a file
            String directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();

            File file = new File(directoryPath, userEmail);

            try {
                file.getParentFile().mkdirs(); // Create directory if not exist
                FileOutputStream outputStream = new FileOutputStream(file);
                document.writeTo(outputStream);
                document.close();
                outputStream.close();
                Toast.makeText(this, "PDF created successfully!", Toast.LENGTH_SHORT).show();

                // Upload PDF to Firebase Storage
                uploadPdfToFirebase(file);
            } catch (IOException e) {
                progressDialog.dismiss();
                e.printStackTrace();
            }
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    private void uploadPdfToFirebase(File file) {
        Uri fileUri = Uri.fromFile(file);
        StorageReference reportRef = storageRef.child("reports/" + file.getName());

        reportRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get URL of the uploaded file
                    reportRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Add URL to Firestore
                        Map<String, Object> pdfMap = new HashMap<>();
                        pdfMap.put("url", uri.toString());
                        db.collection("pdfReports").add(pdfMap)
                                .addOnSuccessListener(documentReference -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(this, "PDF uploaded to Firestore", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    progressDialog.dismiss();
                                    Log.w("PDF", "Error adding document", e);
                                });
                    });
                })
                .addOnFailureListener(e -> Log.w("PDF", "Error uploading file", e));
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
        } else if (id == R.id.nav_dashboard) {
            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
        } else if (id == R.id.nav_update_profile) {
            startActivity(new Intent(getApplicationContext(), UpdateProfile.class));
//        } else if (id == R.id.nav_exam_section) {
//            startActivity(new Intent(getApplicationContext(), DumbActivity.class));
//        } else if (id == R.id.nav_pdf) {
//            startActivity(new Intent(getApplicationContext(), PdfReportGenerator.class));
//        } else if (id == R.id.nav_assistant) {
//            //TODO
        }
        return true;
    }

//    private boolean checkPermission() {
//        // checking of permissions.
//        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
//        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
//        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
//    }
//
//    private void requestPermission() {
//        // requesting permissions if not provided.
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
//    }

//   @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0) {
//
//                // after requesting permissions we are showing
//                // users a toast message of permission granted.
//                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//
//                if (writeStorage && readStorage) {
//                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted, yay! Do the
                    // read-related task you need to do.
                } else {
                    // Permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}