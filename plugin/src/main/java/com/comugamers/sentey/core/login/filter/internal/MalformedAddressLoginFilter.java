package com.comugamers.sentey.core.login.filter.internal;

import com.comugamers.sentey.common.file.YamlFile;
import com.comugamers.sentey.core.login.context.LoginContext;
import com.comugamers.sentey.core.login.filter.LoginFilter;
import com.google.common.net.InetAddresses;
import com.google.inject.Inject;

import static com.comugamers.sentey.common.util.NetworkUtil.isValidIPv4;

public class MalformedAddressLoginFilter implements LoginFilter {

    @Inject
    private YamlFile config;

    @Override
    public String getName() {
        return "MALFORMED_ADDRESS_STRING";
    }

    @Override
    public boolean isClean(LoginContext context) {
        // Check if we should check for malformed IP addresses - usually this is already done by
        // the null address check, but we do it again just in case
        if(config.getBoolean("config.login.block-invalid-addresses.enabled")) {
            // Get the spoofed address as a string
            String spoofedAddress = context.getSpoofedAddress().getHostAddress();

            // Get the handshake address as a string as well
            String handshakeAddress = context.getHandshakeAddress().getHostAddress();

            // Return false if any of the IP addresses is malformed
            return InetAddresses.isInetAddress(spoofedAddress) && InetAddresses.isInetAddress(handshakeAddress);
        }

        // If all conditions above failed, allow the login attempt
        return true;
    }
}
