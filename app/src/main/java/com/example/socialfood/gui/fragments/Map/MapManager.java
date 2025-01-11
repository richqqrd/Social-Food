package com.example.socialfood.gui.fragments.Map;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

/**
 * Manager class for handling OpenStreetMap view functionality. Responsible for map setup,
 * configuration and state management.
 * 
 */
public class MapManager {
    @NonNull
    private final MapView mapView;
    private static final double DEFAULT_ZOOM = 15.0;
    @NonNull
    private final Context context;
    private static final String TAG = "MapManager";

    /**
     * Creates a new MapManager instance.
     *
     * @param mapView The MapView instance to manage
     * @param context The application context for configuration
     */
    public MapManager(@NonNull MapView mapView, @NonNull Context context) {
        this.mapView = mapView;
        this.context = context;
        setupMap();
    }

    /**
     * Initializes and configures the map view. Loads OSMdroid configuration and sets basic
     * settings.
     */
    public void setupMap() {
        Configuration.getInstance().load(context,
                context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE));
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(DEFAULT_ZOOM);
    }

    /**
     * Saves the current map state to a bundle.
     *
     * @param outState Bundle to save the state in
     */
    public void saveMapState(Bundle outState) {
        outState.putDouble("latitude", mapView.getMapCenter().getLatitude());
        outState.putDouble("longitude", mapView.getMapCenter().getLongitude());
        outState.putDouble("zoom", mapView.getZoomLevelDouble());
    }

    /**
     * Restores a previously saved map state from a bundle.
     *
     * @param savedState Bundle containing the saved state
     */
    public void restoreMapState(Bundle savedState) {
        if (savedState == null)
            return;
        try {
            double latitude = savedState.getDouble("latitude");
            double longitude = savedState.getDouble("longitude");
            double zoom = savedState.getDouble("zoom");
            mapView.getController().setCenter(new GeoPoint(latitude, longitude));
            mapView.getController().setZoom(zoom);
        } catch (Exception e) {
            Log.e(TAG, "Error restoring map state", e);
        }
    }
}