package com.comugamers.sentey.ping.action.internal;

import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.report.AbuseDatabase;
import com.comugamers.sentey.report.result.AbuseReportResult;
import com.comugamers.sentey.Sentey;
import com.comugamers.sentey.ping.action.PingAction;
import com.comugamers.sentey.util.PlaceholderUtil;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.net.InetAddress;

public class AbuseReportPingAction implements PingAction {

    @Inject @Named("abuseipdb")
    private AbuseDatabase abuseDatabase;

    @Inject
    private YamlFile config;

    @Inject
    private Sentey plugin;

    @Override
    public void handle(InetAddress address) {
        // Check if the AbuseIPDB integration is enabled
        if(!config.getBoolean("config.integrations.abuseipdb.enabled")) {
            // If not, return
            return;
        }

        // Check if server list pings are ignored
        if(config.getBoolean("config.server-list-ping.actions.abuseipdb.ignore-pings")) {
            // If so, return
            return;
        }

        // Get the comment
        String comment = PlaceholderUtil.applyPlaceholdersFromPingAddress(
                config.getString("config.server-list-ping.actions.abuseipdb.comment"), address
        );

        // Report the source address
        AbuseReportResult result = abuseDatabase.reportAddress(address.getHostAddress(), comment);

        // Check if the abuse report result is "RATE_LIMIT_EXCEEDED"
        if(result == AbuseReportResult.RATE_LIMIT_EXCEEDED) {
            // If so, warn the server admin
            plugin.getLogger().warning(
                    "AbuseIPDB daily rate limit exceeded. Please consider upgrading your API key to " +
                            "continue reporting."
            );
        }
    }
}
