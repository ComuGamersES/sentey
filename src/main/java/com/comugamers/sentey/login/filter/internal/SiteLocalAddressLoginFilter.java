package com.comugamers.sentey.login.filter.internal;

import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.login.structure.LoginContext;
import com.comugamers.sentey.login.filter.LoginFilter;

import javax.inject.Inject;

@Deprecated
public class SiteLocalAddressLoginFilter implements LoginFilter {

    @Inject
    private YamlFile config;

    @Override
    public String getName() {
        return "SITE_LOCAL_IP_ADDRESS";
    }

    @Override
    public boolean isClean(LoginContext context) {
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
