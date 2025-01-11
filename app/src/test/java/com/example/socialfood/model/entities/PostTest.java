package com.example.socialfood.model.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Post entity. Tests all getters, setters and validation logic.
 */
public class PostTest {
    private Post post;

    /**
     * Sets up a Post instance before each test.
     */
    @Before
    public void setup() {
        post = new Post();
    }

    /**
     * Tests setting and getting the user ID.
     */
    @Test
    public void testUid() {
        post.setUid(1);
        assertEquals(1, post.getUid());
    }

    /**
     * Tests validation of negative user ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidUid() {
        post.setUid(-1);
    }

    /**
     * Tests setting and getting the post ID.
     */
    @Test
    public void testPostId() {
        post.setPostId(1);
        assertEquals(1, post.getPostId());
    }

    /**
     * Tests validation of negative post ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPostId() {
        post.setPostId(-1);
    }

    /**
     * Tests setting and getting the image URL.
     */
    @Test
    public void testImageUrl() {
        post.setImageUrl("test.jpg");
        assertEquals("test.jpg", post.getImageUrl());
    }

    /**
     * Tests validation of empty image URL.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyImageUrl() {
        post.setImageUrl("");
    }

    /**
     * Tests validation of whitespace-only image URL.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWhitespaceImageUrl() {
        post.setImageUrl("   ");
    }

    /**
     * Tests setting and getting the description.
     */
    @Test
    public void testDescription() {
        post.setDescription("Test description");
        assertEquals("Test description", post.getDescription());
    }

    /**
     * Tests setting and getting the timestamp.
     */
    @Test
    public void testTimestamp() {
        long timestamp = System.currentTimeMillis();
        post.setTimestamp(timestamp);
        assertEquals(timestamp, post.getTimestamp());
    }

    /**
     * Tests validation of negative timestamp.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTimestamp() {
        post.setTimestamp(-1);
    }

    /**
     * Tests setting and getting the comment count.
     */
    @Test
    public void testCommentCount() {
        post.setCommentCount(5);
        assertEquals(5, post.getCommentCount());
    }

    /**
     * Tests validation of negative comment count.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCommentCount() {
        post.setCommentCount(-1);
    }

    /**
     * Tests setting and getting the recipe.
     */
    @Test
    public void testRecipe() {
        post.setRecipe("Test recipe");
        assertEquals("Test recipe", post.getRecipe());
    }

    /**
     * Tests setting and getting the ingredients.
     */
    @Test
    public void testIngredients() {
        post.setIngredients("Test ingredients");
        assertEquals("Test ingredients", post.getIngredients());
    }

    /**
     * Tests setting and getting the latitude.
     */
    @Test
    public void testLatitude() {
        post.setLatitude(45.0);
        assertEquals(45.0, post.getLatitude(), 0.001);
    }

    /**
     * Tests validation of invalid latitude (too low).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLatitudeLow() {
        post.setLatitude(-91.0);
    }

    /**
     * Tests validation of invalid latitude (too high).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLatitudeHigh() {
        post.setLatitude(91.0);
    }

    /**
     * Tests setting and getting the longitude.
     */
    @Test
    public void testLongitude() {
        post.setLongitude(45.0);
        assertEquals(45.0, post.getLongitude(), 0.001);
    }

    /**
     * Tests validation of invalid longitude (too low).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLongitudeLow() {
        post.setLongitude(-181.0);
    }

    /**
     * Tests validation of invalid longitude (too high).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLongitudeHigh() {
        post.setLongitude(181.0);
    }

    /**
     * Tests equals method with same values.
     */
    @Test
    public void testEqualsWithSameValues() {
        Post post1 = createTestPost(1, 1);
        Post post2 = createTestPost(1, 1);
        assertTrue(post1.equals(post2));
    }

    /**
     * Tests equals method with different values.
     */
    @Test
    public void testEqualsWithDifferentValues() {
        Post post1 = createTestPost(1, 1);
        Post post2 = createTestPost(2, 2);
        assertFalse(post1.equals(post2));
    }

    /**
     * Tests hashCode generation.
     */
    @Test
    public void testHashCode() {
        Post post1 = createTestPost(1, 1);
        Post post2 = createTestPost(1, 1);
        assertEquals(post1.hashCode(), post2.hashCode());
    }

    /**
     * Tests toString method.
     */
    @Test
    public void testToString() {
        Post post = createTestPost(1, 1);
        String expected = "Post{uid=1, postId=1, imageUrl='default.png', description='Test post', "
                +
                "timestamp=" + post.getTimestamp() + ", commentCount=0, recipe='Test recipe', " +
                "ingredients='Test ingredients', latitude=0.0, longitude=0.0}";
        assertEquals(expected, post.toString());
    }

    /**
     * Helper method to create a test post with specified parameters.
     *
     * @param uid The user ID for the post
     * @param postId The unique identifier for the post
     * @return A Post entity initialized with test data
     */
    private Post createTestPost(int uid, int postId) {
        Post post = new Post();
        post.setUid(uid);
        post.setPostId(postId);
        post.setImageUrl("default.png");
        post.setDescription("Test post");
        post.setTimestamp(System.currentTimeMillis());
        post.setCommentCount(0);
        post.setRecipe("Test recipe");
        post.setIngredients("Test ingredients");
        post.setLatitude(0.0);
        post.setLongitude(0.0);
        return post;
    }
}