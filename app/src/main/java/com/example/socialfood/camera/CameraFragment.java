package com.example.socialfood.camera;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.socialfood.databinding.FragmentCameraBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;

/**
 * Fragment for handling camera functionality in the app. Manages camera preview, photo capture and
 * permissions.
 */
public class CameraFragment extends Fragment {
    private static final String TAG = "CameraFragment";
    private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;

    private FragmentCameraBinding binding;
    private ProcessCameraProvider cameraProvider;
    private ImageCapture imageCapture;
    private CameraCallback cameraCallback;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    /**
     * Sets the callback for photo capture events
     *
     * @param callback The callback to be notified when a photo is taken
     * @throws IllegalArgumentException if callback is null
     */
    public void setCameraCallback(CameraCallback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("CameraCallback cannot be null");
        }
        this.cameraCallback = callback;
    }

    /**
     * Initializes permission launcher when fragment is created
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializePermissionLauncher();
    }

    /**
     * Creates and initializes the fragment's view hierarchy
     * 
     * @param inflater The LayoutInflater object to inflate views
     * @param container The parent view to attach to
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     * saved state
     * @return The View for the fragment's UI
     */
    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentCameraBinding.inflate(inflater, container, false);
        setupUI();
        return binding.getRoot();
    }

    /**
     * Sets up the camera UI and checks for required permissions
     */
    private void setupUI() {
        checkPermission(binding.getRoot());
        binding.captureButton.setOnClickListener(v -> takePhoto());
    }

    /**
     * Called when the fragment is resumed. Checks camera permission and sets up camera if
     * permission is granted.
     */
    @Override
    public void onResume() {
        super.onResume();
        if (hasCameraPermission()) {
            setupCamera();
        } else {
            checkPermission(binding.getRoot());
        }
    }

    /**
     * Called when the fragment is paused. Unbinds all camera use cases to release resources.
     */
    @Override
    public void onPause() {
        super.onPause();
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }
    }

    /**
     * Called when the fragment's view is destroyed. Cleans up resources to prevent memory leaks.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Initializes the permission launcher for camera permission requests. Sets up the callback to
     * handle permission results.
     */
    private void initializePermissionLauncher() {
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                this::handlePermissionResult);
    }

    /**
     * Handles the result of a camera permission request. Sets up camera if permission is granted.
     *
     * @param isGranted true if permission was granted, false otherwise
     */
    private void handlePermissionResult(boolean isGranted) {
        if (isGranted) {
            Log.d(TAG, "Camera permission granted");
            setupCamera();
        }
    }

    /**
     * Takes a photo using the camera Saves the image and notifies via callback
     */
    private void takePhoto() {
        Log.d("Camera", "takePhoto");

        if (imageCapture == null) {
            showToast("Kamera nicht bereit");
            return;
        }

        File photoFile = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "SocialFood_photo" + System.currentTimeMillis() + ".jpg");
        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(
                photoFile).build();

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(requireContext()),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(
                            @NonNull ImageCapture.OutputFileResults outputFileResults) {
                        addPhotoToGallery(photoFile);
                        cameraCallback.onPhotoTaken(photoFile.getAbsolutePath());
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        showToast("Fehler beim Speichern des Fotos");
                        Log.e(TAG, "Photo capture failed: " + exception.getMessage());
                    }
                });
    }

    /**
     * Checks if the app has camera permission
     * 
     * @return true if permission is granted, false otherwise
     */
    protected void checkPermission(View view) {
        Log.d(TAG, "checkPermission");
        if (hasCameraPermission()) {
            setupCamera();
            return;
        }
        Snackbar.make(view,
                "Kamera-Berechtigung benötigt, um Fotos aufzunehmen",
                Snackbar.LENGTH_INDEFINITE)
                .setAction("Erlauben",
                        v -> requestPermissionLauncher.launch(CAMERA_PERMISSION))
                .show();
    }

    /**
     * Checks if camera permission is granted
     * 
     * @return true if permission is granted, false otherwise
     */
    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(requireContext(),
                CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Adds the captured photo to the device's media gallery
     * 
     * @param photoFile The file containing the captured photo
     */
    private void addPhotoToGallery(File photoFile) {
        Log.d(TAG, "addPhoto");

        MediaScannerConnection.scanFile(
                requireContext(),
                new String[] { photoFile.getAbsolutePath() },
                null, (path, uri) -> {
                    Toast.makeText(requireContext(),
                            "Foto gespeichert: " + photoFile.getAbsolutePath(), Toast.LENGTH_SHORT)
                            .show();
                });
    }

    /**
     * Checks if the camera is available and bound to the capture use case
     * 
     * @return true if camera is available and bound, false otherwise
     */
    private boolean isCameraAvailable() {
        return cameraProvider != null && cameraProvider.isBound(imageCapture);
    }

    /**
     * Shows a toast message to the user
     * 
     * @param message The message to display
     */
    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Initializes the camera preview if permission is granted
     */
    protected void setupCamera() {
        Log.d(TAG, "setupCamera");
        if (isCameraAvailable()) {
            Log.d(TAG, "Camera already available");
            return;
        }

        ProcessCameraProvider.getInstance(requireContext()).addListener(() -> {
            try {
                cameraProvider = ProcessCameraProvider.getInstance(requireContext()).get();
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(binding.cameraPreview.getSurfaceProvider());

                imageCapture = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
                Log.d(TAG, "Camera setup successful");

            } catch (Exception e) {
                Log.e(TAG, "Camera setup failed", e);
                showToast("Kamera-Setup fehlgeschlagen");
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }
}
