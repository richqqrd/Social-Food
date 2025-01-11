package com.example.socialfood.model.entities;

import java.util.Objects;

import androidx.room.Entity;
import androidx.room.ForeignKey;

/**
 * Entity class representing a like relationship between a user and a post. A like is uniquely
 * identified by the combination of userId and postId.
 *
 * <p>
 * This entity has foreign key relationships to:
 * <ul>
 * <li>User entity through userId (with cascade delete)</li>
 * <li>Post entity through uid and postId (with cascade delete)</li>
 * </ul>
 * </p>
 *
 * <p>
 * Primary key is composite of:
 * <ul>
 * <li>userId - ID of the user who created the like</li>
 * <li>postId - ID of the post being liked</li>
 * </ul>
 * </p>
 *
 * @see com.example.socialfood.model.entities.User
 * @see com.example.socialfood.model.entities.Post
 */
@Entity(tableName = "like_table", foreignKeys = {
        @ForeignKey(entity = User.class, parentColumns = "uid", childColumns = "userId", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Post.class, parentColumns = { "uid", "postId" }, childColumns = {
                "userId", "postId" }, onDelete = ForeignKey.CASCADE)
}, primaryKeys = { "userId", "postId" })
public class Like {

    /** The ID of the user who created the like */
    private int userId;

    /** The ID of the post being liked */
    private int postId;

    /** Timestamp when the like was created */
    private long timestamp;

    /**
     * Gets the user ID of the like creator
     * 
     * @return The user ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the user ID of the like creator
     * 
     * @param userId The user ID to set
     * @throws IllegalArgumentException if userId is not positive
     */
    public void setUserId(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("userId must be positive");
        }
        this.userId = userId;
    }

    /**
     * Gets the ID of the post being liked
     * 
     * @return The post ID
     */
    public int getPostId() {
        return postId;
    }

    /**
     * Sets the ID of the post being liked
     * 
     * @param postId The post ID to set
     * @throws IllegalArgumentException if postId is not positive
     */
    public void setPostId(int postId) {
        if (postId <= 0) {
            throw new IllegalArgumentException("postId must be positive");
        }
        this.postId = postId;
    }

    /**
     * Gets the timestamp when the like was created
     * 
     * @return The timestamp in milliseconds
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp when the like was created
     * 
     * @param timestamp The timestamp in milliseconds
     * @throws IllegalArgumentException if timestamp is not positive
     */
    public void setTimestamp(long timestamp) {
        if (timestamp <= 0) {
            throw new IllegalArgumentException("timestamp must be positive");
        }
        this.timestamp = timestamp;
    }

    /**
     * Returns a string representation of this Like object
     * 
     * @return A string containing the user ID, post ID and timestamp
     */
    @Override
    public String toString() {
        return "Like{" +
                "userId=" + userId +
                ", postId=" + postId +
                ", timestamp=" + timestamp +
                '}';
    }

    /**
     * Compares this like with another object for equality. Two likes are considered equal if they
     * have the same userId, postId and timestamp.
     *
     * @param o The object to compare this like against
     * @return true if the given object represents a Like equivalent to this like
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Like like = (Like) o;
        return userId == like.userId &&
                postId == like.postId &&
                timestamp == like.timestamp;
    }

    /**
     * Returns a hash code value for this like. This implementation consistently returns the same
     * hash code for Like objects that are equal according to the {@link #equals(Object)} method.
     *
     * @return A hash code value for this like
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId, postId, timestamp);
    }
}