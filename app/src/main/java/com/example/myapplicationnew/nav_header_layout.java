package com.example.myapplicationnew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class nav_header_layout extends AppCompatActivity {
    final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    final String phoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_header_layout);
        final ImageView profileImage = (ImageView) findViewById(R.id.navProfileImage);
        final TextView navName = (TextView)findViewById(R.id.navName);
        final TextView navPhone = (TextView) findViewById(R.id.navNumber);

        super.onStart();
        navPhone.setText(phoneNumber);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/"+userId);
        try {
            final File localFile = File.createTempFile("profileImg","jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitMapNew = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    profileImage.setImageBitmap(bitMapNew);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("user").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot docSnap = task.getResult();
                    if (docSnap.exists()) {
                        navName.setText(docSnap.get("name").toString());
                        navPhone.setText(docSnap.get("phone_number").toString());
                    } else {
                        Log.d("Data", "No Data");
                    }
                }
            }
        });
    }
}