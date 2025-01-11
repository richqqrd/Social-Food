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
 * Test class for the ProfileFragment, tests the behavior of the fragment
 */
@RunWith(AndroidJUnit4.class)
public class ProfileFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(
            new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class)
                    .putExtra(MainActivity.EXTRA_SKIP_LOGIN, true));

    @Rule
    public GrantPermissionRule locationPermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    /**
     * Sets up the test environment before each test
     * Creates test user and navigates to profile screen
     */
    @Before
    public void setup() {
        createTestUser();
        navigateToProfile();
    }

    /**
     * Creates a test user and populates the database with example data
     */
    private void createTestUser() {
        Context context = ApplicationProvider.getApplicationContext();
        UserController userController = new UserController(context, null, null);
        PostController postController = new PostController(context, null, null, null, userController);
        ExampleData.populateDatabase(postController, userController);
    }

    /**
     * Navigates to the profile screen
     */
    private void navigateToProfile() {
        onView(withId(R.id.nav_profile))
                .perform(click());
    }

    /**
     * Tests the initialization of the ProfileFragment
     */
    @Test
    public void testProfileFragmentInitialization() {
        onView(withId(R.id.profile_posts_recycler))
                .check(matches(isDisplayed()));
    }

    /**
     * Tests if username is displayed correctly
     */
    @Test
    public void testUsernameIsDisplayedCorrectly() {
        onView(withId(R.id.profile_username))
                .check(matches(isDisplayed()))
                .check(matches(withText("TestUser1")));
    }

    /**
     * Tests if bio is displayed correctly
     */
    @Test
    public void testBioIsDisplayedCorrectly() {
        onView(withId(R.id.profile_bio))
                .check(matches(isDisplayed()))
                .check(matches(withText("Ich bin der erste Testbenutzer")));
    }

    /**
     * Tests if posts count is displayed correctly
     */
    @Test
    public void testPostsCountIsDisplayedCorrectly() {
        onView(withId(R.id.profile_posts_count))
                .check(matches(isDisplayed()));
    }

    /**
     * Tests if user posts are displayed correctly
     */
    @Test
    public void testUserPostsAreDisplayed() {
        onView(withId(R.id.profile_posts_recycler))
                .check(matches(isDisplayed()));
    }

    /**
     * Tests if followers count is displayed correctly
     */
    @Test
    public void testFollowersCountIsDisplayed() {
        onView(withId(R.id.profile_followers_count))
                .check(matches(isDisplayed()));
    }

}