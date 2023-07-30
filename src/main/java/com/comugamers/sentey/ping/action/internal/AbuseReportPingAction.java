package com.comugamers.sentey.ping.action.internal;

import com.comugamers.sentey.integrations.abuseipdb.AbuseIPDB;
import com.comugamers.sentey.integrations.abuseipdb.categories.AbuseCategory;
import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.integrations.abuseipdb.result.AbuseReportResult;
import com.comugamers.sentey.Sentey;
import com.comugamers.sentey.ping.action.PingAction;
import com.comugamers.sentey.util.PlaceholderUtil;

import javax.inject.Inject;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import static com.comugamers.sentey.util.IntegerUtil.isInteger;

public class AbuseReportPingAction implements PingAction {

    @Inject
    private AbuseIPDB abuseIPDB;

    @Inject
    private YamlFile config;

    @Inject
    private Sentey plugin;

    @Override
    public void handle(InetAddress address) {
        if (!config.getBoolean("config.integrations.abuseipdb.enabled")) {
            return;
        }

        if (config.getBoolean("config.server-list-ping.actions.abuseipdb.ignore-pings")) {
            return;
        }

        String addressAsString = address.getHostAddress();
        String comment = PlaceholderUtil.applyPlaceholdersFromPingAddress(
                config.getString("config.server-list-ping.actions.abuseipdb.comment"), address
        );

        List<AbuseCategory> categories = new ArrayList<>();
        config.getStringList("config.server-list-ping.actions.abuseipdb.categories").forEach(category -> {
            try {
                categories.add(
                        AbuseCategory.valueOf(category.toUpperCase())
                );
            } catch (IllegalArgumentException ex) {
                plugin.getLogger().warning(
                        "Invalid abuse category with name '" + category + "'! Skipping..."
                );
            }
        });

        if (categories.isEmpty()) {
            categories.add(AbuseCategory.HACKING);
            categories.add(AbuseCategory.PORT_SCAN);
        }

        abuseIPDB.reportAddress(
                addressAsString,
                comment,
                categories.toArray(new AbuseCategory[0])
        );

        AbuseReportResult result = abuseIPDB.reportAddress(
                addressAsString,
                comment,
                categories.toArray(new AbuseCategory[0])
        );

        if (result == AbuseReportResult.RATE_LIMIT_EXCEEDED) {
            plugin.getLogger().warning(
                    "AbuseIPDB daily rate limit exceeded. Please consider upgrading your API key to " +
                            "continue reporting."
            );
        }
    }
}
