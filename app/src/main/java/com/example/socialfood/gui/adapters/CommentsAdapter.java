package com.example.socialfood.gui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialfood.R;
import com.example.socialfood.controller.User.UserController;
import com.example.socialfood.model.entities.Comment;
import com.example.socialfood.model.entities.User;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {
    private List<Comment> comments;
    private UserController userController;

    public CommentsAdapter(List<Comment> comments, UserController userController) {
        this.comments = comments;
        this.userController = userController;
    }

    @NonNull @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        User user = userController.getUserById(comment.getUid());

        holder.username.setText(user.getUsername());
        holder.commentText.setText(comment.getContent());
    }

    public void updateComments(List<Comment> newComments) {
        this.comments = new ArrayList<>(newComments);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView commentText;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.comment_username);
            commentText = itemView.findViewById(R.id.comment_text);
        }
    }
}