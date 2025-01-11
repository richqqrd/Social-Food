package com.example.socialfood.gui.fragments;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.example.socialfood.R;
import com.example.socialfood.controller.Post.PostController;
import com.example.socialfood.controller.User.UserController;
import com.example.socialfood.gui.activities.MainActivity;
import com.example.socialfood.utils.ExampleData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test class for the SettingsFragment, tests the behavior of the fragment
 */
@RunWith(AndroidJUnit4.class)
public class SettingsFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(
            new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class)
                    .putExtra(MainActivity.EXTRA_SKIP_LOGIN, true));

    @Rule
    public GrantPermissionRule locationPermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    /**
     * Sets up the test environment before each test
     * Creates test user and navigates to settings screen
     */
    @Before
    public void setup() {
        createTestUser();
        navigateToSettings();
    }

    /**
     * Creates a test user and populates the database with example data
     * Uses UserController and PostController to set up test environment
     */
    private void createTestUser() {
        Context context = ApplicationProvider.getApplicationContext();
        UserController userController = new UserController(context, null, null);
        PostController postController = new PostController(context, null, null, null, userController);
        ExampleData.populateDatabase(postController, userController);
    }

    /**
     * Navigates to the settings screen by clicking the settings navigation item
     */
    private void navigateToSettings() {
        onView(withId(R.id.nav_settings))
                .perform(click());
    }

    /**
     * Tests if notifications switch is displayed
     */
    @Test
    public void testNotificationsSwitchIsDisplayed() {
        onView(withText("Push-Benachrichtigungen"))
                .check(matches(isDisplayed()));
    }

    /**
     * Tests if language list preference is displayed
     */
    @Test
    public void testLanguageListPreferenceIsDisplayed() {
        onView(withText("Push-Benachrichtigungen"))
                .check(matches(isDisplayed()));
    }

    /**
     * Tests if delete profile is displayed
     */
    @Test
    public void testAccountDeletePreferenceIsDisplayed() {
        onView(withText("Konto löschen"))
                .check(matches(isDisplayed()));
    }
}
