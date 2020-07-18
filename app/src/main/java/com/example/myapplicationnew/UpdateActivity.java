package com.example.myapplicationnew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.io.IOException;
import de.hdodenhof.circleimageview.CircleImageView;
import com.example.myapplicationnew.Users;

public class UpdateActivity extends AppCompatActivity {


    private CircleImageView ProfileImage;
    private EditText tName,tPhone_number,tAlternate_number;
    private Button bUpdate;
    private static final int PICK_IMAGE = 1;
    Uri imageUri;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        tName = findViewById(R.id.editname);
        tPhone_number = findViewById(R.id.edit_phone_number);
        tAlternate_number = findViewById(R.id.editalternatenumber);
        bUpdate = findViewById(R.id.updatebutton);

        bUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String name = tName.getText().toString();
                String phone_number = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                String alternate_phone_number = tAlternate_number.getText().toString();
                boolean v = validate(name,phone_number,alternate_phone_number);
                if (v == true){
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference userRef = db.collection("user").document(userId);
                    Users user = new Users();
                    user.setName(name);
                    user.setPhone_number(phone_number);
                    user.setAlternate_phone_number(alternate_phone_number);
                    user.setUser_id(userId);
                    userRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"Data Saved Successfully",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Data Saving Failed",Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                }

            }
        });



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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}