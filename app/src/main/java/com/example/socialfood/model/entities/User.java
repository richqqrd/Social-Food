package com.example.socialfood.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

/**
 * Entity class representing a user in the social food application. Contains user profile
 * information and statistics.
 */
@Entity(tableName = "user")
public class User implements Serializable {
    /** Unique identifier for the user, auto-generated */

    @PrimaryKey(autoGenerate = true)
    private int uid;

    /** Username of the user, must be non-null and between 3-30 characters */

    @NonNull
    private String username;

    /** URL or path to the user's profile image */

    @NonNull
    private String profilImage;

    /** Optional biography text, maximum 150 characters */

    private String bio;

    /** Number of followers this user has */

    @NonNull
    private int followersCount;

    /** Number of posts created by this user */

    @NonNull
    private int postsCount;

    /** User's password, must be non-null and at least 6 characters */

    @NonNull
    private String password;

    /**
     * Gets the user's unique identifier
     * 
     * @return The user ID
     */
    public int getUid() {
        return uid;
    }

    /**
     * Sets the user's unique identifier
     * 
     * @param id The user ID to set
     * @throws IllegalArgumentException if uid is negative
     */
    public void setUid(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("User ID cannot be negative");
        }
        this.uid = id;
    }

    /**
     * Gets the username
     * 
     * @return The username
     */
    @NonNull
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username
     * 
     * @param username The username to set
     * @throws IllegalArgumentException if username is null, empty or invalid length
     */
    public void setUsername(@NonNull String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        this.username = username;
    }

    /**
     * Gets the profile image URL
     * 
     * @return The profile image URL
     */
    @NonNull
    public String getProfilImage() {
        return profilImage;
    }

    /**
     * Sets the profile image URL
     * 
     * @param profilImage The profile image URL to set
     * @throws IllegalArgumentException if profilImage is null or empty
     */
    public void setProfilImage(@NonNull String profilImage) {
        if (profilImage == null || profilImage.trim().isEmpty()) {
            throw new IllegalArgumentException("Profile image URL cannot be null or empty");
        }
        this.profilImage = profilImage;
    }

    /**
     * Gets the user's biography
     * 
     * @return The biography text
     */
    public String getBio() {
        return bio;
    }

    /**
     * Sets the user's biography
     * 
     * @param bio The biography text to set
     * @throws IllegalArgumentException if bio exceeds 150 characters
     */
    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     * Gets the number of followers
     * 
     * @return The followers count
     */
    @NonNull
    public int getFollowersCount() {
        return followersCount;
    }

    /**
     * Sets the number of followers
     * 
     * @param followersCount The followers count to set
     * @throws IllegalArgumentException if followersCount is negative
     */
    public void setFollowersCount(@NonNull int followersCount) {
        if (followersCount < 0) {
            throw new IllegalArgumentException("Followers count cannot be negative");
        }
        this.followersCount = followersCount;
    }

    /**
     * Gets the number of posts
     * 
     * @return The posts count
     */
    @NonNull
    public int getPostsCount() {
        return postsCount;
    }

    /**
     * Sets the number of posts
     * 
     * @param postsCount The posts count to set
     * @throws IllegalArgumentException if postsCount is negative
     */
    public void setPostsCount(@NonNull int postsCount) {
        if (postsCount < 0) {
            throw new IllegalArgumentException("Posts count cannot be negative");
        }
        this.postsCount = postsCount;
    }

    /**
     * Gets the user's password
     * 
     * @return The password
     */
    @NonNull
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password
     * 
     * @param password The password to set
     * @throws IllegalArgumentException if password is null, empty or less than 6 characters
     */
    public void setPassword(@NonNull String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        this.password = password;
    }

    /**
     * Returns a string representation of this User object
     * 
     * @return A string containing all user fields
     */
    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", profilImage='" + profilImage + '\'' +
                ", bio='" + bio + '\'' +
                ", followersCount=" + followersCount +
                ", postsCount=" + postsCount +
                ", password='" + password + '\'' +
                '}';
    }

    /**
     * Compares this user with another object for equality
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
        User user = (User) o;
        return uid == user.uid &&
                followersCount == user.followersCount &&
                postsCount == user.postsCount &&
                Objects.equals(username, user.username) &&
                Objects.equals(profilImage, user.profilImage) &&
                Objects.equals(bio, user.bio) &&
                Objects.equals(password, user.password);
    }

    /**
     * Returns a hash code value for this user
     * 
     * @return A hash code based on all user fields
     */
    @Override
    public int hashCode() {
        return Objects.hash(uid, username, profilImage, bio,
                followersCount, postsCount, password);
    }

}
