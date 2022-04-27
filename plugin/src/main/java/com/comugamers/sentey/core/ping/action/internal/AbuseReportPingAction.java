package com.comugamers.sentey.core.ping.action.internal;

import com.comugamers.sentey.common.file.YamlFile;
import com.comugamers.sentey.common.report.AbuseDatabase;
import com.comugamers.sentey.common.report.result.AbuseReportResult;
import com.comugamers.sentey.core.Sentey;
import com.comugamers.sentey.core.ping.action.PingAction;
import com.comugamers.sentey.core.util.PlaceholderUtil;
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

        // Get the comment
        String comment = PlaceholderUtil.applyPlaceholdersFromPingContext(
                config.getString("config.server-list-ping.actions.abuseipdb.comment"), address
        );

        // Report it
        AbuseReportResult result = abuseDatabase.reportAddress(address.getHostAddress(), comment);

        // Check if the abuse report result is "RATE_LIMIT_EXCEEDED"
        if(result == AbuseReportResult.RATE_LIMIT_EXCEEDED) {
            // If so, warn the server admin
            plugin.getLogger().warning("AbuseIPDB daily rate limit exceeded. Please consider upgrading your API key.");
        }
    }
}
