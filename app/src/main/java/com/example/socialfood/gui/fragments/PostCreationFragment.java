package com.example.socialfood.gui.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.socialfood.R;

import com.example.socialfood.controller.Navigation.NavigationController;
import com.example.socialfood.controller.Post.PostController;
import com.example.socialfood.databinding.FragmentPostCreationBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class PostCreationFragment extends Fragment {
    private FragmentPostCreationBinding binding;
    private String photoPath;
    private PostController postController;
    private NavigationController navigationController;
    private static final String ARG_PHOTO_PATH = "photo_path";

    public static PostCreationFragment newInstance(String photoPath, PostController postController,
            NavigationController navigationController) {
        PostCreationFragment fragment = new PostCreationFragment();
        fragment.photoPath = photoPath;
        fragment.postController = postController;
        fragment.navigationController = navigationController;
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentPostCreationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            photoPath = getArguments().getString(ARG_PHOTO_PATH);
        }

        setupUI();
        setupListeners();
    }

    private void setupUI() {
        if (photoPath != null) {
            Glide.with(this)
                    .load(photoPath)
                    .into(binding.previewImage);
        }
        setupIngredientChips();
    }

    private void setupListeners() {
        binding.postButton.setOnClickListener(v -> createPost());
    }

    private void createPost() {
        String description = binding.descriptionInput.getText().toString();
        String recipe = binding.recipeInput.getText().toString();
        String ingredients = getSelectedIngredients();

        if (!validateInputs(description, recipe, ingredients)) {
            return;
        }

        LocationManager locationManager = (LocationManager) requireContext()
                .getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            showLocationPermissionError();
            return;
        }

        try {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showNoLocationError();
                return;
            }

            // Versuche Location von verschiedenen Providern zu bekommen
            Location lastLocation = null;

            // Prüfe GPS Provider
            lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            // Falls GPS keine Location liefert, versuche Network Provider
            if (lastLocation == null) {
                lastLocation = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (lastLocation != null) {
                double latitude = lastLocation.getLatitude();
                double longitude = lastLocation.getLongitude();

                if (postController.createPost(photoPath, description, recipe, ingredients, latitude,
                        longitude)) {
                    showSuccessMessage();
                    navigateBack();
                } else {
                    showErrorMessage();
                }
            } else {
                showNoLocationError();
            }
        } catch (SecurityException e) {
            Log.e("PostCreationFragment", "Error getting location", e);
            showLocationError();
        }
    }

    private void showLocationPermissionError() {
        Snackbar.make(binding.getRoot(),
                "Standortberechtigung wird benötigt",
                Snackbar.LENGTH_LONG)
                .setAction("Einstellungen", v -> openAppSettings())
                .show();
    }

    private void showNoLocationError() {
        Snackbar.make(binding.getRoot(),
                "Kein Standort verfügbar. Bitte GPS aktivieren.",
                Snackbar.LENGTH_LONG)
                .show();
    }

    private void showLocationError() {
        Snackbar.make(binding.getRoot(),
                "Fehler beim Abrufen des Standorts",
                Snackbar.LENGTH_LONG)
                .show();
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", requireContext().getPackageName(), null);
        intent.setData(uri);
    }

    private void showSuccessMessage() {
        Toast.makeText(requireContext(), R.string.post_creation_success, Toast.LENGTH_SHORT).show();
    }

    private void showErrorMessage() {
        Toast.makeText(requireContext(), R.string.post_creation_error, Toast.LENGTH_SHORT).show();
    }

    private void navigateBack() {
        navigationController.showMap();
    }

    private void setupIngredientChips() {
        String[] ingredients = { "Mehl", "Zucker", "Eier", "Milch", "Butter", "Salz", "Hefe",
                "Öl" }; // Beispiel-Zutaten

        for (String ingredient : ingredients) {
            Chip chip = new Chip(requireContext());
            chip.setText(ingredient);
            chip.setCheckable(true);
            binding.ingredientsChipGroup.addView(chip);
        }
    }

    private String getSelectedIngredients() {
        List<String> selectedIngredients = new ArrayList<>();
        ChipGroup chipGroup = binding.ingredientsChipGroup;

        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (chip.isChecked()) {
                selectedIngredients.add(chip.getText().toString());
            }
        }

        return String.join(", ", selectedIngredients);
    }

    private boolean validateInputs(String description, String recipe, String ingredients) {
        if (description.isEmpty() || recipe.isEmpty() || ingredients.isEmpty()) {
            showInputError();
            return false;
        }
        return true;
    }

    private void showInputError() {
        Toast.makeText(requireContext(), "Bitte füllen Sie alle Felder aus", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
