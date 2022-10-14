package com.aniketjain.chatappjava.Activity;

import static com.aniketjain.chatappjava.Constant.Pattern.emailPattern;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aniketjain.chatappjava.databinding.ActivityRegistrationBinding;
import com.aniketjain.roastedtoast.Toasty;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {

    private static final int PICK_PHOTO = 1000;
    private Uri imageUri;
    private ActivityRegistrationBinding registrationBinding;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Binding SetUp
        registrationBinding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(registrationBinding.getRoot());

        // Firebase Current User Instance
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        // setUp the click listeners
        onClickListeners();


    }

    private void onClickListeners() {

        // setUp the fetched image data on to ImageView
        registrationBinding.profileIv.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Tack Image"), PICK_PHOTO);
        });

        // setUp the Sign Up Button for New User
        registrationBinding.signUpBtn.setOnClickListener(view -> {
            // get the name, email, password for the New User
            String name = registrationBinding.nameEt.getText().toString();
            String email = registrationBinding.emailEt.getText().toString();
            String password = registrationBinding.passwordEt.getText().toString();
            String confirm_password = registrationBinding.confirmPasswordEt.getText().toString();

            /*
             * Check the validation of name, email, password and confirm password
             * when the new user is sign up
             */
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirm_password.isEmpty()) {
                if (name.isEmpty()) {
                    registrationBinding.nameEt.setError("Please enter the name");
                } else if (email.isEmpty()) {
                    registrationBinding.emailEt.setError("Please enter the email address");
                } else if (password.isEmpty()) {
                    registrationBinding.passwordEt.setError("Please enter the password");
                } else {
                    registrationBinding.confirmPasswordEt.setError("Please enter the confirm password");
                }
            } else if (email.matches(emailPattern)) {
                registrationBinding.emailEt.setError("Invalid email address");
            } else if (password.length() < 6) {
                registrationBinding.passwordEt.setError("Please enter valid password");
            } else if (!password.equals(confirm_password)) {
                registrationBinding.confirmPasswordEt.setError("Enter the same password");
            } else {


                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toasty.success(this, "User Created Successfully");

                        DatabaseReference databaseReference =
                                database.getReference().child("user").child(Objects.requireNonNull(auth.getUid()));
                        StorageReference storageReference =
                                storage.getReference().child("upload").child(Objects.requireNonNull(auth.getUid()));


                    } else {
                        Toasty.error(this, "Registration Failed");
                    }
                });
            }

        });

        // setUp the Sign In Button for Goto Login Activity
        registrationBinding.signInTv.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // profile image checking, data is fetched or not
        if (requestCode == PICK_PHOTO) {
            if (data != null) {
                imageUri = data.getData();
                registrationBinding.profileIv.setImageURI(imageUri);
            }
        }
    }
}