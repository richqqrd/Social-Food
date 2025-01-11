package com.example.socialfood.gui.activities;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static org.mockito.Mockito.when;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.socialfood.R;
import com.example.socialfood.controller.Authentication.AuthController;
import com.example.socialfood.controller.Authentication.RegisterResult;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(AndroidJUnit4.class)
/**
 * Integration test class for UserCreationActivity. Tests all registration-related functionality
 */
public class UserCreationActivityTest {

    /**
     * Rule to launch LoginActivity before each test
     */
    @Rule
    public ActivityScenarioRule<UserCreationActivity> activityRule = new ActivityScenarioRule<>(
            UserCreationActivity.class);

    /**
     * Mock of AuthController for testing login behavior
     */
    @Mock
    private AuthController mockAuthController;

    /**
     * Sets up test environment before each test. Initializes mocks and configures mock behavior.
     */
    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        Intents.init();

        activityRule.getScenario().onActivity(activity -> {
            activity.setAuthController(mockAuthController);
            when(mockAuthController.validateAndRegister("newuser", "password"))
                    .thenReturn(RegisterResult.SUCCESS);
            when(mockAuthController.validateAndRegister("", ""))
                    .thenReturn(RegisterResult.EMPTY_FIELDS);
            when(mockAuthController.validateAndRegister("existinguser", "password"))
                    .thenReturn(RegisterResult.USERNAME_TAKEN);
        });
    }

    /**
     * Tests successful registration flow.
     */
    @Test
    public void testSuccessfulRegistration() {
        onView(withId(R.id.usernameInput))
                .perform(typeText("newuser"), closeSoftKeyboard());
        onView(withId(R.id.passwordInput))
                .perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.passwordConfirmInput))
                .perform(typeText("password"), closeSoftKeyboard());

        onView(withId(R.id.createUserButton)).perform(click());

        onView(withText(R.string.registration_success))
                .check(matches(isDisplayed()));
    }

    /**
     * Tests empty fields validation.
     */
    @Test
    public void testEmptyFieldsValidation() {
        onView(withId(R.id.createUserButton)).perform(click());

        onView(withText(R.string.error_empty_fields))
                .check(matches(isDisplayed()));
    }

    /**
     * Tests password mismatch validation.
     */
    @Test
    public void testPasswordMismatch() {
        onView(withId(R.id.usernameInput))
                .perform(typeText("newuser"), closeSoftKeyboard());
        onView(withId(R.id.passwordInput))
                .perform(typeText("password1"), closeSoftKeyboard());
        onView(withId(R.id.passwordConfirmInput))
                .perform(typeText("password2"), closeSoftKeyboard());

        onView(withId(R.id.createUserButton)).perform(click());

        onView(withText(R.string.error_password_mismatch))
                .check(matches(isDisplayed()));
    }

    /**
     * Tests username already taken error.
     */
    @Test
    public void testUsernameTaken() {
        onView(withId(R.id.usernameInput))
                .perform(typeText("existinguser"), closeSoftKeyboard());
        onView(withId(R.id.passwordInput))
                .perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.passwordConfirmInput))
                .perform(typeText("password"), closeSoftKeyboard());

        onView(withId(R.id.createUserButton)).perform(click());

        onView(withText(R.string.error_username_taken))
                .check(matches(isDisplayed()));
    }

    /**
     * Tests navigation back to login screen.
     */
    @Test
    public void testNavigateBackToLogin() {
        onView(withId(R.id.backToLoginButton)).perform(click());
    }

    /**
     * Cleans up test environment after each test.
     */
    @After
    public void tearDown() {
        Intents.release();
    }
}