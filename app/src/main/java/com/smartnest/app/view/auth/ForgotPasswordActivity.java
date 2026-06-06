package com.smartnest.app.view.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.smartnest.app.databinding.ActivityLoginBinding;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        binding.btnLogin.setText("Send Reset Email");
        binding.btnSignup.setText("Back to Login");
        binding.tvForgotPassword.setVisibility(android.view.View.GONE);

        binding.btnLogin.setOnClickListener(v -> sendResetEmail());
        binding.btnSignup.setOnClickListener(v -> finish());
    }

    private void sendResetEmail() {
        String email = binding.etEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            binding.emailLayout.setError("Enter your email");
            return;
        }

        binding.btnLogin.setEnabled(false);
        binding.btnLogin.setText("Sending...");

        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Reset email sent! Check your inbox.", Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                    binding.btnLogin.setEnabled(true);
                    binding.btnLogin.setText("Send Reset Email");
                });
    }
}