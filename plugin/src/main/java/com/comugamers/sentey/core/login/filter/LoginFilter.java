package com.comugamers.sentey.core.login.filter;

import com.comugamers.sentey.core.login.context.LoginContext;

/**
 * Represents a login security module.
 * These are later ran by the Sentey plugin on player login.
 */
public interface LoginFilter {

    /**
     * The name of the module displayed on detection types.
     */
    String getName();

    /**
     * Handles the login attempt.
     * @param context The login context.
     * @return false if the login attempt should be cancelled.
     */
    boolean isClean(LoginContext context);
}
