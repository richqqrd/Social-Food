package com.example.socialfood.controller.User;

import android.content.Context;

import com.example.socialfood.controller.BaseController;
import com.example.socialfood.model.handler.FollowHandler;
import com.example.socialfood.model.handler.UserHandler;
import com.example.socialfood.model.entities.Follow;
import com.example.socialfood.model.entities.User;

/**
 * Controller class for handling user-related operations. Implements UserControllerInterface and
 * extends BaseController. Manages user creation, updates, deletions and follow relationships.
 */
public class UserController extends BaseController implements com.example.socialfood.controller.User.UserControllerInterface {
    private final UserHandler userHandler;
    private final FollowHandler followHandler;

    /**
     * Constructs a new UserController
     *
     * @param context Application context
     * @param userHandler Handler for user operations, creates new if null
     * @param followHandler Handler for follow operations, creates new if null
     */
    public UserController(Context context, UserHandler userHandler, FollowHandler followHandler) {
        super(context);
        this.userHandler = userHandler != null ? userHandler : new UserHandler(context);
        this.followHandler = followHandler != null ? followHandler : new FollowHandler(context);
    }

    @Override
    public int createUser(User user) {
        if (userHandler.insert(user)) {
            return userHandler.getUserByUsername(user.getUsername()).getUid();
        }
        return -1;
    }

    @Override
    public boolean updateUser(User user) {
        return userHandler.update(user);
    }

    @Override
    public boolean deleteUser(User user) {
        return userHandler.delete(user);
    }

    @Override
    public boolean followUser(int userId) {
        User currentUser = getCurrentUser();
        User userToFollow = userHandler.getUserById(userId);

        if (currentUser != null && userToFollow != null) {
            Follow follow = new Follow();
            follow.setFollowerId(currentUser.getUid());
            follow.setFollowedId(userId);
            follow.setTimestamp(System.currentTimeMillis());

            if (followHandler.insert(follow)) {
                userToFollow.setFollowersCount(userToFollow.getFollowersCount() + 1);
                return userHandler.update(userToFollow);
            }
        }
        return false;
    }

    @Override
    public boolean unfollowUser(int userId) {
        User currentUser = getCurrentUser();
        User userToUnfollow = userHandler.getUserById(userId);

        if (currentUser != null && userToUnfollow != null) {
            if (followHandler.delete(currentUser.getUid(), userId)) {
                int newFollowersCount = userToUnfollow.getFollowersCount() - 1;
                if (newFollowersCount < 0) {
                    newFollowersCount = 0;
                }
                userToUnfollow.setFollowersCount(newFollowersCount);
                return userHandler.update(userToUnfollow);
            }
        }
        return false;
    }

    @Override
    public boolean setCurrentUser(User user) {
        if (user != null) {
            userManager.loginUser(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User getCurrentUser() {
        return userManager.getCurrentUser();
    }

    @Override
    public int getCurrentUserId() {
        return userManager.getCurrentUserId();
    }

    @Override
    public boolean loadCurrentUser(int userId) {
        User user = userHandler.getUserById(userId);
        return setCurrentUser(user);
    }

    @Override
    public boolean isFollowing(int userId) {
        return followHandler.exists(getCurrentUserId(), userId);
    }

    @Override
    public User getUserById(int userId) {
        return userHandler.getUserById(userId);
    }
}
