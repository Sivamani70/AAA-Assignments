package com.example.firebaseauth;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.firebaseauth.databinding.ActivityLoginBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        auth = FirebaseAuth.getInstance();

        binding.login.setOnClickListener(v -> {
            final String mail = String.valueOf(binding.mailId.getText());
            final String password = String.valueOf(binding.password.getText());

            if (!mail.isEmpty() && !password.isEmpty()) {
                auth.signInWithEmailAndPassword(mail, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Snackbar.make(v, "Login Successful", Snackbar.LENGTH_LONG).show();
                                navigateToHome();
                                binding.mailId.setText("");
                                binding.password.setText("");
                            } else {
                                Snackbar.make(v, "Login Failed", Snackbar.LENGTH_LONG).show();
                            }
                        });
                return;
            }
            Snackbar.make(v, "Please Enter Mail and Password", Snackbar.LENGTH_LONG).show();
            binding.mailId.setError("Enter Mail-id");
            binding.password.setError("Enter Enter Password");
        });


        binding.register.setOnClickListener(v -> {
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(Fields.APP_NAME);
            if (launchIntent != null) {
                startActivity(launchIntent);
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null) {
            navigateToHome();
        }
    }


    void navigateToHome() {
        startActivity(new Intent(this, HomeScreen.class));
    }
}