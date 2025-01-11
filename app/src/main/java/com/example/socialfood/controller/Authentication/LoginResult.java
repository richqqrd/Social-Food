package com.example.socialfood.controller.Authentication;

/**
 * Enum representing possible outcomes of a login attempt. Used by the authentication controller to
 * indicate login status.
 */
public enum LoginResult {
    /**
     * Login was successful, credentials were valid
     */
    SUCCESS,

    /**
     * Required fields (username/password) were empty
     */
    EMPTY_FIELDS,

    /**
     * Provided credentials did not match any user
     */
    INVALID_CREDENTIALS
}