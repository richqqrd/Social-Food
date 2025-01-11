package com.example.socialfood.controller.Post;

import android.content.Context;

import com.example.socialfood.controller.BaseController;
import com.example.socialfood.controller.User.UserController;
import com.example.socialfood.model.handler.CommentHandler;
import com.example.socialfood.model.handler.LikeHandler;
import com.example.socialfood.model.handler.PostHandler;
import com.example.socialfood.model.entities.Comment;
import com.example.socialfood.model.entities.Post;
import com.example.socialfood.model.entities.User;

import java.util.List;

/**
 * Controller class for handling post-related operations. Implements PostControllerInterface and
 * extends BaseController. Manages creation, retrieval, updates and interactions with posts.
 */
public class PostController extends BaseController implements com.example.socialfood.controller.Post.PostControllerInterface {
    private final PostHandler postHandler;
    private final LikeHandler likeHandler;
    private final CommentHandler commentHandler;
    private final UserController userController;

    /**
     * Constructs a new PostController
     *
     * @param context Application context
     * @param postHandler Handler for post operations, creates new if null
     * @param likeHandler Handler for like operations, creates new if null
     * @param commentHandler Handler for comment operations, creates new if null
     * @param userController Controller for user operations
     */
    public PostController(Context context, PostHandler postHandler, LikeHandler likeHandler,
            CommentHandler commentHandler, UserController userController) {
        super(context);
        this.postHandler = postHandler != null ? postHandler : new PostHandler(context);
        this.likeHandler = likeHandler != null ? likeHandler : new LikeHandler(context);
        this.commentHandler = commentHandler != null ? commentHandler : new CommentHandler(context);
        this.userController = userController;
    }

    @Override
    public boolean createPost(String photoPath, String description, String recipe,
            String ingredients, double latitude, double longitude) {
        Post post = new Post();
        post.setImageUrl(photoPath);
        post.setDescription(description);
        post.setUid(getCurrentUserId());
        post.setPostId(generateNextPostId());
        post.setTimestamp(System.currentTimeMillis());
        post.setLatitude(latitude);
        post.setLongitude(longitude);
        post.setIngredients(ingredients);
        post.setRecipe(recipe);

        boolean success = postHandler.insert(post);
        if (success) {
            // Update user's post count
            User currentUser = getCurrentUser();
            currentUser.setPostsCount(currentUser.getPostsCount() + 1);
            userController.updateUser(currentUser);
        }
        return success;
    }

    @Override
    public Post getPost(int postId) {
        return postHandler.getPostById(getCurrentUserId(), postId);
    }

    @Override
    public List<Post> getPostsFromUser(int uid) {
        return postHandler.getPostByUser(uid);
    }

    @Override
    public int getLikeCount(int postId) {
        return likeHandler.getLikeCount(postId);
    }

    @Override
    public boolean updatePost(Post post) {
        return postHandler.update(post);
    }

    @Override
    public boolean deletePost(Post post) {
        return postHandler.delete(post);
    }

    @Override
    public boolean isPostLikedByUser(int postId) {
        Post post = getPostById(getCurrentUserId(), postId);
        return post != null && postHandler.isLikedByUser(post, getCurrentUser());
    }

    @Override
    public Post getPostById(int uid, int postId) {
        return postHandler.getPostById(uid, postId);
    }

    @Override
    public void onLikePost(int postId) {
        Post post = postHandler.getPostById(getCurrentUser().getUid(), postId);
        if (post != null) {
            likeHandler.toggleLike(getCurrentUserId(), postId);
        }
    }

    @Override
    public void onCommentPost(int postId, String commentText) {
        Post post = postHandler.getPostById(getCurrentUser().getUid(), postId);
        if (post != null) {
            Comment comment = new Comment();
            comment.setUid(getCurrentUser().getUid());
            comment.setPostId(postId);
            long timestamp = System.currentTimeMillis();
            int commentId = Math.abs((int) (timestamp % Integer.MAX_VALUE)) + 1;
            comment.setCommentId(commentId);

            comment.setContent(commentText);
            comment.setTimestamp(System.currentTimeMillis());

            if (commentHandler.insert(comment)) {
                post.setCommentCount(post.getCommentCount() + 1);
                postHandler.update(post);
            }
        }
    }

    @Override
    public List<Comment> getCommentsForPost(int postId) {
        return commentHandler.getCommentsByPostId(postId);
    }

    @Override
    public List<Post> getAllPosts() {
        return postHandler.getAllPosts();
    }

    /**
     * Generates the next available post ID for the current user
     * 
     * @return A new unique post ID calculated as maximum existing ID + 1
     */
    private int generateNextPostId() {
        List<Post> userPosts = postHandler.getPostByUser(getCurrentUserId());
        return userPosts.stream()
                .mapToInt(Post::getPostId)
                .max()
                .orElse(0) + 1;
    }
}
