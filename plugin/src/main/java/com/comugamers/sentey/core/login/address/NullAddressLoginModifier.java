package com.comugamers.sentey.core.login.address;

import com.comugamers.sentey.common.file.YamlFile;
import com.comugamers.sentey.common.login.LoginModifier;
import com.comugamers.sentey.common.login.context.LoginContext;
import com.google.inject.Inject;

public class NullAddressLoginModifier implements LoginModifier {

    @Inject
    private YamlFile config;

    @Override
    public String getName() {
        return "NULL_ADDRESS";
    }

    @Override
    public boolean handle(LoginContext context) {
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
