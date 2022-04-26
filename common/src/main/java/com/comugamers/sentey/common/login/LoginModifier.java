package com.comugamers.sentey.common.login;

import com.comugamers.sentey.common.login.context.LoginContext;

/**
 * Represents a login security module.
 * These are later ran by the Sentey plugin on player login.
 */
public interface LoginModifier {

    /**
     * The human-readable name of the module.
     */
    String getName();

    /**
     * Handles the login attempt.
     * @param context The login context.
     * @return Whether the login attempt was allowed or not.
     */
    boolean handle(LoginContext context);
}
