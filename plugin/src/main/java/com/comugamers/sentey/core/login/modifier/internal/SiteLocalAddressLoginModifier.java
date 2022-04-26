package com.comugamers.sentey.core.login.modifier.internal;

import com.comugamers.sentey.common.file.YamlFile;
import com.comugamers.sentey.core.login.context.LoginContext;
import com.comugamers.sentey.core.login.modifier.LoginModifier;
import com.google.inject.Inject;

public class SiteLocalAddressLoginModifier implements LoginModifier {

    @Inject
    private YamlFile config;

    @Override
    public String getName() {
        return "SITE_LOCAL_IP_ADDRESS";
    }

    @Override
    public boolean handle(LoginContext context) {
        // Check if we should block site local addresses
        if(config.getBoolean("config.login.block-site-local-addresses.enabled")) {
            // Check if we should check the handshake IP address
            if(config.getBoolean("config.login.block-site-local-addresses.check-handshake")) {
                // If so, check if the handshake IP address is a site local address
                return !context.getHandshakeAddress().isSiteLocalAddress();
            }

            // Check if we should check the spoofed IP address as well
            if(config.getBoolean("config.login.block-site-local-addresses.check-spoofed-address")) {
                // If so, check if the spoofed IP address is a site local address
                return !context.getSpoofedAddress().isSiteLocalAddress();
            }
        }

        // If all conditions above failed, allow the login attempt.
        return true;
    }
}
