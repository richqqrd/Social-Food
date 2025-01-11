package com.example.socialfood.camera;

/**
 * Interface for communication between CameraFragment and other components when a photo has been
 * successfully taken.
 */
public interface CameraCallback {
    /**
     * Called when a photo has been successfully captured
     * 
     * @param photoPath The absolute path to the saved photo file
     */
    void onPhotoTaken(String photoPath);
}
