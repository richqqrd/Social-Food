package com.example.socialfood.gui.fragments;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.RecyclerViewActions;
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
 * Test class for PostDetailFragment, tests post detail display and interactions
 */
@RunWith(AndroidJUnit4.class)
public class PostDetailFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(
            new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class)
                    .putExtra(MainActivity.EXTRA_SKIP_LOGIN, true));

    @Rule
    public GrantPermissionRule locationPermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    /**
     * Sets up the test environment before each test
     * Creates test user and navigates to post detail
     */
    @Before
    public void setup() {
        createTestUser();
        navigateToPostDetail();
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
     * Navigates to post detail by clicking first post
     */
    private void navigateToPostDetail() {
        onView(withId(R.id.nav_profile))
                .perform(click());
        onView(withId(R.id.profile_posts_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    /**
     * Tests if post detail content is displayed correctly
     */
    @Test
    public void testPostDetailContent() {
        onView(withId(R.id.post_image))
                .check(matches(isDisplayed()));
        onView(withId(R.id.post_description))
                .check(matches(isDisplayed()));
        onView(withId(R.id.post_recipe))
                .check(matches(isDisplayed()));
        onView(withId(R.id.post_ingredients))
                .check(matches(isDisplayed()));
    }


    /**
     * Tests if like functionality works
     */
    @Test
    public void testLikeInteraction() {
        onView(withId(R.id.like_button))
                .perform(click());
        onView(withId(R.id.like_count))
                .check(matches(isDisplayed()));
    }

}