package com.comugamers.sentey.core.login.address;

import com.comugamers.sentey.common.file.YamlFile;
import com.comugamers.sentey.common.login.LoginModifier;
import com.comugamers.sentey.common.login.context.LoginContext;
import com.google.inject.Inject;

import static com.comugamers.sentey.common.util.NetworkUtil.isValidIPv4;

public class MalformedAddressLoginModifier implements LoginModifier {

    @Inject
    private YamlFile config;

    @Override
    public String getName() {
        return "MALFORMED_ADDRESS_STRING";
    }

    @Override
    public boolean handle(LoginContext context) {
        // Check if we should check for malformed IP addresses - usually this is already done by
        // the null address check, but we do it again just in case
        if(config.getBoolean("config.login.block-invalid-addresses.enabled")) {
            // Get the spoofed address as a string
            String spoofedAddress = context.getSpoofedAddress().getHostAddress();

            // Get the handshake address as a string as well
            String handshakeAddress = context.getHandshakeAddress().getHostAddress();

            // Return false if any of the IP addresses is malformed
            return isValidIPv4(spoofedAddress) && isValidIPv4(handshakeAddress);
        }

        // If all conditions above failed, allow the login attempt
        return true;
    }
}
