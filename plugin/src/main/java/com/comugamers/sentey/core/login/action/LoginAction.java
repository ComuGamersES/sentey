package com.comugamers.sentey.core.login.action;

import com.comugamers.sentey.core.login.context.LoginContext;

/**
 * Ran when an unauthorized login attempt is made.
 * @author Pabszito
 */
public interface LoginAction {

    /**
     * Handles the unauthorized login attempt.
     * @param context The login context.
     * @param detection The detection that caused the unauthorized login attempt.
     */
    void handle(LoginContext context, String detection);
}
