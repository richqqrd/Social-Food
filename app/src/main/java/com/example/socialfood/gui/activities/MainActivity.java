package com.example.socialfood.gui.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.socialfood.controller.Navigation.NavigationController;
import com.example.socialfood.controller.Navigation.NavigationControllerInterface;
import com.example.socialfood.controller.Post.PostController;
import com.example.socialfood.controller.Post.PostControllerInterface;
import com.example.socialfood.controller.User.UserController;
import com.example.socialfood.controller.User.UserControllerInterface;
import com.example.socialfood.databinding.ActivityMainBinding;
import com.example.socialfood.utils.ExampleData;
import com.example.socialfood.utils.UserManager;

/**
 * Main activity class that serves as the entry point of the application. Handles initialization of
 * core components and navigation.
 */
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private NavigationControllerInterface navigationController;
    public static final String EXTRA_SKIP_LOGIN = "skip_login";

    /**
     * Initializes the activity and its components. Checks login status and sets up views and
     * controllers.
     *
     * @param savedInstanceState If non-null, this activity is being re-constructed from a previous
     * saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getIntent().getBooleanExtra(EXTRA_SKIP_LOGIN, false)) {
            if (!checkLoginStatus()) {
                return;
            }
        }

        initializeView();
        initializeControllers();
        setupInitialState(savedInstanceState);
    }

    public void setControllers(
            NavigationController navigationController) {
        this.navigationController = navigationController;
    }

    /**
     * Verifies if a user is logged in. Redirects to LoginActivity if no user is logged in.
     *
     * @return true if user is logged in, false otherwise
     */
    private boolean checkLoginStatus() {
        if (!UserManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return false;
        }
        return true;
    }

    /**
     * Initializes the view bindings.
     */
    private void initializeView() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    /**
     * Initializes all required controllers. Sets up PostController, UserController and
     * NavigationController.
     */
    private void initializeControllers() {
        UserControllerInterface userController = new UserController(this, null, null);
        PostControllerInterface postController = new PostController(this, null, null, null, userController);

        navigationController = new NavigationController(
                this,
                getSupportFragmentManager(),
                postController,
                userController,
                binding.bottomNavigation);

        ExampleData.populateDatabase(postController, userController);

    }

    /**
     * Sets up the initial state of the activity. Populates database with example data if first
     * launch.
     *
     * @param savedInstanceState Bundle containing the saved state
     */
    private void setupInitialState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            navigationController.showMap();
        }
    }

    /**
     * Cleans up resources when activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
        navigationController = null;
    }

}
