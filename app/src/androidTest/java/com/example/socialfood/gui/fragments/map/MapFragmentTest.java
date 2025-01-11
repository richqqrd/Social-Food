package com.example.socialfood.gui.fragments.map;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.example.socialfood.R;
import com.example.socialfood.controller.Post.PostController;
import com.example.socialfood.controller.User.UserController;
import com.example.socialfood.gui.activities.MainActivity;
import com.example.socialfood.gui.fragments.Map.MapFragment;
import com.example.socialfood.utils.ExampleData;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osmdroid.views.MapView;

/**
 * Test class for MapFragment, tests map display and interaction functionality.
 * Uses ActivityScenarioRule to launch MainActivity and Espresso for UI testing.
 */
@RunWith(AndroidJUnit4.class)
public class MapFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(
            new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class)
                    .putExtra(MainActivity.EXTRA_SKIP_LOGIN, true));

    @Rule
    public GrantPermissionRule locationPermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    /**
     * Sets up the test environment before each test
     * Creates test user and navigates to map screen
     */
    @Before
    public void setup() {
        createTestUser();
        navigateToMap();
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
     * Navigates to the map screen
     */
    private void navigateToMap() {
        onView(withId(R.id.nav_map))
                .perform(click());
    }

    /**
     * Tests if map is displayed correctly
     */
    @Test
    public void testMapIsDisplayed() {
        onView(withId(R.id.osm_map))
                .check(matches(isDisplayed()));
    }

    /**
     * Tests if map container is displayed
     */
    @Test
    public void testMapContainerIsDisplayed() {
        onView(withId(R.id.fragment_map_container))
                .check(matches(isDisplayed()));
    }

    /**
     * Tests if the map zooming in functionality works correctly.
     * Verifies that after a pinch-in gesture the zoom level increases.
     */
    @Test
    public void testMapZoomIn() {
        final MapFragment[] mapFragment = new MapFragment[1];
        activityRule.getScenario().onActivity(activity -> {
            mapFragment[0] = (MapFragment) activity
                    .getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_container);
        });

        float initialZoom = mapFragment[0].getCurrentZoomLevel();

        onView(withId(R.id.osm_map))
                .perform(pinchIn());

        assertTrue(mapFragment[0].getCurrentZoomLevel() > initialZoom);
    }

    /**
     * Tests if the map zooming in functionality works correctly.
     * Verifies that after a pinch-in gesture the zoom level increases.
     */
    @Test
    public void testMapZoomOut() {
        final MapFragment[] mapFragment = new MapFragment[1];
        activityRule.getScenario().onActivity(activity -> {
            mapFragment[0] = (MapFragment) activity
                    .getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_container);
        });

        float initialZoom = mapFragment[0].getCurrentZoomLevel();

        onView(withId(R.id.osm_map))
                .perform(pinchOut());

        assertTrue(mapFragment[0].getCurrentZoomLevel() < initialZoom);
    }

    private ViewAction pinchIn() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isDisplayed();
            }

            @Override
            public String getDescription() {
                return "Pinch in on view";
            }

            @Override
            public void perform(UiController uiController, View view) {
                MapView mapView = (MapView) view;
                mapView.getController().zoomIn();
                uiController.loopMainThreadForAtLeast(500);
            }
        };
    }

    private ViewAction pinchOut() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isDisplayed();
            }

            @Override
            public String getDescription() {
                return "Pinch out on view";
            }

            @Override
            public void perform(UiController uiController, View view) {
                MapView mapView = (MapView) view;
                mapView.getController().zoomOut();
                uiController.loopMainThreadForAtLeast(500);
            }
        };
    }



}
