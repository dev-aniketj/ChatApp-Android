package com.aniketjain.chatappjava.Activity;

import static com.aniketjain.chatappjava.Constant.Pattern.emailPattern;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.aniketjain.chatappjava.databinding.ActivityLoginBinding;
import com.aniketjain.roastedtoast.Toasty;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding loginBinding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Binding SetUp
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());

        // Firebase Current User Instance
        auth = FirebaseAuth.getInstance();

        // setUp the click listeners
        onClickListeners();

    }

    private void onClickListeners() {

        // setUp the Sign In Button for User
        loginBinding.signInBtn.setOnClickListener(view -> {

            // get the email, password for the User Login
            String email = loginBinding.emailEt.getText().toString();
            String password = loginBinding.passwordEt.getText().toString();

            /*
             * Check the validation of email and password
             * when the user is sign in
             */
            if (email.isEmpty() || password.isEmpty()) {
                if (email.isEmpty()) {
                    loginBinding.emailEt.setError("Please enter the email address");
                } else {
                    loginBinding.passwordEt.setError("Please enter the password");
                }
            } else if (email.matches(emailPattern)) {
                loginBinding.emailEt.setError("Invalid email address");
            } else if (password.length() < 6) {
                loginBinding.passwordEt.setError("Please enter valid password");
            } else {
                // sign in setUp Code
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toasty.error(this, "Login Failed");
                    }
                });

            }


        });

        // setUp the SignUp TextView for Goto Registration Activity
        loginBinding.signUpTv.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
            finish();
        });
    }
}