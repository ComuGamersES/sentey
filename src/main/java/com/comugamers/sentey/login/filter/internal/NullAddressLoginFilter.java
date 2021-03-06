package com.comugamers.sentey.login.filter.internal;

import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.login.structure.LoginContext;
import com.comugamers.sentey.login.filter.LoginFilter;

import javax.inject.Inject;

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
