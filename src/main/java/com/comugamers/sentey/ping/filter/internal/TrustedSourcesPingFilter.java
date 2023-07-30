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
        if (!config.getBoolean("config.server-list-ping.filters.ignore-trusted-proxies")) {
            return true;
        }

        List<String> allowedProxies = config.getStringList(
                "config.login.unknown-proxies.allowed-proxies", false
        );

        return !allowedProxies.contains(address.getHostAddress());
    }
}
