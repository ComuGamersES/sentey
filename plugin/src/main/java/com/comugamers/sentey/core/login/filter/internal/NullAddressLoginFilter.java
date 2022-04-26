package com.comugamers.sentey.core.login.filter.internal;

import com.comugamers.sentey.common.file.YamlFile;
import com.comugamers.sentey.core.login.context.LoginContext;
import com.comugamers.sentey.core.login.filter.LoginFilter;
import com.google.inject.Inject;

public class NullAddressLoginFilter implements LoginFilter {

    @Inject
    private YamlFile config;

    @Override
    public String getName() {
        return "NULL_ADDRESS";
    }

    @Override
    public boolean isClean(LoginContext context) {
        // Check if we should check for null addresses
        if(!config.getBoolean("config.login.block-null-addresses.enabled")) {
            // If not, allow the login attempt.
            return true;
        }

        // Allow the login attempt if both IP addresses are valid.
        // If one of them is null or empty, the login attempt is denied.
        return context.isValidSpoofedAddress() && context.isValidHandshakeAddress();
    }
}
