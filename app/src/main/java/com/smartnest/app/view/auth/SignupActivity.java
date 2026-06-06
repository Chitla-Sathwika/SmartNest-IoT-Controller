package com.smartnest.app.view.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.smartnest.app.databinding.ActivityLoginBinding;
import com.smartnest.app.view.dashboard.DashboardActivity;

public class SignupActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        binding.btnLogin.setText("Create Account");
        binding.btnSignup.setText("Already have an account? Login");
        binding.tvForgotPassword.setVisibility(android.view.View.GONE);

        binding.btnLogin.setOnClickListener(v -> signupUser());
        binding.btnSignup.setOnClickListener(v -> finish());
    }

    private void signupUser() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            binding.emailLayout.setError("Enter email");
            return;
        }
        if (password.length() < 6) {
            binding.passwordLayout.setError("Password must be at least 6 characters");
            return;
        }

        binding.btnLogin.setEnabled(false);
        binding.btnLogin.setText("Creating account...");

        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> {
                    startActivity(new Intent(this, DashboardActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                    binding.btnLogin.setEnabled(true);
                    binding.btnLogin.setText("Create Account");
                });
    }
}