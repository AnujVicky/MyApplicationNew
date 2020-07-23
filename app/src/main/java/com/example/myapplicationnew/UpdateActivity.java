package com.example.myapplicationnew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import de.hdodenhof.circleimageview.CircleImageView;


public class UpdateActivity extends AppCompatActivity {


    private static final String TAG = "UpdateActivity";
    private CircleImageView ProfileImage;
    private EditText tName, tPhone_number, tAlternate_number, tNameVal;
    private Button bUpdate;
    private TextView title;
    private  ImageView  profileImage;
    private static final int PICK_IMAGE = 1;
    Uri imageUri,filePath;
    FirebaseStorage storage;
    StorageReference storageReference;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Log.d("ONCREATE", "msg2");
        tNameVal = findViewById(R.id.editname);
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference refData = FirebaseDatabase.getInstance().getReference("user");
        Log.d("REF", "msg1" + refData);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        ImageView DirectToEdit;
        ProfileImage = (CircleImageView) findViewById(R.id.profile_image);
        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallery, "Sellect Picture"), PICK_IMAGE);
            }
        });
        Users userGet = new Users();
        tName = findViewById(R.id.editname);
        tPhone_number = findViewById(R.id.edit_phone_number);
        tAlternate_number = findViewById(R.id.editalternatenumber);
        bUpdate = findViewById(R.id.updatebutton);
        title = findViewById(R.id.tv_name);

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("user").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot docSnap = task.getResult();
                    if (docSnap.exists()) {
                        tName.setText(docSnap.get("name").toString());
                        tPhone_number.setText(docSnap.get("phone_number").toString());
                        tAlternate_number.setText(docSnap.get("alternate_phone_number").toString());
                        title.setText(docSnap.get("name").toString());
//                        Log.d("Data","msg"+docSnap.get("name").toString());
                    } else {
                        Log.d("Data", "No Data");
                    }
                }
            }
        });
        profileImage = findViewById(R.id.profile_image);
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


        bUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = tName.getText().toString();
                final String phone_number = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                final String alternate_phone_number = tAlternate_number.getText().toString();
                final boolean v = validate(name, phone_number, alternate_phone_number);
                if (v == true) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    final DocumentReference userRef = db.collection("user").document(userId);
                    userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot docsp = task.getResult();
                                if(docsp.exists())
                                {
                                    Users userUpdate = new Users(name,alternate_phone_number);
                                    Map<String, Object> updateValues = userUpdate.toMap();
                                    Map<String, Object> childUpdates = new HashMap<>();

                                    childUpdates.put("name", name);
                                    childUpdates.put("alternate_phone_number",alternate_phone_number);

                                    userRef.update(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), "Data Updated Successfully", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Data Update Failed", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                                }
                                else
                                {
                                    Users user = new Users();
                                    user.setName(name);
                                    user.setPhone_number(phone_number);
                                    user.setAlternate_phone_number(alternate_phone_number);
                                    user.setUser_id(userId);
                                    userRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), "Data Saved Successfully", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Data Saving Failed", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });

                }

            }
        });


    }

   public void updateData(){

   }

    private boolean validate(String name, String phone_number, String alternate_phone_number) {
        if(name.equals("")){
            Toast.makeText(getApplicationContext(), "Enter a Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(phone_number.equals("")){
            Toast.makeText(getApplicationContext(), "Enter a Phone Number", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(alternate_phone_number.equals("")){
            Toast.makeText(getApplicationContext(), "Enter a Alternate Phone Number", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                ProfileImage.setImageBitmap(bitmap);

                uploadImage();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void uploadImage()
    {
        if (imageUri != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            String userIdData = FirebaseAuth.getInstance().getCurrentUser().getUid();
            // Defining the child of storageReference
            StorageReference ref  = storageReference.child("images/"+userIdData.toString());
            // adding listeners on upload
            // or failure of image
            ref.putFile(imageUri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(getApplicationContext(),
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(getApplicationContext(),
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }
}