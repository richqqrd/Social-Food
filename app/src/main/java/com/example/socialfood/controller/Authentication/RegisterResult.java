package com.example.socialfood.controller.Authentication;

/**
 * Enum representing possible outcomes of a registration attempt. Used by the authentication
 * controller to indicate registration status.
 */
public enum RegisterResult {
    /**
     * Registration was successful, user account created
     */
    SUCCESS,

    /**
     * Required registration fields were empty
     */
    EMPTY_FIELDS,

    /**
     * The chosen username is already taken by another user
     */
    USERNAME_TAKEN,

    /**
     * Password and password confirmation do not match
     */
    PASSWORD_MISMATCH
}