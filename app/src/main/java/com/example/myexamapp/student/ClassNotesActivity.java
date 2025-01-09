package com.example.myexamapp.student;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myexamapp.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ClassNotesActivity extends AppCompatActivity {
    private Spinner subjectSpinner, classSpinner;
    private ArrayAdapter<String> subjectAdapter, classAdapter;
    private final String[] items1 = {"Item 1A", "Item 1B", "Item 1C"};
    private final String[] items2 = {"Item 2A", "Item 2B", "Item 2C"};
    private String selectedClass, selectedSubject;

    private List<String> pdfNames;
    private RecyclerView recyclerView;
    private PdfAdapter pdfAdapter;
    private Button viewAllButton;

    private List<Uri> pdfUris;

    // In your Activity class
    public static final int INTERNET_REQUEST_CODE = 1;

    private void checkForInternetPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    INTERNET_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case INTERNET_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted, yay! Do the
                    // internet-related task you need to do.
                } else {
                    // Permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    checkForInternetPermission(); // Ask again
                }
            }

            // Other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_note);
        checkForInternetPermission();

        setStatusBarColor(R.color.statusbar);

        subjectSpinner = findViewById(R.id.subjectSpinner);
        classSpinner = findViewById(R.id.classSpinner);
        viewAllButton = findViewById(R.id.viewAllButton);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        pdfNames = new ArrayList<>();
        pdfUris = new ArrayList<>();
        //TODO
        pdfAdapter = new PdfAdapter(pdfNames);
        recyclerView.setAdapter(pdfAdapter);

        subjectAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items2);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(subjectAdapter);

        classAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items1);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(classAdapter);

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

        viewAllButton.setOnClickListener(v -> getPdf());
    }

    public void onClassItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedClass = parent.getItemAtPosition(position).toString();
        // Toast.makeText(this, "Class: " + selectedClass, // Toast.LENGTH_SHORT).show();
    }

    public void onSubjectItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedSubject = parent.getItemAtPosition(position).toString();
        // Toast.makeText(this, "Subject: " + selectedSubject, // Toast.LENGTH_SHORT).show();
    }

    private String getSelectedClass() {
        return selectedClass;
    }

    private String getSelectedSubject() {
        return selectedSubject;
    }


    private void getPdf() {
        // Toast.makeText(this, "Getting PDFs: ", // Toast.LENGTH_SHORT).show();
        //to get the pdfs of the particular file and display it in the pdf view
        String pathString = "pdfs/" + getSelectedClass() + "/" + getSelectedSubject() + "/";
        StorageReference pdfRef = FirebaseStorage.getInstance().getReference().child(pathString);

        pdfRef.listAll()
                .addOnSuccessListener(listResult -> {
                    // Toast.makeText(this, "PDFs: " + listResult.getItems().size(), // Toast.LENGTH_SHORT).show();
                    for (StorageReference fileRef : listResult.getItems()) {
                        pdfNames.add(fileRef.getName());
                        fileRef.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    pdfUris.add(uri);
                                    // Toast.makeText(getApplicationContext(), "PDF URI: " + uri.toString(), // Toast.LENGTH_SHORT).show();
                                    Log.d("Download Link S", uri.toString());
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Download Link F", "Failed to get download URL", e);
                                });
                    }
                    pdfAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(listResult -> {
                });

//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection(pathString).get()
//                        .addOnCompleteListener(task -> {
//                            if (task.isSuccessful()) {
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    Log.d("Firestore", document.getId() + " => " + document.getData());
//                                }
//                            } else {
//                                Log.w("Firestore", "Error getting documents.", task.getException());
//                            }
//                        });
        // Toast.makeText(this, "PDFs: " + pdfUris.toString(), // Toast.LENGTH_SHORT).show();
    }


    private class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.PdfViewHolder> {

        private final List<String> pdfList;

        public PdfAdapter(List<String> pdfList) {
            this.pdfList = pdfList;
        }

        @NonNull
        @Override
        public PdfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new PdfViewHolder(view);
        }

//        @Override
//        public void onBindViewHolder(@NonNull PdfViewHolder holder, int position) {
//            String fileName = pdfList.get(position);
//            holder.textView.setText(fileName);
//        }

        @Override
        public void onBindViewHolder(@NonNull PdfViewHolder holder, int position) {
//            Uri uri = pdfUris.get(position);
            String fileName = pdfList.get(position);
            holder.textView.setText(fileName);
//            holder.textView.setText(uri.getLastPathSegment());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Pass the URI to ViewPdf activity when an item is clicked
                    Log.d("PDF URI", "ok 0");

                    int pos = holder.getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(ClassNotesActivity.this, ViewPdfActivity.class);
                        intent.putExtra("pdfUri", pdfUris.get(pos).toString());

                        // Toast.makeText(getApplicationContext(), "Launching: " + fileName, // Toast.LENGTH_SHORT).show();

                        startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return pdfList.size();
        }

        private class PdfViewHolder extends RecyclerView.ViewHolder {

            TextView textView;

            public PdfViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(android.R.id.text1);
            }
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
