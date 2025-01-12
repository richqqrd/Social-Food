package com.example.socialfood.gui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialfood.R;
import com.example.socialfood.controller.Navigation.NavigationController;
import com.example.socialfood.controller.Navigation.NavigationControllerInterface;
import com.example.socialfood.model.entities.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private final List<Post> posts;
    private final boolean isDetailView;
    private final NavigationControllerInterface navigationController;

    public PostAdapter(List<Post> posts, boolean isDetailView,
            NavigationControllerInterface navigationController) {
        this.posts = posts;
        this.isDetailView = isDetailView;
        this.navigationController = navigationController;
    }

    @NonNull @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent,
                false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostViewHolder holder, int position) {
        Post post = posts.get(position);
        String imageUrl = post.getImageUrl();

        holder.itemView.setOnClickListener(v -> {
            if (!isDetailView) {
                navigationController.showPostDetail(post);
            }
        });

        if (isDetailView) {
            holder.description.setVisibility(View.VISIBLE);
            holder.ingredients.setVisibility(View.VISIBLE);
            holder.recipe.setVisibility(View.VISIBLE);
            holder.description.setText(post.getDescription());
            holder.ingredients.setText("Zutaten:\n" + post.getIngredients());
            holder.recipe.setText("Rezept:\n" + post.getRecipe());
        } else {
            holder.description.setVisibility(View.GONE);
            holder.ingredients.setVisibility(View.GONE);
            holder.recipe.setVisibility(View.GONE);
        }

        try {
            int resourceId = Integer.parseInt(imageUrl);
            Glide.with(holder.itemView.getContext())
                    .load(resourceId)
                    .placeholder(R.drawable.ic_profile_placeholder)
                    .error(R.drawable.ic_error)
                    .into(holder.postImage);
        } catch (NumberFormatException e) {
            // Datei-Pfad verwenden
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_profile_placeholder)
                    .error(R.drawable.ic_error)
                    .into(holder.postImage);
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView description;
        ImageView postImage;
        TextView ingredients;
        TextView recipe;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.dialog_post_image);
            description = itemView.findViewById(R.id.dialog_post_description);
            ingredients = itemView.findViewById(R.id.dialog_post_ingredients);
            recipe = itemView.findViewById(R.id.dialog_post_recipe);
        }
    }
}
