package com.example.socialfood.gui.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.socialfood.R;
import com.example.socialfood.controller.Authentication.AuthController;
import com.example.socialfood.controller.Authentication.AuthControllerInterface;
import com.example.socialfood.controller.Authentication.RegisterResult;
import com.example.socialfood.databinding.ActivityUserCreationBinding;
import com.google.android.material.snackbar.Snackbar;

/**
 * Activity for user registration. Handles the creation of new user accounts
 */
public class UserCreationActivity extends AppCompatActivity {
    private ActivityUserCreationBinding binding;
    private AuthControllerInterface authController;

    /**
     * Initializes the activity, sets up view binding and controller.
     * 
     * @param savedInstanceState If non-null, this activity is being re-constructed from a previous
     * saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserCreationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authController = new AuthController(this, null, null);
        setupClickListeners();
    }

    /**
     * Sets the authentication controller. Used for dependency injection in tests.
     * 
     * @param authController The controller to be used for authentication operations
     */
    public void setAuthController(AuthControllerInterface authController) {
        this.authController = authController;
    }

    /**
     * Sets up click listeners for the create user and back buttons.
     */
    private void setupClickListeners() {
        binding.createUserButton.setOnClickListener(v -> createUser());
        binding.backToLoginButton.setOnClickListener(v -> finish());
    }

    /**
     * Handles user creation process. Validates input fields and attempts to create a new user.
     * Shows appropriate error messages for validation failures.
     */
    private void createUser() {
        String username = binding.usernameInput.getText().toString().trim();
        String password = binding.passwordInput.getText().toString();
        String passwordConfirm = binding.passwordConfirmInput.getText().toString();

        if (!password.equals(passwordConfirm)) {
            showError(getString(R.string.error_password_mismatch));
            return;
        }

        RegisterResult result = authController.validateAndRegister(username, password);

        switch (result) {
        case SUCCESS:
            Snackbar.make(binding.getRoot(),
                    getString(R.string.registration_success),
                    Snackbar.LENGTH_SHORT)
                    .addCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            finish();
                        }
                    }).show();
            break;
        case EMPTY_FIELDS:
            showError(getString(R.string.error_empty_fields));
            break;
        case USERNAME_TAKEN:
            showError(getString(R.string.error_username_taken));
            break;
        }
    }

    /**
     * Shows error message using Snackbar.
     * 
     * @param message The error message to display
     */
    private void showError(String message) {
        Snackbar.make(findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_SHORT).show();
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
