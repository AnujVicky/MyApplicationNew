package com.example.myapplicationnew;

<<<<<<< HEAD
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
=======
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class ProfileActivity extends AppCompatActivity {
    private  ImageView  profileImage;
    final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

>>>>>>> a1e5541... all image retrive done
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ImageView DirectToEdit;

        DirectToEdit = (ImageView) findViewById(R.id.editdata);
        DirectToEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,
                        UpdateActivity.class);
                startActivity(intent);
<<<<<<< HEAD
            }
        });



=======

            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        profileImage = findViewById(R.id.profile_image);
        final TextView proName =(TextView)findViewById(R.id.proName);
        final TextView proPhone =(TextView)findViewById(R.id.proPhone);
        final TextView proAlternate =(TextView)findViewById(R.id.proAlternate);
        final TextView proLocation =(TextView)findViewById(R.id.proLocation);
        final TextView proTitle = (TextView)findViewById(R.id.tv_name);

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
                        proName.setText(docSnap.get("name").toString());
                        proPhone.setText(docSnap.get("phone_number").toString());
                        proAlternate.setText(docSnap.get("alternate_phone_number").toString());
                        proLocation.setText("MAHUADANR");
                        proTitle.setText(docSnap.get("name").toString());
//                        Log.d("Data","msg"+docSnap.get("name").toString());
                    } else {
                        Log.d("Data", "No Data");
                    }
                }
            }
        });
>>>>>>> a1e5541... all image retrive done

    }


<<<<<<< HEAD
=======

>>>>>>> a1e5541... all image retrive done
}