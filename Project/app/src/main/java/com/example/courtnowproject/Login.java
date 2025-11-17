package com.example.courtnowproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private TextView register, forgetPassword;
    private Button login;
    private EditText editTextEmail1, editTextPassword1;

    private FirebaseAuth mAuth;
    CollectionReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("CourtNow Court Booking");

        register = (TextView) findViewById(R.id.textviewRegister);
        register.setOnClickListener(this);

        forgetPassword = (TextView) findViewById(R.id.textviewForget);
        forgetPassword.setOnClickListener(this);

        login = (Button) findViewById(R.id.btnLogin);
        login.setOnClickListener(this);

        editTextEmail1 = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword1 = (EditText) findViewById(R.id.editTextPassword);



        mAuth = FirebaseAuth.getInstance();

        userRef = FirebaseFirestore.getInstance().collection("User");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textviewRegister:
                startActivity(new Intent(this, Register.class));
                break;

            case R.id.btnLogin:
                userLogin();
                break;

            case R.id.textviewForget:
                startActivity(new Intent(this, ForgotPassword.class));
                break;
        }
    }

    private void userLogin() {
        String email = editTextEmail1.getText().toString().trim();
        String password = editTextPassword1.getText().toString().trim();

        if(email.isEmpty() && password.isEmpty())
        {
            editTextEmail1.setError("Email is required");
            editTextPassword1.setError("Password is required");

            Toast.makeText(this, "Please enter all field for logging in ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail1.setError("Email is required");
            editTextEmail1.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail1.setError("Please enter a valid email!");
            editTextEmail1.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword1.setError("Password is required");
            editTextPassword1.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword1.setError("Minimum password length is 6 characters");
            editTextPassword1.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if ((task.isSuccessful())){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    DocumentReference currentUser = userRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    currentUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful())
                            {
                                DocumentSnapshot userSnapShot = task.getResult();
                                if(!userSnapShot.exists())
                                {

                                }
                                else
                                {
                                    Common.currentUser = userSnapShot.toObject(Users.class);
                                }
                            }
                        }
                    });

                    if(user.isEmailVerified()){

                        Toast.makeText(Login.this, "Login Successfully. Welcome to CourtNow Booking App", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Login.this, MainActivity.class ));
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(Login.this, "Please check your email to verify your account!", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(Login.this, "Failed to login! Please try again and check your credentials!", Toast.LENGTH_LONG).show();
                }
            }
        });





    }
}