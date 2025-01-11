package com.example.socialfood.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.io.Serializable;
import java.util.Objects;

/**
 * Entity class representing a post in the social food application. A post is uniquely identified by
 * the combination of uid (user id) and postId.
 *
 * <p>
 * This entity has foreign key relationships to:
 * <ul>
 * <li>User entity through uid (with cascade delete)</li>
 * </ul>
 * </p>
 *
 * <p>
 * Primary key is composite of:
 * <ul>
 * <li>uid - ID of the post creator</li>
 * <li>postId - unique identifier within user's posts</li>
 * </ul>
 * </p>
 *
 * <p>
 * Required fields (NonNull):
 * <ul>
 * <li>imageUrl - URL/path to post image</li>
 * <li>timestamp - creation time</li>
 * <li>commentCount - number of comments</li>
 * <li>latitude - location coordinate</li>
 * <li>longitude - location coordinate</li>
 * </ul>
 * </p>
 *
 * @see com.example.socialfood.model.entities.User
 * @see Comment
 * @see Like
 */
@Entity(tableName = "post", foreignKeys = @ForeignKey(entity = User.class, parentColumns = "uid", childColumns = "uid", onDelete = ForeignKey.CASCADE), primaryKeys = {
        "uid", "postId" })
public class Post implements Serializable {

    /** The ID of the user who created the post */
    private int uid;

    /** The unique identifier for this post */
    private int postId;

    /** The URL or path to the post's image */
    @NonNull
    private String imageUrl;

    /** The text description of the post */
    private String description;

    /** Timestamp when the post was created */
    @NonNull
    private long timestamp;

    /** Number of comments on this post */
    @NonNull
    private int commentCount;

    /** The recipe instructions for this food post */
    private String recipe;

    /** The ingredients list for this food post */
    private String ingredients;

    /** The latitude coordinate where the post was created */
    @NonNull
    private double latitude;

    /** The longitude coordinate where the post was created */
    @NonNull
    private double longitude;

    /**
     * Gets the user ID of the post creator
     * 
     * @return The user ID
     */
    public int getUid() {
        return uid;
    }

    /**
     * Sets the user ID of the post creator
     * 
     * @param uid The user ID to set
     * @throws IllegalArgumentException if uid is not positive
     */
    public void setUid(int uid) {
        if (uid <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        this.uid = uid;
    }

    /**
     * Gets the unique identifier of this post
     * 
     * @return The post ID
     */
    public int getPostId() {
        return postId;
    }

    /**
     * Sets the unique identifier of this post
     * 
     * @param postId The post ID to set
     * @throws IllegalArgumentException if postId is not positive
     */
    public void setPostId(int postId) {
        if (postId <= 0) {
            throw new IllegalArgumentException("Post ID must be positive");
        }
        this.postId = postId;
    }

    /**
     * Gets the URL or path to the post's image
     * 
     * @return The image URL/path
     */
    @NonNull
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets the URL or path to the post's image
     * 
     * @param imageUrl The image URL/path to set
     * @throws IllegalArgumentException if imageUrl is empty or only whitespace
     */
    public void setImageUrl(@NonNull String imageUrl) {
        if (imageUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Image URL cannot be empty");
        }
        this.imageUrl = imageUrl;
    }

    /**
     * Gets the text description of the post
     * 
     * @return The post description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the text description of the post
     * 
     * @param description The description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the timestamp when the post was created
     * 
     * @return The timestamp in milliseconds
     */
    @NonNull
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp when the post was created
     * 
     * @param timestamp The timestamp in milliseconds
     * @throws IllegalArgumentException if timestamp is not positive
     */
    public void setTimestamp(@NonNull long timestamp) {
        if (timestamp <= 0) {
            throw new IllegalArgumentException("Timestamp must be positive");
        }
        this.timestamp = timestamp;
    }

    /**
     * Gets the number of comments on this post
     * 
     * @return The comment count
     */
    @NonNull
    public int getCommentCount() {
        return commentCount;
    }

    /**
     * Sets the number of comments on this post
     * 
     * @param commentCount The comment count to set
     * @throws IllegalArgumentException if commentCount is negative
     */
    public void setCommentCount(@NonNull int commentCount) {
        if (commentCount < 0) {
            throw new IllegalArgumentException("Comment count cannot be negative");
        }
        this.commentCount = commentCount;
    }

    /**
     * Gets the recipe instructions
     * 
     * @return The recipe text
     */
    public String getRecipe() {
        return recipe;
    }

    /**
     * Sets the recipe instructions
     * 
     * @param recipe The recipe text to set
     */
    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    /**
     * Gets the ingredients list
     * 
     * @return The ingredients text
     */
    public String getIngredients() {
        return ingredients;
    }

    /**
     * Sets the ingredients list
     * 
     * @param ingredients The ingredients text to set
     */
    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Gets the latitude coordinate
     * 
     * @return The latitude value
     */
    @NonNull
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude coordinate
     * 
     * @param latitude The latitude value to set
     * @throws IllegalArgumentException if latitude is not between -90 and 90
     */
    public void setLatitude(@NonNull double latitude) {
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        this.latitude = latitude;
    }

    /**
     * Gets the longitude coordinate
     * 
     * @return The longitude value
     */
    @NonNull
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude coordinate
     * 
     * @param longitude The longitude value to set
     * @throws IllegalArgumentException if longitude is not between -180 and 180
     */
    public void setLongitude(@NonNull double longitude) {
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }
        this.longitude = longitude;
    }

    /**
     * Returns a string representation of this Post object
     * 
     * @return A string containing all the post's fields
     */
    @Override
    public String toString() {
        return "Post{" +
                "uid=" + uid +
                ", postId=" + postId +
                ", imageUrl='" + imageUrl + '\'' +
                ", description='" + description + '\'' +
                ", timestamp=" + timestamp +
                ", commentCount=" + commentCount +
                ", recipe='" + recipe + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    /**
     * Compares this post with another object for equality. Two posts are considered equal if they
     * have the same uid, postId, timestamp, commentCount, coordinates and content fields.
     *
     * @param o The object to compare this post against
     * @return true if the given object represents a Post equivalent to this post
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Post post = (Post) o;
        return uid == post.uid &&
                postId == post.postId &&
                timestamp == post.timestamp &&
                commentCount == post.commentCount &&
                Double.compare(post.latitude, latitude) == 0 &&
                Double.compare(post.longitude, longitude) == 0 &&
                Objects.equals(imageUrl, post.imageUrl) &&
                Objects.equals(description, post.description) &&
                Objects.equals(recipe, post.recipe) &&
                Objects.equals(ingredients, post.ingredients);
    }

    /**
     * Returns a hash code value for this post. This implementation consistently returns the same
     * hash code for Post objects that are equal according to the {@link #equals(Object)} method.
     *
     * @return A hash code value for this post
     */
    @Override
    public int hashCode() {
        return Objects.hash(uid, postId, imageUrl, description, timestamp,
                commentCount, recipe, ingredients, latitude, longitude);
    }

}
