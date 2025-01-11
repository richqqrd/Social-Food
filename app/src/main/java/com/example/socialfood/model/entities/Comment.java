package com.example.socialfood.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.util.Objects;

/**
 * Entity class representing a comment in the social food application. A comment is uniquely
 * identified by the combination of uid (user id), postId, and commentId.
 *
 * <p>
 * This entity has foreign key relationships to:
 * <ul>
 * <li>User entity through uid (with cascade delete)</li>
 * <li>Post entity through uid and postId (with cascade delete)</li>
 * </ul>
 * </p>
 *
 * <p>
 * Primary key is composite of:
 * <ul>
 * <li>uid - user ID of comment creator</li>
 * <li>postId - ID of the post being commented on</li>
 * <li>commentId - unique identifier within the post</li>
 * </ul>
 * </p>
 *
 * @see com.example.socialfood.model.entities.User
 * @see com.example.socialfood.model.entities.Post
 */
@Entity(tableName = "comment", foreignKeys = {
        @ForeignKey(entity = User.class, parentColumns = "uid", childColumns = "uid", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Post.class, parentColumns = { "uid", "postId" }, childColumns = {
                "uid", "postId" }, onDelete = ForeignKey.CASCADE)
}, primaryKeys = { "uid", "postId", "commentId" })

public class Comment {

    /** The ID of the user who created the comment */
    private int uid;

    /** The ID of the post this comment belongs to */
    private int postId;

    /** The unique identifier for this comment within a post */
    private int commentId;

    /** Timestamp when the comment was created */
    private long timestamp;

    @NonNull
    /** The actual content/text of the comment */
    private String content;

    /**
     * Gets the user ID of the comment creator
     * 
     * @return The user ID
     */
    public int getUid() {
        return uid;
    }

    /**
     * Sets the user ID of the comment creator
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
     * Gets the ID of the post this comment belongs to
     * 
     * @return The post ID
     */
    public int getPostId() {
        return postId;
    }

    /**
     * Sets the ID of the post this comment belongs to
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
     * Gets the unique identifier of this comment
     * 
     * @return The comment ID
     */
    public int getCommentId() {
        return commentId;
    }

    /**
     * Sets the unique identifier of this comment
     * 
     * @param commentId The comment ID to set
     * @throws IllegalArgumentException if commentId is not positive
     */
    public void setCommentId(int commentId) {
        if (commentId <= 0) {
            throw new IllegalArgumentException("Comment ID must be positive");
        }
        this.commentId = commentId;
    }

    /**
     * Generates a combined primary key from uid, postId, and commentId
     * 
     * @return The combined primary key value
     */
    public String getPrimaryKey() {
        return uid + "_" + postId + "_" + commentId;
    }

    /**
     * Gets the timestamp when the comment was created
     * 
     * @return The timestamp in milliseconds
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp when the comment was created
     * 
     * @param timestamp The timestamp in milliseconds
     * @throws IllegalArgumentException if timestamp is not positive
     */
    public void setTimestamp(long timestamp) {
        if (timestamp <= 0) {
            throw new IllegalArgumentException("Timestamp must be positive");
        }
        this.timestamp = timestamp;
    }

    /**
     * Gets the content/text of the comment
     * 
     * @return The comment content
     */
    @NonNull
    public String getContent() {
        return content;
    }

    /**
     * Sets the content/text of the comment
     * 
     * @param content The comment content to set
     * @throws IllegalArgumentException if content is empty or only whitespace
     */
    public void setContent(@NonNull String content) {
        if (content.trim().isEmpty()) {
            throw new IllegalArgumentException("Comment content cannot be empty");
        }
        this.content = content;
    }

    /**
     * Compares this comment with another object for equality. Two comments are considered equal if
     * they have the same uid, postId, commentId, timestamp and content.
     *
     * @param o The object to compare this comment against
     * @return true if the given object represents a Comment equivalent to this comment, false
     * otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Comment comment = (Comment) o;
        return uid == comment.uid &&
                postId == comment.postId &&
                commentId == comment.commentId &&
                timestamp == comment.timestamp &&
                Objects.equals(content, comment.content);
    }

    /**
     * Returns a hash code value for this comment. This implementation consistently returns the same
     * hash code for Comment objects that are equal according to the {@link #equals(Object)} method.
     *
     * @return A hash code value for this comment
     */
    @Override
    public int hashCode() {
        return Objects.hash(uid, postId, commentId, timestamp, content);
    }
}
