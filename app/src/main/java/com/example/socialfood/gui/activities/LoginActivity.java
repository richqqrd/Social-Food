package com.example.socialfood.gui.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.socialfood.R;
import com.example.socialfood.controller.Authentication.AuthController;
import com.example.socialfood.controller.Authentication.AuthControllerInterface;
import com.example.socialfood.controller.Authentication.LoginResult;
import com.example.socialfood.databinding.ActivityLoginBinding;
import com.google.android.material.snackbar.Snackbar;

/**
 * Activity for handling user login. Provides interface for users to log in with existing
 * credentials or navigate to user creation. Implements MVC pattern with AuthController handling
 * business logic.
 */
public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private AuthControllerInterface authController;

    /**
     * Initializes the activity, sets up view binding and controllers.
     * 
     * @param savedInstanceState If non-null, this activity is being re-constructed from a previous
     * saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authController = new AuthController(this, null, null);
        setupClickListeners();
    }

    public void setAuthController(AuthControllerInterface authController) {
        this.authController = authController;
    }

    /**
     * Sets up click listeners for login and registration buttons.
     */
    private void setupClickListeners() {
        binding.loginButton.setOnClickListener(v -> handleLogin());
        binding.registerButton.setOnClickListener(v -> startUserCreation());
    }

    /**
     * Handles the login process. Validates user input and processes login attempt. Shows
     * appropriate error messages for invalid inputs or failed login attempts.
     */
    private void handleLogin() {
        String username = binding.loginUsername.getText().toString().trim();
        String password = binding.loginPassword.getText().toString();

        LoginResult result = authController.validateAndLogin(username, password);

        switch (result) {
        case SUCCESS:
            startActivity(new Intent(this, MainActivity.class));
            finish();
            break;
        case EMPTY_FIELDS:
            showEmptyFieldsError();
            break;
        case INVALID_CREDENTIALS:
            showLoginError();
            break;
        }
    }

    /**
     * Starts the user creation activity for new user registration.
     */
    private void startUserCreation() {
        startActivity(new Intent(this, com.example.socialfood.gui.activities.UserCreationActivity.class));
    }

    /**
     * Shows error message when input fields are empty.
     */
    private void showEmptyFieldsError() {
        showError(getString(R.string.error_empty_fields));
    }

    /**
     * Shows error message when login credentials are invalid.
     */
    private void showLoginError() {
        showError(getString(R.string.error_invalid_credentials));
    }

    /**
     * Shows error message using Snackbar
     */
    private void showError(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Cleans up resources when activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}