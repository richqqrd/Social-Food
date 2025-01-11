package com.example.socialfood.gui.fragments.Map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

/**
 * Manager class for handling location functionality in the map view. Responsible for location
 * permissions, overlay setup and location tracking.
 */
public class LocationManager {
    private static final String TAG = LocationManager.class.getSimpleName();
    private static final String PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;

    @NonNull
    private final MapView mapView;
    @NonNull
    private final Context context;
    private MyLocationNewOverlay locationOverlay;

    /**
     * Creates a new LocationManager instance.
     *
     * @param mapView The MapView instance to manage location features for
     * @param context The application context for permissions
     */
    public LocationManager(@NonNull MapView mapView, @NonNull Context context) {
        this.mapView = mapView;
        this.context = context;
    }

    /**
     * Sets the location overlay for testing purposes.
     * 
     * @param overlay The location overlay to set
     */
    public void setLocationOverlay(MyLocationNewOverlay overlay) {
        this.locationOverlay = overlay;
    }

    /**
     * Sets up the location overlay for tracking user position on the map. Enables location tracking
     * and following.
     */
    public void setupLocationOverlay() {
        try {
            if (locationOverlay == null) {
                locationOverlay = new MyLocationNewOverlay(
                        new GpsMyLocationProvider(context),
                        mapView);
            }
            locationOverlay.enableMyLocation();
            locationOverlay.enableFollowLocation();
            mapView.getOverlays().add(locationOverlay);
        } catch (Exception e) {
            Log.e(TAG, "Error setting up location overlay", e);
        }
    }

    /**
     * Checks and requests location permissions if needed.
     *
     * @param fragment The fragment requesting permissions
     * @param permissionLauncher Launcher for permission request
     */
    public void checkLocationPermission(@NonNull Fragment fragment,
            @NonNull ActivityResultLauncher<String> permissionLauncher) {
        try {
            if (hasLocationPermission()) {
                setupLocationOverlay();
            } else {
                permissionLauncher.launch(PERMISSION);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error checking location permission", e);
        }
    }

    /**
     * Checks if location permission is granted.
     *
     * @return true if permission is granted, false otherwise
     */
    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(context,
                PERMISSION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Cleans up location resources. Should be called when the manager is no longer needed.
     */
    public void cleanup() {
        if (locationOverlay != null) {
            locationOverlay.disableMyLocation();
            locationOverlay.disableFollowLocation();
            mapView.getOverlays().remove(locationOverlay);
        }
    }
}