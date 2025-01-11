package com.example.socialfood.gui.adapters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.socialfood.controller.User.UserController;
import com.example.socialfood.model.entities.Comment;
import com.example.socialfood.model.entities.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for CommentsAdapter. Tests adapter functionality with mock data.
 */
@RunWith(AndroidJUnit4.class)
public class CommentsAdapterTest {

    private com.example.socialfood.gui.adapters.CommentsAdapter adapter;
    private List<Comment> testComments;
    private UserController mockUserController;
    private Context context;

    /**
     * Sets up test environment before each test.
     * Creates mock data and initializes adapter.
     */
    @Before
    public void setup() {
        context = ApplicationProvider.getApplicationContext();
        mockUserController = mock(UserController.class);

        testComments = new ArrayList<>();
        testComments.add(createTestComment(1, 1, 1));
        testComments.add(createTestComment(2, 1, 2));

        User testUser = new User();
        testUser.setUsername("TestUser");
        when(mockUserController.getUserById(1)).thenReturn(testUser);
        when(mockUserController.getUserById(2)).thenReturn(testUser);

        adapter = new com.example.socialfood.gui.adapters.CommentsAdapter(testComments, mockUserController);
    }

    /**
     * Tests if adapter returns correct item count
     */
    @Test
    public void testGetItemCount() {
        assertEquals(2, adapter.getItemCount());
    }

    /**
     * Tests updating comments list
     */
    @Test
    public void testUpdateComments() {
        List<Comment> newComments = new ArrayList<>();
        newComments.add(createTestComment(3, 1, 3));

        adapter.updateComments(newComments);

        assertEquals(1, adapter.getItemCount());
    }

    /**
     * Tests comment view holder creation
     */
    @Test
    public void testCreateViewHolder() {
        View itemView = LayoutInflater.from(context)
                .inflate(com.example.socialfood.R.layout.item_comment, new LinearLayout(context), false);

        CommentsAdapter.CommentViewHolder holder = adapter.onCreateViewHolder(new LinearLayout(context), 0);

        assertNotNull(holder);
        assertNotNull(holder.username);
        assertNotNull(holder.commentText);
    }

    /**
     * Helper method to create test comments
     */
    private Comment createTestComment(int uid, int postId, int commentId) {
        Comment comment = new Comment();
        comment.setUid(uid);
        comment.setPostId(postId);
        comment.setCommentId(commentId);
        comment.setContent("Test Content " + commentId);
        comment.setTimestamp(System.currentTimeMillis());
        return comment;
    }
}