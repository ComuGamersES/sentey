package com.comugamers.sentey.login.filter.internal;

import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.login.structure.LoginContext;
import com.comugamers.sentey.login.filter.LoginFilter;

import javax.inject.Inject;
import java.util.List;

public class UnknownProxyLoginFilter implements LoginFilter {

    @Inject
    private YamlFile config;

    @Override
    public String getName() {
        return "UNKNOWN_PROXY";
    }

    @Override
    public boolean isClean(LoginContext context) {
        // Check if the 'Unknown proxy' detection type is enabled
        if(config.getBoolean("config.login.unknown-proxies.enabled")) {
            // If so, check if the detection is on setup mode
            if(config.getBoolean("config.login.unknown-proxies.setup")) {
                // If true, allow the login attempt
                return true;
            }

            // Get the list of allowed proxies
            List<String> allowedProxies = config.getStringList(
                    "config.login.unknown-proxies.allowed-proxies", false
            );

            // Allow or disallow the connection depending on whether the proxy
            // is marked as trusted or not.
            return allowedProxies.contains(context.getHandshakeAddress().getHostAddress());
        }

        // If all conditions above failed, allow the login attempt
        return true;
    }
}
