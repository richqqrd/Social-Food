package com.example.socialfood.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.util.Objects;

/**
 * Entity class representing a follow relationship between users. A follow relationship is uniquely
 * identified by the combination of followerId and followedId.
 *
 * <p>
 * This entity has foreign key relationships to:
 * <ul>
 * <li>User entity through followerId (with cascade delete)</li>
 * <li>User entity through followedId (with cascade delete)</li>
 * </ul>
 * </p>
 *
 * <p>
 * Primary key is composite of:
 * <ul>
 * <li>followerId - ID of the user who is following</li>
 * <li>followedId - ID of the user being followed</li>
 * </ul>
 * </p>
 *
 * @see com.example.socialfood.model.entities.User
 */
@Entity(tableName = "follows", foreignKeys = {
        @ForeignKey(entity = User.class, parentColumns = "uid", childColumns = "followerId", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = User.class, parentColumns = "uid", childColumns = "followedId", onDelete = ForeignKey.CASCADE)
}, primaryKeys = { "followerId", "followedId" })
public class Follow {

    /** The ID of the user who is following */
    private int followerId;

    /** The ID of the user being followed */
    private int followedId;

    @NonNull
    /** Timestamp when the follow relationship was created */
    private long timestamp;

    /**
     * Gets the ID of the follower
     * 
     * @return The follower's user ID
     */
    public int getFollowerId() {
        return followerId;
    }

    /**
     * Sets the ID of the follower
     * 
     * @param followerId The follower's user ID
     * @throws IllegalArgumentException if followerId is not positive
     */
    public void setFollowerId(int followerId) {
        if (followerId <= 0) {
            throw new IllegalArgumentException("followerId must be positive");
        }
        this.followerId = followerId;
    }

    /**
     * Gets the ID of the user being followed
     * 
     * @return The followed user's ID
     */
    public int getFollowedId() {
        return followedId;
    }

    /**
     * Sets the ID of the user being followed
     * 
     * @param followedId The followed user's ID
     * @throws IllegalArgumentException if followedId is not positive
     */
    public void setFollowedId(int followedId) {
        if (followedId <= 0) {
            throw new IllegalArgumentException("followedId must be positive");
        }
        this.followedId = followedId;
    }

    /**
     * Gets the timestamp when the follow relationship was created
     * 
     * @return The timestamp in milliseconds since epoch
     */
    @NonNull
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp when the follow relationship was created
     * 
     * @param timestamp The timestamp in milliseconds since epoch
     * @throws IllegalArgumentException if timestamp is not positive
     */
    public void setTimestamp(@NonNull long timestamp) {
        if (timestamp <= 0) {
            throw new IllegalArgumentException("timestamp must be positive");
        }
        this.timestamp = timestamp;
    }

    /**
     * Returns a string representation of the Follow object
     * 
     * @return A string containing the follower ID, followed ID, and timestamp
     */
    @Override
    public String toString() {
        return "Follow{" +
                "followerId=" + followerId +
                ", followedId=" + followedId +
                ", timestamp=" + timestamp +
                '}';
    }

    /**
     * Checks if this Follow object equals another object
     * 
     * @param o The object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Follow follow = (Follow) o;
        return followerId == follow.followerId &&
                followedId == follow.followedId &&
                timestamp == follow.timestamp;
    }

    /**
     * Generates a hash code for this Follow object
     * 
     * @return A hash code based on followerId, followedId, and timestamp
     */
    @Override
    public int hashCode() {
        return Objects.hash(followerId, followedId, timestamp);
    }
}
