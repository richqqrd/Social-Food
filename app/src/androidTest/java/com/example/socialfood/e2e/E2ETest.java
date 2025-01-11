package com.example.socialfood.e2e;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;
import android.os.SystemClock;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.example.socialfood.R;
import com.example.socialfood.gui.activities.LoginActivity;
import com.example.socialfood.utils.UserManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * End-to-End Test class for testing the complete user journey in the Social Food app.
 */
@RunWith(AndroidJUnit4.class)
public class E2ETest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Rule
    public GrantPermissionRule locationPermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule cameraPermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.CAMERA);

    @Rule
    public GrantPermissionRule storagePermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

    @Before
    public void setup() {
        // Reset app state
        Context context = ApplicationProvider.getApplicationContext();
        UserManager.getInstance(context).logoutUser();
    }

    /**
     * Tests the complete user journey from registration through post creation.
     */
    @Test
    public void testCompleteUserJourney() {
        onView(withId(R.id.register_button))
                .perform(click());

        onView(withId(R.id.usernameInput))
                .perform(typeText("testuser"), closeSoftKeyboard());
        onView(withId(R.id.passwordInput))
                .perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.passwordConfirmInput))
                .perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.createUserButton))
                .perform(click());

        SystemClock.sleep(5000);

        onView(withId(R.id.login_username))
                .check(matches(isDisplayed()))
                .perform(typeText("testuser"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.login_button))
                .perform(click());

        onView(withId(R.id.nav_camera))
                .perform(click());

        SystemClock.sleep(3000);

        onView(withId(R.id.capture_button))
                .perform(click());

        SystemClock.sleep(2000);

        onView(withId(R.id.description_input))
                .perform(typeText("Test post"), closeSoftKeyboard());
        onView(withId(R.id.recipe_input))
                .perform(typeText("Test recipe"), closeSoftKeyboard());
        onView(withId(R.id.ingredients_chip_group))
                .perform(click());
        onView(withId(R.id.post_button))
                .perform(click());

        onView(withId(R.id.nav_profile))
                .perform(click());
        onView(withId(R.id.profile_username))
                .check(matches(withText("TestUser1")));
    }


    @Test
    public void testNavigationJourney() {

        onView(withId(R.id.register_button))
                .perform(click());

        onView(withId(R.id.usernameInput))
                .perform(typeText("testuser"), closeSoftKeyboard());
        onView(withId(R.id.passwordInput))
                .perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.passwordConfirmInput))
                .perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.createUserButton))
                .perform(click());

        SystemClock.sleep(5000);

        onView(withId(R.id.login_username))
                .perform(typeText("testuser"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.login_button))
                .perform(click());

        onView(withId(R.id.nav_map))
                .perform(click());
        onView(withId(R.id.osm_map))
                .check(matches(isDisplayed()));

        onView(withId(R.id.nav_profile))
                .perform(click());
        onView(withId(R.id.profile_username))
                .check(matches(isDisplayed()));
        onView(withId(R.id.profile_posts_recycler))
                .check(matches(isDisplayed()));

        onView(withId(R.id.nav_camera))
                .perform(click());
        SystemClock.sleep(3000);
        onView(withId(R.id.capture_button))
                .check(matches(isDisplayed()));

        onView(withId(R.id.nav_map))
                .perform(click());
        onView(withId(R.id.osm_map))
                .check(matches(isDisplayed()));

        onView(withId(R.id.nav_settings))
                .perform(click());
        onView(withText("Push-Benachrichtigungen"))
                .check(matches(isDisplayed()));

        onView(withId(R.id.nav_profile))
                .perform(click());
        onView(withId(R.id.profile_posts_recycler))
                .check(matches(isDisplayed()));

        onView(withId(R.id.profile_posts_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.post_image))
                .check(matches(isDisplayed()));
        onView(withId(R.id.post_description))
                .check(matches(isDisplayed()));

        onView(withId(R.id.nav_profile))
                .perform(click());
    }
}