package com.example.socialfood.gui.activities;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.example.socialfood.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test class for MainActivity, tests basic navigation and UI functionality.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<com.example.socialfood.gui.activities.MainActivity> activityRule = new ActivityScenarioRule<>(
            new Intent(ApplicationProvider.getApplicationContext(), com.example.socialfood.gui.activities.MainActivity.class)
                    .putExtra(com.example.socialfood.gui.activities.MainActivity.EXTRA_SKIP_LOGIN, true));

    @Rule
    public GrantPermissionRule locationPermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    /**
     * Tests if map navigation is working
     */
    @Test
    public void testNavigationToMap() {
        onView(withId(R.id.nav_map))
                .perform(click());

        onView(withId(R.id.osm_map))
                .check(matches(isDisplayed()));
    }

    /**
     * Tests if profile navigation is working
     */
    @Test
    public void testNavigationToProfile() {
        onView(withId(R.id.nav_profile))
                .perform(click());

        onView(withId(R.id.profile_posts_recycler))
                .check(matches(isDisplayed()));
    }

    /**
     * Tests if settings navigation is working
     */
    @Test
    public void testNavigationToSettings() {
        onView(withId(R.id.nav_settings))
                .perform(click());

        onView(withText("Push-Benachrichtigungen"))
                .check(matches(isDisplayed()));
    }
}