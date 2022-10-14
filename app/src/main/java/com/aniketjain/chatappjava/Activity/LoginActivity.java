package com.aniketjain.chatappjava.Activity;

import static com.aniketjain.chatappjava.Constant.ConstantPattern.emailPattern;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.aniketjain.chatappjava.Constant.ConstantProgressDialog;
import com.aniketjain.chatappjava.databinding.ActivityLoginBinding;
import com.aniketjain.roastedtoast.Toasty;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding loginBinding;
    private FirebaseAuth auth;
    private ConstantProgressDialog constantProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Binding SetUp
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());

        // Firebase Current User Instance
        auth = FirebaseAuth.getInstance();

        // setUp the progress dialog
        constantProgressDialog = new ConstantProgressDialog(this);

        // setUp the click listeners
        onClickListeners();

    }

    private void onClickListeners() {

        // setUp the Sign In Button for User
        loginBinding.signInBtn.setOnClickListener(view -> {

            // progress dialog show user click on the button
            constantProgressDialog.show();

            // get the email, password for the User Login
            String email = loginBinding.emailEt.getText().toString();
            String password = loginBinding.passwordEt.getText().toString();

            /*
             * Check the validation of email and password
             * when the user is sign in
             */
            if (email.isEmpty() || password.isEmpty()) {
                // progress dialog disable
                constantProgressDialog.dismiss();

                if (email.isEmpty()) {
                    loginBinding.emailEt.setError("Please enter the email address");
                } else {
                    loginBinding.passwordEt.setError("Please enter the password");
                }
            } else if (email.matches(emailPattern)) {
                // progress dialog disable
                constantProgressDialog.dismiss();

                loginBinding.emailEt.setError("Invalid email address");
            } else if (password.length() < 6) {
                // progress dialog disable
                constantProgressDialog.dismiss();

                loginBinding.passwordEt.setError("Please enter valid password");
            } else {
                // sign in setUp Code
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // progress dialog disable
                        constantProgressDialog.dismiss();

                        startActivity(new Intent(this, HomeActivity.class));
                        finish();
                    } else {
                        // progress dialog disable
                        constantProgressDialog.dismiss();

                        Toasty.error(this, "Login Failed");
                    }
                });

            }


        });

        // setUp the SignUp TextView for Goto Registration Activity
        loginBinding.signUpTv.setOnClickListener(view -> {
            startActivity(new Intent(this, RegistrationActivity.class));
            finish();
        });
    }
}