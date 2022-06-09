package com.comugamers.sentey.login.filter;

import com.comugamers.sentey.login.structure.LoginContext;

/**
 * Represents a login filter which analyzes a login attempt based on its {@link LoginContext}.
 * @author Pabszito
 */
public interface LoginFilter {

    /**
     * The name of a {@link LoginFilter} is usually displayed on alerts. It should
     * follow the same naming convention as {@link Enum enums}.
     * @return The name of the filter as a {@link String}.
     */
    String getName();

    /**
     * Checks if a login attempt is clean or not.
     * @param context The {@link LoginContext}.
     * @return false if the login attempt should be denied.
     */
    boolean isClean(LoginContext context);
}
