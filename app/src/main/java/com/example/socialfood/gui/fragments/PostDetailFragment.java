package com.example.socialfood.gui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.example.socialfood.R;
import com.example.socialfood.controller.Navigation.NavigationController;
import com.example.socialfood.controller.Navigation.NavigationControllerInterface;
import com.example.socialfood.controller.Post.PostController;
import com.example.socialfood.controller.Post.PostControllerInterface;
import com.example.socialfood.controller.User.UserController;
import com.example.socialfood.controller.User.UserControllerInterface;
import com.example.socialfood.databinding.FragmentPostDetailBinding;
import com.example.socialfood.gui.adapters.CommentsAdapter;
import com.example.socialfood.model.entities.Comment;
import com.example.socialfood.model.entities.Post;
import com.example.socialfood.model.entities.User;

public class PostDetailFragment extends Fragment {
    private Post post;
    private final NavigationControllerInterface navigationController;
    private final PostControllerInterface postController;
    private final UserControllerInterface userController;
    private FragmentPostDetailBinding binding;
    private CommentsAdapter commentsAdapter;

    public static PostDetailFragment newInstance(Post post,
            NavigationControllerInterface navigationController, PostControllerInterface postController,
            UserControllerInterface userController) {
        return new PostDetailFragment(post, navigationController, postController, userController);
    }

    private PostDetailFragment(
            Post post,
            NavigationControllerInterface navigationController,
            PostControllerInterface postController,
            UserControllerInterface userController) {
        this.post = post;
        this.navigationController = navigationController;
        this.postController = postController;
        this.userController = userController;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentPostDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUI();
        setupInteractions();
    }

    private void setupUI() {
        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        setupUserInfo();
        setupPostContent();
        setupComments();
        updateLikeStatus();

        binding.sendCommentButton.setOnClickListener(v -> submitComment());

    }

    private void setupUserInfo() {
        User postUser = userController.getUserById(post.getUid());
        if (postUser != null) {
            binding.postUsername.setText(postUser.getUsername());
            binding.userProfileContainer.setOnClickListener(v ->
                    navigationController.showProfile(postUser.getUid()));
        }
    }

    private void setupPostContent() { // Detail-Ansicht
        binding.postDescription.setText(post.getDescription());
        binding.postIngredients
                .setText(getString(R.string.ingredients_format, post.getIngredients()));
        binding.postRecipe.setText(getString(R.string.recipe_format, post.getRecipe()));
        loadImage();
    }

    private void setupComments() {
        try {
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
            binding.commentsRecyclerView.setLayoutManager(layoutManager);
            List<Comment> comments = postController.getCommentsForPost(post.getPostId());
            if (comments != null) {
                commentsAdapter = new CommentsAdapter(comments, userController);
                binding.commentsRecyclerView.setAdapter(commentsAdapter);
                updateCommentCount();
            }
        } catch (Exception e) {
            Log.e("PostDetailFragment", "Error setting up comments", e);
            // Zeige leere Kommentarliste
            commentsAdapter = new CommentsAdapter(new ArrayList<>(), userController);
            binding.commentsRecyclerView.setAdapter(commentsAdapter);
        }
    }

    private void setupInteractions() {
        binding.likeButton.setOnClickListener(v -> {
            postController.onLikePost(post.getPostId());
            updateLikeStatus();
        });

        binding.commentButton.setOnClickListener(v -> submitComment());
    }

    private void submitComment() {
        String commentText = binding.commentInput.getText().toString().trim();
        if (!commentText.isEmpty()) {
            postController.onCommentPost(post.getPostId(), commentText);
            binding.commentInput.setText("");
            commentsAdapter.updateComments(postController.getCommentsForPost(post.getPostId()));
            commentsAdapter.notifyDataSetChanged();
            updateCommentCount();
        }
    }

    private void updateLikeStatus() {
        binding.likeCount.setText(String.valueOf(postController.getLikeCount(post.getPostId())));
        binding.likeButton.setSelected(postController.isPostLikedByUser(post.getPostId()));
    }

    private void updateCommentCount() {
        int commentCount = postController.getCommentsForPost(post.getPostId()).size();
        binding.commentCount.setText(String.valueOf(commentCount));
    }

    private void loadImage() {
        String imageUrl = post.getImageUrl();
        try {
            int resourceId = Integer.parseInt(imageUrl);
            Glide.with(this)
                    .load(resourceId)
                    .centerCrop()
                    .into(binding.postImage);
        } catch (NumberFormatException e) {
            Glide.with(this)
                    .load(imageUrl)
                    .centerCrop()
                    .into(binding.postImage);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}