package com.example.socialfood.controller.Navigation;

import androidx.fragment.app.Fragment;

import com.example.socialfood.model.entities.Post;

import org.osmdroid.views.MapView;

import java.util.List;

/**
 * Interface defining navigation operations for the social food application. Handles transitions
 * between different views and screens.
 */
public interface NavigationControllerInterface {
    /**
     * Navigates to a user's profile page
     * 
     * @param userId The ID of the user whose profile should be shown
     */
    void showProfile(int userId);

    /**
     * Navigates to the settings screen
     */
    void showSettings();

    /**
     * Displays the map view with all posts
     */
    void showMap();

    /**
     * Shows the camera interface for taking photos
     */
    void showCamera();

    /**
     * Displays the detailed view of a post
     * 
     * @param post The post to show details for
     */
    void showPostDetail(Post post);

    /**
     * Shows the post creation screen
     * 
     * @param photoPath Path to the photo to be used in the post
     */
    void showPostCreation(String photoPath);

    /**
     * Displays posts as markers on the map
     * 
     * @param posts List of posts to display
     * @param mapView The MapView to add markers to
     */
    void showPostsOnMap(List<Post> posts, MapView mapView);

    /**
     * Handles navigation after a photo is taken
     * 
     * @param photoPath Path to the captured photo
     */
    void onPhotoTaken(String photoPath);

    /**
     * Handles navigation when a post is clicked
     * 
     * @param postId ID of the clicked post
     */
    void onClickPost(int postId);

    /**
     * Replaces the current fragment with a new one
     * 
     * @param fragment The new fragment to display
     */
    void replaceFragment(Fragment fragment);
}
