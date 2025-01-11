package com.example.socialfood.controller.Post;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.example.socialfood.controller.User.UserController;
import com.example.socialfood.model.entities.User;
import com.example.socialfood.model.handler.CommentHandler;
import com.example.socialfood.model.handler.LikeHandler;
import com.example.socialfood.model.handler.PostHandler;
import com.example.socialfood.model.entities.Comment;
import com.example.socialfood.model.entities.Post;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * test class for {@link com.example.socialfood.controller.Post.PostController}.
 */
public class PostControllerTest {

    private com.example.socialfood.controller.Post.PostController postController;
    private PostHandler mockPostHandler;
    private LikeHandler mockLikeHandler;
    private CommentHandler mockCommentHandler;
    private UserController mockUserController;

    /**
     * Sets up the test environment before each test.
     * <p>
     * Mocks the {@link PostHandler}, {@link LikeHandler}, and {@link CommentHandler}.
     * Initializes a spy of {@link com.example.socialfood.controller.Post.PostController} to allow partial stubbing of its methods.
     */
    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();

        mockPostHandler = mock(PostHandler.class);
        mockLikeHandler = mock(LikeHandler.class);
        mockCommentHandler = mock(CommentHandler.class);
        mockUserController = mock(UserController.class);

        postController = spy(new com.example.socialfood.controller.Post.PostController(context, mockPostHandler, mockLikeHandler, mockCommentHandler, mockUserController ));
        doReturn(1).when(postController).getCurrentUserId();
        User mockUser = new User();
        mockUser.setUid(1);
        doReturn(mockUser).when(postController).getCurrentUser();
    }

    /**
     * Tests the {@link com.example.socialfood.controller.Post.PostController#createPost(String, String, String, String, double, double)} method.
     * <p>
     * Verifies that a new post is created and stored using {@link PostHandler#insert(Post)}.
     */
    @Test
    public void testCreatePost() {
        String photoPath = "/path/to/photo.jpg";
        String description = "Delicious meal!";
        String recipe = "Recipe text";
        String ingredients = "Ingredients text";
        double latitude = 12.34;
        double longitude = 56.78;

        when(mockPostHandler.insert(any(Post.class))).thenReturn(true);

        boolean result = postController.createPost(photoPath, description, recipe, ingredients, latitude, longitude);
        assertTrue(result);

        verify(mockPostHandler).insert(any(Post.class));
    }

    /**
     * Tests the {@link com.example.socialfood.controller.Post.PostController#getPost(int)} method.
     * <p>
     * Verifies that the correct post is retrieved using {@link PostHandler#getPostById(int, int)}.
     */
    @Test
    public void testGetPost() {
        int postId = 123;
        Post mockPost = new Post();
        when(mockPostHandler.getPostById(1, postId)).thenReturn(mockPost);

        Post result = postController.getPost(postId);
        assertEquals(mockPost, result);

        verify(mockPostHandler).getPostById(1, postId);
    }

    /**
     * Tests the {@link com.example.socialfood.controller.Post.PostController#getPostsFromUser(int)} method.
     * <p>
     * Verifies that all posts from a specific user are retrieved.
     */
    @Test
    public void testGetPostsFromUser() {
        int uid = 1;
        List<Post> mockPosts = new ArrayList<>();
        when(mockPostHandler.getPostByUser(uid)).thenReturn(mockPosts);

        List<Post> result = postController.getPostsFromUser(uid);
        assertEquals(mockPosts, result);

        verify(mockPostHandler).getPostByUser(uid);
    }

    /**
     * Tests the {@link com.example.socialfood.controller.Post.PostController#getLikeCount(int)} method.
     * <p>
     * Verifies that the correct like count for a post is retrieved.
     */
    @Test
    public void testGetLikeCount() {
        int postId = 123;
        when(mockLikeHandler.getLikeCount(postId)).thenReturn(10);

        int result = postController.getLikeCount(postId);
        assertEquals(10, result);

        verify(mockLikeHandler).getLikeCount(postId);
    }

    /**
     * Tests the {@link com.example.socialfood.controller.Post.PostController#updatePost(Post)} method.
     * <p>
     * Verifies that a post is updated using {@link PostHandler#update(Post)}.
     */
    @Test
    public void testUpdatePost() {
        Post mockPost = new Post();
        when(mockPostHandler.update(mockPost)).thenReturn(true);

        boolean result = postController.updatePost(mockPost);
        assertTrue(result);

        verify(mockPostHandler).update(mockPost);
    }

    /**
     * Tests the {@link com.example.socialfood.controller.Post.PostController#deletePost(Post)} method.
     * <p>
     * Verifies that a post is deleted using {@link PostHandler#delete(Post)}.
     */
    @Test
    public void testDeletePost() {
        Post mockPost = new Post();
        when(mockPostHandler.delete(mockPost)).thenReturn(true);

        boolean result = postController.deletePost(mockPost);
        assertTrue(result);

        verify(mockPostHandler).delete(mockPost);
    }

    /**
     * Tests the {@link com.example.socialfood.controller.Post.PostController#isPostLikedByUser(int)} method.
     * <p>
     * Verifies that the correct like status for a post is determined.
     */
    @Test
    public void testIsPostLikedByUser() {
        int postId = 123;

        Post mockPost = new Post();
        User mockUser = new User();
        mockUser.setUid(1);

        when(mockPostHandler.getPostById(1, postId)).thenReturn(mockPost);
        doReturn(mockUser).when(postController).getCurrentUser();
        when(mockPostHandler.isLikedByUser(mockPost, mockUser)).thenReturn(true);

        boolean result = postController.isPostLikedByUser(postId);

        assertTrue(result);

        verify(mockPostHandler).getPostById(1, postId);
        verify(mockPostHandler).isLikedByUser(mockPost, mockUser);
    }

    /**
     * Tests the {@link com.example.socialfood.controller.Post.PostController#onLikePost(int)} method.
     * <p>
     * Verifies that a post like is toggled using {@link LikeHandler#toggleLike(int, int)}.
     */
    @Test
    public void testOnLikePost() {
        int postId = 123;
        Post mockPost = new Post();
        when(mockPostHandler.getPostById(1, postId)).thenReturn(mockPost);

        postController.onLikePost(postId);

        verify(mockLikeHandler).toggleLike(1, postId);
    }

    /**
     * Tests the {@link com.example.socialfood.controller.Post.PostController#onCommentPost(int, String)} method.
     * <p>
     * Verifies that a comment is added to a post and the post is updated.
     */
    @Test
    public void testOnCommentPost() {
        int postId = 123;
        String commentText = "Great post!";

        Post mockPost = new Post();
        mockPost.setUid(1);
        mockPost.setPostId(postId);
        mockPost.setCommentCount(0);

        when(mockPostHandler.getPostById(1, postId)).thenReturn(mockPost);
        when(mockCommentHandler.insert(any(Comment.class))).thenReturn(true);

        postController.onCommentPost(postId, commentText);

        verify(mockCommentHandler).insert(any(Comment.class));

        mockPost.setCommentCount(1);
        verify(mockPostHandler).update(mockPost);
    }

    /**
     * Tests the {@link com.example.socialfood.controller.Post.PostController#getCommentsForPost(int)} method.
     * <p>
     * Verifies that all comments for a post are retrieved.
     */
    @Test
    public void testGetCommentsForPost() {
        int postId = 123;
        List<Comment> mockComments = new ArrayList<>();
        when(mockCommentHandler.getCommentsByPostId(postId)).thenReturn(mockComments);

        List<Comment> result = postController.getCommentsForPost(postId);
        assertEquals(mockComments, result);

        verify(mockCommentHandler).getCommentsByPostId(postId);
    }


    /**
     * Tests the {@link com.example.socialfood.controller.Post.PostController#getAllPosts()} method.
     * <p>
     * Verifies that all posts are retrieved using {@link PostHandler#getAllPosts()}.
     */
    @Test
    public void testGetAllPosts() {
        List<Post> mockPosts = new ArrayList<>();
        when(mockPostHandler.getAllPosts()).thenReturn(mockPosts);

        List<Post> result = postController.getAllPosts();
        assertEquals(mockPosts, result);

        verify(mockPostHandler).getAllPosts();
    }
}
