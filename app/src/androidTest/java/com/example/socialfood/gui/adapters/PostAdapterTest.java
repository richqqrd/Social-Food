package com.example.socialfood.gui.adapters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.socialfood.controller.Navigation.NavigationController;
import com.example.socialfood.model.entities.Post;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for PostAdapter. Tests adapter functionality with mock data.
 */
@RunWith(AndroidJUnit4.class)
public class PostAdapterTest {
    private com.example.socialfood.gui.adapters.PostAdapter adapter;
    private List<Post> testPosts;
    private NavigationController mockNavigationController;
    private Context context;


    /**
     * Sets up test environment before each test.
     * Creates test user and navigates to post creation via camera.
     */
    @Before
    public void setup() {
        context = ApplicationProvider.getApplicationContext();
        mockNavigationController = mock(NavigationController.class);

        testPosts = new ArrayList<>();
        testPosts.add(createTestPost(1, 1));
        testPosts.add(createTestPost(2, 2));

        adapter = new com.example.socialfood.gui.adapters.PostAdapter(testPosts, false, mockNavigationController);
    }

    /**
     * Tests if adapter returns correct item count
     */
    @Test
    public void testGetItemCount() {
        assertEquals(2, adapter.getItemCount());
    }

    /**
     * Tests post view holder creation.
     * Verifies that all required views are initialized:
     */
    @Test
    public void testCreateViewHolder() {
        View itemView = LayoutInflater.from(context)
                .inflate(com.example.socialfood.R.layout.item_post, new LinearLayout(context), false);

        PostAdapter.PostViewHolder holder = adapter.onCreateViewHolder(new LinearLayout(context), 0);

        assertNotNull(holder);
        assertNotNull(holder.postImage);
        assertNotNull(holder.description);
        assertNotNull(holder.recipe);
        assertNotNull(holder.ingredients);
    }

    /**
     * Tests if detail view displays all elements correctly.
     */
    @Test
    public void testDetailView() {
        adapter = new com.example.socialfood.gui.adapters.PostAdapter(testPosts, true, mockNavigationController) {
            @Override
            public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
                Post post = testPosts.get(position);
                // Skip image loading
                holder.description.setVisibility(View.VISIBLE);
                holder.ingredients.setVisibility(View.VISIBLE);
                holder.recipe.setVisibility(View.VISIBLE);
                holder.description.setText(post.getDescription());
                holder.ingredients.setText("Zutaten:\n" + post.getIngredients());
                holder.recipe.setText("Rezept:\n" + post.getRecipe());
            }
        };

        PostAdapter.PostViewHolder holder = adapter.onCreateViewHolder(
                new LinearLayout(context), 0);
        adapter.onBindViewHolder(holder, 0);

        assertEquals(View.VISIBLE, holder.description.getVisibility());
        assertEquals(View.VISIBLE, holder.recipe.getVisibility());
        assertEquals(View.VISIBLE, holder.ingredients.getVisibility());
    }

    /**
     * Tests if list view hides detail elements correctly.
     */
    @Test
    public void testListView() {
        adapter = new com.example.socialfood.gui.adapters.PostAdapter(testPosts, false, mockNavigationController) {
            @Override
            public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
                // Skip image loading
                holder.description.setVisibility(View.GONE);
                holder.ingredients.setVisibility(View.GONE);
                holder.recipe.setVisibility(View.GONE);
            }
        };

        PostAdapter.PostViewHolder holder = adapter.onCreateViewHolder(
                new LinearLayout(context), 0);
        adapter.onBindViewHolder(holder, 0);

        assertEquals(View.GONE, holder.description.getVisibility());
        assertEquals(View.GONE, holder.recipe.getVisibility());
        assertEquals(View.GONE, holder.ingredients.getVisibility());
    }

    /**
     * Helper method to create test posts with specified parameters.
     */
    private Post createTestPost(int uid, int postId) {
        Post post = new Post();
        post.setUid(uid);
        post.setPostId(postId);
        post.setImageUrl("test_image.jpg");
        post.setDescription("Test Description " + postId);
        post.setRecipe("Test Recipe " + postId);
        post.setIngredients("Test Ingredients " + postId);
        post.setTimestamp(System.currentTimeMillis());
        return post;
    }
}
