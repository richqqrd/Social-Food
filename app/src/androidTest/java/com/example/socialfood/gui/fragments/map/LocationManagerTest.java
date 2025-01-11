package com.example.socialfood.gui.fragments.map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.ContextCompat;

import com.example.socialfood.gui.fragments.Map.LocationManager;
import androidx.fragment.app.Fragment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Test class for LocationManager, tests location functionality and error handling.
 * Uses Mockito to mock MapView and location components.
 */
@RunWith(MockitoJUnitRunner.class)
public class LocationManagerTest {

    @Mock
    private Context mockContext;

    @Mock
    private MapView mockMapView;

    @Mock
    private MyLocationNewOverlay mockLocationOverlay;

    @Mock
    private Fragment mockFragment;

    @Mock
    private ActivityResultLauncher<String> mockPermissionLauncher;

    private LocationManager locationManager;

    /**
     * Sets up test environment before each test.
     * Initializes mocks and creates LocationManager instance.
     */
    @Before
    public void setup() {
        locationManager = new LocationManager(mockMapView, mockContext);
        try {
            Field locationOverlayField = LocationManager.class.getDeclaredField("locationOverlay");
            locationOverlayField.setAccessible(true);
            locationOverlayField.set(locationManager, mockLocationOverlay);
        } catch (Exception e) {
            fail("Could not set mocked locationOverlay: " + e.getMessage());
        }
    }

    /**
     * Tests cleanup method properly disables location features
     */
    @Test
    public void testCleanup() {
        List<Overlay> overlays = mock(List.class);
        when(mockMapView.getOverlays()).thenReturn(overlays);

        locationManager.cleanup();

        verify(mockLocationOverlay).disableMyLocation();
        verify(mockLocationOverlay).disableFollowLocation();
        verify(overlays).remove(mockLocationOverlay);
    }

    /**
     * Tests if location permission check works correctly when permission is granted.
     * Verifies that location features are enabled when permission is available.
     */
    @Test
    public void testLocationPermissionCheck() {
        when(ContextCompat.checkSelfPermission(mockContext,
                Manifest.permission.ACCESS_FINE_LOCATION))
                        .thenReturn(PackageManager.PERMISSION_GRANTED);

        locationManager.checkLocationPermission(mockFragment, mockPermissionLauncher);

        verify(mockLocationOverlay).enableMyLocation();
    }

    /**
     * Tests permission check when permission is denied
     */
    @Test
    public void testLocationPermissionDenied() {
        when(ContextCompat.checkSelfPermission(mockContext,
                Manifest.permission.ACCESS_FINE_LOCATION))
                        .thenReturn(PackageManager.PERMISSION_DENIED);

        locationManager.checkLocationPermission(mockFragment, mockPermissionLauncher);

        verify(mockPermissionLauncher).launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    /**
     * Tests setup of location overlay
     */
    @Test
    public void testSetupLocationOverlay() {
        List<Overlay> overlays = mock(List.class);
        when(mockMapView.getOverlays()).thenReturn(overlays);

        locationManager.setLocationOverlay(mockLocationOverlay);

        locationManager.setupLocationOverlay();

        verify(mockLocationOverlay).enableMyLocation();
        verify(mockLocationOverlay).enableFollowLocation();
        verify(overlays).add(mockLocationOverlay);
    }

    /**
     * Tests error handling during setup
     */
    @Test
    public void testSetupLocationOverlayError() {
        String TAG = LocationManager.class.getSimpleName();

        when(mockLocationOverlay.enableMyLocation())
                .thenThrow(new RuntimeException("Test Exception"));

        locationManager.setupLocationOverlay();
    }
}