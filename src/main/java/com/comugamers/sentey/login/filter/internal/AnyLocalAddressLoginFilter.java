package com.comugamers.sentey.login.filter.internal;

import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.login.structure.LoginContext;
import com.comugamers.sentey.login.filter.LoginFilter;

import javax.inject.Inject;

@Deprecated
public class AnyLocalAddressLoginFilter implements LoginFilter {

    @Inject
    private YamlFile config;

    @Override
    public String getName() {
        return "ANY_LOCAL_ADDRESS";
    }

    @Override
    public boolean isClean(LoginContext context) {
        // Check if we should block local addresses
        if(config.getBoolean("config.login.block-local-addresses.enabled")) {
            // Check if we should check the handshake IP address
            if(config.getBoolean("config.login.block-local-addresses.check-handshake")) {
                // If so, block the login if the handshake address is local
                return !context.getHandshakeAddress().isAnyLocalAddress();
            }

            // Check if we should check the spoofed IP address
            if(config.getBoolean("config.login.block-local-addresses.check-spoofed-address")) {
                // If so, block the login if the spoofed address is local
                return !context.getSpoofedAddress().isAnyLocalAddress();
            }
        }

        // If all conditions above failed, allow the login attempt.
        return true;
    }
}
