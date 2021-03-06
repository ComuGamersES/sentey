package com.comugamers.sentey.ping.filter.internal;

import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.ping.filter.PingFilter;

import javax.inject.Inject;
import java.net.InetAddress;
import java.util.List;

public class TrustedSourcesPingFilter implements PingFilter {

    @Inject
    private YamlFile config;

    @Override
    public boolean isClean(InetAddress address) {
        // Check if we should filter trusted proxies
        if(!config.getBoolean("config.server-list-ping.filters.ignore-trusted-proxies")) {
            // If so, return
            return true;
        }

        // Get the list of trusted proxies
        List<String> allowedProxies = config.getStringList(
                "config.login.unknown-proxies.allowed-proxies", false
        );

        // Check if the IP address is trusted. If so, return true.
        return !allowedProxies.contains(address.getHostAddress());
    }
}
