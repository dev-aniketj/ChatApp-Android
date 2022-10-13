package com.aniketjain.chatappjava;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.aniketjain.chatappjava.databinding.ActivityRegistrationBinding;
import com.aniketjain.roastedtoast.Toasty;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding registrationBinding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Binding SetUp
        registrationBinding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(registrationBinding.getRoot());


        auth = FirebaseAuth.getInstance();
        onClickListeners();


    }

    private void onClickListeners() {

        registrationBinding.signUpBtn.setOnClickListener(view -> {

            String name = registrationBinding.nameEt.getText().toString();
            String email = registrationBinding.emailEt.getText().toString();
            String password = registrationBinding.passwordEt.getText().toString();
            String confirm_password = registrationBinding.confirmPasswordEt.getText().toString();

            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\\\.+[a-z]+";

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
                    } else {
                        Toasty.error(this, "Registration Failed");
                    }
                });
            }

        });

        registrationBinding.signInBtn.setOnClickListener(view ->

        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }


}