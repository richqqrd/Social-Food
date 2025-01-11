package com.example.socialfood.gui.fragments.Map;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.socialfood.controller.Navigation.NavigationController;
import com.example.socialfood.controller.Post.PostController;
import com.example.socialfood.databinding.FragmentMapBinding;
import com.example.socialfood.model.entities.Post;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.List;

/**
 * Fragment for displaying posts on a map using OpenStreetMap. Handles map initialization, location
 * permissions and post markers.
 */
public class MapFragment extends Fragment {
    private FragmentMapBinding binding;
    private MapView mapView;
    private NavigationController navigationController;
    private PostController postController;
    private List<Post> posts;
    private com.example.socialfood.gui.fragments.Map.MapManager mapManager;
    private LocationManager locationManager;
    private static final String TAG = "MapFragment";

    /**
     * Creates a new instance of MapFragment with the required dependencies.
     *
     * @param navigationController The controller for handling navigation
     * @param postController The controller for handling post operations
     * @param posts The list of posts to display on the map
     * @return A new instance of MapFragment
     */
    public static MapFragment newInstance(NavigationController navigationController,
            PostController postController, List<Post> posts) {
        return new MapFragment(navigationController, postController, posts);
    }

    /**
     * Private constructor to enforce usage of newInstance().
     *
     * @param navigationController The controller for handling navigation
     * @param postController The controller for handling post operations
     * @param posts The list of posts to display on the map
     */
    private MapFragment(NavigationController navigationController, PostController postController,
            List<Post> posts) {
        this.navigationController = navigationController;
        this.postController = postController;
        this.posts = posts;
    }

    public void setLocationManager(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    /**
     * Creates and initializes the fragment's view hierarchy. Sets up the map and location services.
     *
     * @param inflater The LayoutInflater object to inflate views
     * @param container The parent view to attach to
     * @param savedInstanceState State data to restore, if available
     * @return The fragment's root View
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        mapView = binding.osmMap;

        mapManager = new com.example.socialfood.gui.fragments.Map.MapManager(mapView, requireContext());
        locationManager = new LocationManager(mapView, requireContext());

        locationManager.checkLocationPermission(this, requestPermissionLauncher);

        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        }
        return binding.getRoot();
    }

    /**
     * Launcher for requesting location permissions from the user. Handles the permission result and
     * enables location features if granted.
     */
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    locationManager.setupLocationOverlay();
                }
            });

    /**
     * Loads and displays all posts on the map. Retrieves posts from controller and shows them as
     * markers.
     */
    private void loadAndDisplayPosts() {
        try {
            posts = postController.getAllPosts();
            if (posts != null && !posts.isEmpty()) {
                navigationController.showPostsOnMap(posts, mapView);
            } else {
                Log.d(TAG, "No posts found to display");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading posts", e);
        }
    }

    /**
     * Sets the list of posts to display on the map.
     *
     * @param posts The list of posts to display
     */
    public void setPosts(List<Post> posts) {
        this.posts = posts;
        if (mapView != null && isAdded()) {
            navigationController.showPostsOnMap(posts, mapView);
        }
    }

    /**
     * Saves the current map state to a bundle.
     *
     * @param outState The bundle to save the state in
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null) {
            mapManager.saveMapState(outState);
        }
    }

    /**
     * Restores the map state from a saved instance state bundle.
     *
     * @param savedInstanceState The bundle containing the saved state
     */
    private void restoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && mapView != null) {
            mapManager.restoreMapState(savedInstanceState);
        }
    }

    /**
     * Lifecycle method called when fragment resumes. Resumes the map view and reloads posts.
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        loadAndDisplayPosts();
    }

    /**
     * Lifecycle method called when fragment pauses. Pauses the map view.
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * Lifecycle method called when fragment's view is destroyed. Cleans up bindings to prevent
     * memory leaks.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Gets the current zoom level of the map
     * 
     * @return current zoom level as float
     */
    public float getCurrentZoomLevel() {
        return (float) mapView.getZoomLevelDouble();
    }

    /**
     * Gets the current center position of the map
     * 
     * @return GeoPoint representing the center position
     */
    public GeoPoint getCurrentMapCenter() {
        return (GeoPoint) mapView.getMapCenter();
    }

    /**
     * Gets the MapView instance
     * 
     * @return The MapView used by this fragment
     */
    public MapView getMapView() {
        return mapView;
    }

}
