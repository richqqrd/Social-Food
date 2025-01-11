package com.example.socialfood.gui.fragments;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import static org.hamcrest.Matchers.allOf;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.WindowManager;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Root;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.example.socialfood.R;
import com.example.socialfood.controller.Post.PostController;
import com.example.socialfood.controller.User.UserController;
import com.example.socialfood.gui.activities.MainActivity;
import com.example.socialfood.utils.ExampleData;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test class for PostCreationFragment, tests post creation functionality and UI elements
 * after taking a photo.
 * Uses ActivityScenarioRule for MainActivity and requires camera and location permissions.
 */
@RunWith(AndroidJUnit4.class)
public class PostCreationFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(
            new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class)
                    .putExtra(MainActivity.EXTRA_SKIP_LOGIN, true));

    @Rule
    public GrantPermissionRule locationPermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule cameraPermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.CAMERA);

    /**
     * Sets up the test environment before each test
     * Creates test user and navigates to post creation
     */
    @Before
    public void setup() {
        createTestUser();
        takePictureAndNavigateToPostCreation();
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
     * Takes picture and navigates to post creation
     */
    private void takePictureAndNavigateToPostCreation() {
        onView(withId(R.id.nav_camera))
                .perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.capture_button))
                .perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests if post creation UI elements are displayed correctly
     */
    @Test
    public void testPostCreationUI() {
        onView(withId(R.id.preview_image))
                .check(matches(isDisplayed()));

        onView(withId(R.id.description_input))
                .perform(scrollTo())
                .check(matches(isDisplayed()));

        onView(withId(R.id.recipe_input))
                .perform(scrollTo())
                .check(matches(isDisplayed()));

        onView(withId(R.id.ingredients_chip_group))
                .perform(scrollTo())
                .check(matches(isDisplayed()));

        onView(withId(R.id.post_button))
                .perform(scrollTo())
                .check(matches(isDisplayed()));
    }


    /**
     * Tests successful post creation with all fields filled
     */
    @Test
    public void testSuccessfulPostCreation() {
        onView(withId(R.id.description_input))
                .perform(scrollTo(), click())
                .perform(typeText("Test Description"), closeSoftKeyboard());

        onView(withId(R.id.recipe_input))
                .perform(scrollTo(), click())
                .perform(typeText("Test Recipe"), closeSoftKeyboard());

        onView(withId(R.id.ingredients_chip_group))
                .perform(scrollTo());

        onView(allOf(withParent(withId(R.id.ingredients_chip_group)), withText("Mehl")))
                .perform(click());

        onView(withId(R.id.post_button))
                .perform(scrollTo(), click());

        onView(withId(R.id.osm_map))
                .check(matches(isDisplayed()));
    }
}