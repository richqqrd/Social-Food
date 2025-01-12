package com.example.socialfood.controller.Navigation;

import com.example.socialfood.controller.Post.PostControllerInterface;
import com.example.socialfood.controller.User.UserControllerInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.content.Context;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.socialfood.R;
import com.example.socialfood.camera.CameraFragment;
import com.example.socialfood.controller.BaseController;
import com.example.socialfood.controller.Post.PostController;
import com.example.socialfood.controller.User.UserController;
import com.example.socialfood.gui.fragments.Map.MapFragment;
import com.example.socialfood.gui.fragments.PostCreationFragment;
import com.example.socialfood.gui.fragments.PostDetailFragment;
import com.example.socialfood.gui.fragments.ProfileFragment;
import com.example.socialfood.gui.fragments.SettingsFragment;
import com.example.socialfood.model.entities.Post;
import com.example.socialfood.model.entities.User;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.List;

/**
 * Controller class responsible for handling navigation between different views
 * in the application.
 * Manages fragment transactions and view transitions.
 */
public class NavigationController extends BaseController
        implements com.example.socialfood.controller.Navigation.NavigationControllerInterface {
    private final FragmentManager fragmentManager;
    private PostControllerInterface postController;
    private UserControllerInterface userController;
    private final BottomNavigationView bottomNavigation;

    /**
     * Constructor for NavigationController
     * 
     * @param context          Application context
     * @param fragmentManager  FragmentManager for handling fragment transactions
     * @param postController   Controller for post-related operations
     * @param userController   Controller for user-related operations
     * @param bottomNavigation Bottom navigation view for main navigation
     */
    public NavigationController(Context context, FragmentManager fragmentManager,
            PostControllerInterface postController, UserControllerInterface userController,
            BottomNavigationView bottomNavigation) {
        super(context);
        this.fragmentManager = fragmentManager;
        this.postController = postController;
        this.userController = userController;
        this.bottomNavigation = bottomNavigation;
        setupNavigation();
    }

    /**
     * Sets up the bottom navigation listener
     */
    public void setupNavigation() {
        bottomNavigation.setOnNavigationItemSelectedListener(this::handleNavigationItemSelected);
    }

    /**
     * Handles bottom navigation item selection
     * 
     * @param item The selected menu item
     * @return true if the selection was handled, false otherwise
     */
    private boolean handleNavigationItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_map) {
            showMap();
            return true;
        } else if (itemId == R.id.nav_camera) {
            showCamera();
            return true;
        } else if (itemId == R.id.nav_profile) {
            showProfile(userController.getCurrentUserId());
            return true;
        } else if (itemId == R.id.nav_settings) {
            showSettings();
            return true;
        }
        return false;
    }

    /**
     * Shows the profile page for a specific user
     * 
     * @param userId The ID of the user whose profile should be shown
     */
    @Override
    public void showProfile(int userId) {
        User user = userController.getUserById(userId);
        List<Post> userPosts = postController.getPostsFromUser(userId);

        ProfileFragment profileFragment = ProfileFragment.newInstance(
                user,
                userPosts,
                userController,
                postController,
                this);

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, profileFragment)
                .addToBackStack(null)
                .commit();

    }

    /**
     * Shows the settings screen by replacing the current fragment with
     * SettingsFragment
     */
    @Override
    public void showSettings() {
        replaceFragment(new SettingsFragment());
    }

    /**
     * Shows the map view with all available posts Retrieves all posts from the
     * database and
     * displays them on the map
     */
    @Override
    public void showMap() {
        List<Post> allPosts = postController.getAllPosts();
        MapFragment mapFragment = MapFragment.newInstance(this, postController, allPosts);
        replaceFragment(mapFragment);
    }

    /**
     * Shows the camera view for taking photos Sets up the camera callback to handle
     * captured photos
     */
    @Override
    public void showCamera() {
        CameraFragment cameraFragment = new CameraFragment();
        cameraFragment.setCameraCallback(this::onPhotoTaken);
        replaceFragment(cameraFragment);
    }

    /**
     * Shows the detailed view of a post Retrieves the full post data and displays
     * it in
     * PostDetailFragment
     *
     * @param post The post to show details for
     */
    @Override
    public void showPostDetail(Post post) {
        Post fullPost = postController.getPostById(post.getUid(), post.getPostId());
        if (fullPost != null) {
            PostDetailFragment detailFragment = PostDetailFragment.newInstance(
                    fullPost,
                    this,
                    postController,
                    userController);
            replaceFragment(detailFragment);
        }
    }

    /**
     * Shows the post creation screen with a captured photo Creates a new
     * PostCreationFragment and
     * initializes it with the photo path
     *
     * @param photoPath The path to the captured photo that will be used in the post
     */
    @Override
    public void showPostCreation(String photoPath) {
        PostCreationFragment postCreationFragment = PostCreationFragment.newInstance(photoPath,
                postController, this);
        replaceFragment(postCreationFragment);
    }

    /**
     * Shows posts on the map by creating markers for each post location
     *
     * @param posts   List of posts to display on the map
     * @param mapView The MapView to add markers to
     */
    @Override
    public void showPostsOnMap(List<Post> posts, MapView mapView) {
        for (Post post : posts) {
            Marker marker = new Marker(mapView);
            GeoPoint postPoint = new GeoPoint(post.getLatitude(), post.getLongitude());
            marker.setPosition(postPoint);
            marker.setTitle(post.getDescription());
            marker.setSnippet("Klicken zum Anzeigen");

            marker.setOnMarkerClickListener((marker1, mapView1) -> {
                Post clickedPost = postController.getPostById(post.getUid(), post.getPostId());
                showPostDetail(clickedPost);
                return true;
            });

            mapView.getOverlays().add(marker);
        }
        mapView.invalidate();
    }

    /**
     * Handles navigation after a photo has been taken
     *
     * @param photoPath Path to the captured photo
     */
    @Override
    public void onPhotoTaken(String photoPath) {
        showPostCreation(photoPath);
    }

    /**
     * Handles navigation when a post is clicked
     *
     * @param postId ID of the clicked post
     */
    @Override
    public void onClickPost(int postId) {
        Post post = postController.getPost(postId);
        if (post != null) {
            showPostDetail(post);
        }
    }

    /**
     * Replaces the current fragment with a new one
     *
     * @param fragment The new fragment to display
     */
    @Override
    public void replaceFragment(Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
