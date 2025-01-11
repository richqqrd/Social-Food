package com.example.socialfood.gui.activities;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.socialfood.R;
import com.example.socialfood.controller.Authentication.AuthController;
import com.example.socialfood.controller.Authentication.LoginResult;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Integration test class for LoginActivity. Tests all login-related functionality including:
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    /**
     * Rule to launch LoginActivity before each test
     */
    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(
            LoginActivity.class);

    /**
     * Mock of AuthController for testing login behavior
     */
    @Mock
    private AuthController mockAuthController;

    /**
     * Sets up test environment before each test. Initializes mocks and configures mock behavior for
     * different login scenarios.
     */
    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        Intents.init();
        activityRule.getScenario().onActivity(activity -> {
            activity.setAuthController(mockAuthController);
            when(mockAuthController.validateAndLogin("testuser", "password"))
                    .thenReturn(LoginResult.SUCCESS);
            when(mockAuthController.validateAndLogin("", ""))
                    .thenReturn(LoginResult.EMPTY_FIELDS);
            when(mockAuthController.validateAndLogin("wrong", "wrong"))
                    .thenReturn(LoginResult.INVALID_CREDENTIALS);
        });
    }

    /**
     * Tests successful login flow. Verifies that entering valid credentials leads to MainActivity
     * launch.
     */
    @Test
    public void testSuccessfulLogin() {
        onView(withId(R.id.login_username))
                .perform(typeText("testuser"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(typeText("password"), closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());

        intended(hasComponent(MainActivity.class.getName()));
    }

    /**
     * Tests empty fields validation. Verifies that submitting empty fields shows appropriate error
     * message.
     */
    @Test
    public void testEmptyFieldsValidation() {
        onView(withId(R.id.login_button)).perform(click());

        onView(withText(R.string.error_empty_fields))
                .check(matches(isDisplayed()));
    }

    /**
     * Tests invalid credentials error handling. Verifies that entering wrong credentials shows
     * appropriate error message.
     */
    @Test
    public void testInvalidCredentials() {
        onView(withId(R.id.login_username))
                .perform(typeText("wrong"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(typeText("wrong"), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        onView(withText(R.string.error_invalid_credentials))
                .check(matches(isDisplayed()));
    }

    /**
     * Tests navigation to registration screen. Verifies that clicking register button starts
     * UserCreationActivity.
     */
    @Test
    public void testNavigateToRegistration() {
        onView(withId(R.id.register_button))
                .perform(click());

        intended(hasComponent(UserCreationActivity.class.getName()));
    }

    /**
     * Cleans up test environment after each test. Releases Intents to avoid memory leaks.
     */
    @After
    public void tearDown() {
        Intents.release();
    }
}
