package com.example.socialfood.gui.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialfood.R;
import com.example.socialfood.controller.Navigation.NavigationController;
import com.example.socialfood.controller.Navigation.NavigationControllerInterface;
import com.example.socialfood.controller.Post.PostController;
import com.example.socialfood.controller.Post.PostControllerInterface;
import com.example.socialfood.controller.User.UserController;
import com.example.socialfood.controller.User.UserControllerInterface;
import com.example.socialfood.databinding.FragmentProfileBinding;
import com.example.socialfood.gui.adapters.PostAdapter;
import com.example.socialfood.model.entities.Post;
import com.example.socialfood.model.entities.User;

import java.util.List;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private NavigationControllerInterface navigationController;
    private User currentUser;
    private List<Post> usersPosts;
    private UserControllerInterface userController;
    private PostControllerInterface postController;
    private PostAdapter postAdapter;

    public static ProfileFragment newInstance(User user, List<Post> posts,
            UserControllerInterface userController, PostControllerInterface postController,
            NavigationControllerInterface navigationController) {
        ProfileFragment fragment = new ProfileFragment();
        fragment.currentUser = user;
        fragment.usersPosts = posts;
        fragment.userController = userController;
        fragment.postController = postController;
        fragment.navigationController = navigationController;
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        setupFollowButton();
        loadProfileData();
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 3);
        binding.profilePostsRecycler.setLayoutManager(layoutManager);
        binding.profilePostsRecycler.addItemDecoration(new GridSpacingItemDecoration());
        postAdapter = new PostAdapter(usersPosts, false, navigationController);
        binding.profilePostsRecycler.setAdapter(postAdapter);
    }

    private void setupFollowButton() {
        // Korrekte Bedingung
        if (currentUser != null && currentUser.getUid() != userController.getCurrentUserId()) {
            // Zeige Button bei fremden Profilen
            binding.followButton.setVisibility(View.VISIBLE);
            updateFollowButtonState();

            binding.followButton.setOnClickListener(v -> {
                if (userController.isFollowing(currentUser.getUid())) {
                    userController.unfollowUser(currentUser.getUid());
                } else {
                    userController.followUser(currentUser.getUid());
                }
                updateFollowButtonState();
                loadProfileData();
            });
        } else {
            // Verstecke Button beim eigenen Profil
            binding.followButton.setVisibility(View.GONE);
        }
    }

    private void updateFollowButtonState() {
        boolean isFollowing = userController.isFollowing(currentUser.getUid());
        binding.followButton.setText(isFollowing ? R.string.unfollow : R.string.follow);
    }

    private void loadProfileData() {
        // Model: Daten abrufen
        currentUser = userController.getUserById(currentUser.getUid());
        List<Post> userPosts = postController.getPostsFromUser(currentUser.getUid());
        int actualPostCount = userPosts.size();

        // Controller: Business Logic
        if (currentUser.getPostsCount() != actualPostCount) {
            currentUser.setPostsCount(actualPostCount);
            userController.updateUser(currentUser);
        }

        // View: UI Aktualisierung
        binding.profileUsername.setText(currentUser.getUsername());
        binding.profileBio.setText(currentUser.getBio());
        binding.profilePostsCount
                .setText(getString(R.string.posts_count, currentUser.getPostsCount()));
        updateFollowerCount();
    }

    private void updateFollowerCount() {
        binding.profileFollowersCount.setText(
                getString(R.string.followers_count, currentUser.getFollowersCount()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int spacing = 1; // 1dp spacing
            int position = parent.getChildAdapterPosition(view);
            int column = position % 3;

            outRect.left = column * spacing / 3;
            outRect.right = spacing - (column + 1) * spacing / 3;
            if (position >= 3) {
                outRect.top = spacing;
            }
        }
    }
}
