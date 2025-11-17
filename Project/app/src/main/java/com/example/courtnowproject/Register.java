package com.example.courtnowproject;

import
        static android.Manifest.permission.CAMERA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.util.SparseArray;
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
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private TextView login;
    private Button register;
    private Button captureBtn, detectBtn;
    private EditText editTextUsername1, editTextEmail1, editTextPassword1, editTextAge1;
    Bitmap bitmap;
    Uri imageUri;
    private static final int REQUEST_CAMERA_CODE = 100;

    private FirebaseAuth mAuth;
    CollectionReference UserRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("CourtNow Court Booking");

        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseFirestore.getInstance().collection("User");

        captureBtn = findViewById(R.id.btnScan);
        detectBtn = findViewById(R.id.btnDetect);

        register = (Button)findViewById(R.id.btnRegister);
        register.setOnClickListener(this);

        editTextUsername1 = (EditText) findViewById(R.id.editTextUsername);
        editTextAge1 = (EditText) findViewById(R.id.editTextAge);
        editTextEmail1 = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword1 = (EditText) findViewById(R.id.editTextPassword);

        login = (TextView) findViewById(R.id.textviewLogin);
        login.setOnClickListener(this);

        if(ContextCompat.checkSelfPermission(Register.this, CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Register.this, new String[]{CAMERA}, REQUEST_CAMERA_CODE);
        }


        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(Register.this);
            }
        });

        detectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp2 = editTextAge1.getText().toString().trim();
                detectText(temp2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    getTextFromImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getTextFromImage(Bitmap bitmap){
        TextRecognizer recognizer = new TextRecognizer.Builder(this).build();

        if(!recognizer.isOperational()){
            Toast.makeText(Register.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
        }
        else{
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();

            SparseArray<TextBlock> textBlockSparseArray = recognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for(int i = 0; i < textBlockSparseArray.size(); i++){
                TextBlock textBlock = textBlockSparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
            }
            editTextAge1.setText(stringBuilder.toString());
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textviewLogin:
                startActivity(new Intent(this, Login.class));
                break;
            case R.id.btnRegister:
                register();
                break;
        }
    }

    private void register() {
        String email = editTextEmail1.getText().toString().trim();
        String password = editTextPassword1.getText().toString().trim();
        String age = editTextAge1.getText().toString().trim();
        String username = editTextUsername1.getText().toString().trim();
        int tempAge;

        if(username.isEmpty() && email.isEmpty() && password.isEmpty() && age.isEmpty())
        {
            editTextUsername1.setError("Username is required!");
            editTextEmail1.setError("Email is required!");
            editTextPassword1.setError("Password is required!");
            editTextAge1.setError("Age is required!");

            Toast.makeText(this, "Please enter all field as required for registration", Toast.LENGTH_SHORT).show();
            return;
        }

        if(age.length() > 3)
        {
            editTextAge1.setError("Invalid age value detected.");
            editTextAge1.requestFocus();
            Toast.makeText(this, "Invalid age value detected, please click the detect age button.", Toast.LENGTH_SHORT).show();
            return;
        }


        if(username.isEmpty()){
            editTextUsername1.setError("Username is required!");
            editTextUsername1.requestFocus();
            return;
        }

        if(email.isEmpty()){
            editTextEmail1.setError("Email is required!");
            editTextEmail1.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail1.setError("Please provide valid email!");
            editTextEmail1.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword1.setError("Password is required!");
            editTextPassword1.requestFocus();
            return;
        }

        if(password.length()< 6){
            editTextPassword1.setError("Minimum password length should be 6 characters!");
            editTextPassword1.requestFocus();
            return;
        }

        if(age.isEmpty()){
            editTextAge1.setError("Age is required!");
            editTextAge1.requestFocus();
            return;
        }

        tempAge = Integer.parseInt(age);

        if(tempAge <= 0 || tempAge >= 100){
            editTextAge1.setError("Invalid age value detected.");
            editTextAge1.requestFocus();
            return;
        }




       mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                            String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            Users users = new Users(username, age, email, user);

                            UserRef.document(user)
                                    .set(users)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            user1.sendEmailVerification();
                                            Toast.makeText(Register.this, "User has been registered successfully, please verify your account in email!", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(Register.this, Login.class));
                                        }
                                    }).addOnFailureListener((e) -> {
                                                Toast.makeText(Register.this, "Failed to register! Please try again!", Toast.LENGTH_LONG).show();
                                            });
                        }else{
                            Toast.makeText(Register.this, "Failed to register! Please try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void detectText(String Ic){

        if(!Objects.equals(Ic, "")) {
            String temp = "";
            int temp1;


            if(Ic.equals("0") || Ic.equals("1") || Ic.equals("2")|| Ic.equals("3")|| Ic.equals("4")|| Ic.equals("5")|| Ic.equals("6")|| Ic.equals("7")|| Ic.equals("8")|| Ic.equals("9"))
            {
                Toast.makeText(this, "Invalid values detected, please try again", Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                temp = Ic.replaceAll("[\\D]", "");
                temp = temp.substring(0,2);
                temp1 = Integer.parseInt(temp.replaceAll("[\\D]", ""));

                if (temp1 > 22 && temp1 <= 99) {
                    temp1 = 1900 + temp1;
                }
                else if (temp1 <= 22 && temp1 >= 00) {
                    temp1 = 2000 + temp1;
                }

                temp1 = 2022 - temp1;
                temp1 = Math.abs(temp1);

                editTextAge1.setText(String.valueOf(temp1));
                }
            }
        else {
            Toast.makeText(this, "Error in detecting IC, please try again", Toast.LENGTH_SHORT).show();
            return;
        }
    }


}