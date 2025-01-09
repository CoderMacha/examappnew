package com.example.myexamapp.teacher;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;

import com.example.myexamapp.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class AddClassNotes extends AppCompatActivity {

    private static final int PICK_PDF_FILE = 1;
    AppCompatButton uploadButton, selectButton;
    PDFView pdfView;
    private Uri uri;

    private AppCompatSpinner classSpinner, subjectSpinner;
    private ArrayAdapter<String> classAdapter, subjectAdapter;
    private final String[] items1 = {"Item 1A", "Item 1B", "Item 1C"};
    private final String[] items2 = {"Item 2A", "Item 2B", "Item 2C"};
    private String selectedClass, selectedSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class_notes);
        setStatusBarColor(R.color.statusbar);
        uploadButton = findViewById(R.id.uploadButton);
        pdfView = findViewById(R.id.pdfView);
        selectButton = findViewById(R.id.selectButton);
        classSpinner = findViewById(R.id.classSpinner);
        subjectSpinner = findViewById(R.id.subjectSpinner);

        classAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items1);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        subjectAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items2);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        classSpinner.setAdapter(classAdapter);
        subjectSpinner.setAdapter(subjectAdapter);

        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onClassItemSelected(parent, view, position, id); // Delegate to the helper method
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onSubjectItemSelected(parent, view, position, id); // Delegate to the helper method
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        uploadButton.setEnabled(false);

        selectButton.setOnClickListener(v -> selectPdf());
        uploadButton.setOnClickListener(v -> uploadPdf());
    }

    private void selectPdf() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        startActivityForResult(intent, PICK_PDF_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        if (requestCode == PICK_PDF_FILE && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                uri = resultData.getData();
                openPdf();
            }
        }
    }

    private void openPdf() {
        Toast.makeText(this, "PDF Opened", Toast.LENGTH_SHORT).show();

        pdfView.fromUri(uri)
                .defaultPage(0)
                .load();

        Toast.makeText(this, "PDF Loaded", Toast.LENGTH_SHORT).show();
        uploadButton.setEnabled(true);
    }

    //Helper function
    String getFileNameByUri(Context context, Uri uri) {
        String fileName = "unknown"; // Default file name
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            fileName = cursor.getString(nameIndex);
            cursor.close();
        }

        return fileName;
    }


    public void onClassItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedClass = parent.getItemAtPosition(position).toString();
        Toast.makeText(this, "Class: " + selectedClass, Toast.LENGTH_SHORT).show();
    }

    public void onSubjectItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedSubject = parent.getItemAtPosition(position).toString();
        Toast.makeText(this, "Class: " + selectedClass, Toast.LENGTH_SHORT).show();
    }

    // Call this method when you need the selected class name
    private String getSelectedClass() {
        return selectedClass;
    }

    private String getSelectedSubject() {
        return selectedSubject;
    }


    private void uploadPdf() {
        //to upload the data when the user clicks on the upload button
        if (uri != null) {

            String pathString = "pdfs/" + getSelectedClass() + "/" + getSelectedSubject() + "/";
            StorageReference pdfRef = FirebaseStorage.getInstance().getReference().child(pathString + getFileNameByUri(this, uri));

            pdfRef.putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(getApplicationContext(), "PDF Uploaded", Toast.LENGTH_SHORT).show();
                        pdfRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    Map<String, Object> pdfMap = new HashMap<>();
                                    pdfMap.put("notes", uri.toString());
                            Toast.makeText(getApplicationContext(), "PDF URL: " + uri.toString(), Toast.LENGTH_SHORT).show();
                                    FirebaseFirestore.getInstance().collection(pathString).add(pdfMap)
                                            .addOnSuccessListener(documentReference -> Log.d("PDF", "DocumentSnapshot written with ID: " + documentReference.getId()))
                                            .addOnFailureListener(e -> Log.w("PDF", "Error adding document", e));
                                }
                        )
                                .addOnFailureListener(e -> Log.w("PDF", "Error uploading file", e));
                    })
                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Upload Failed", Toast.LENGTH_SHORT).show());

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
