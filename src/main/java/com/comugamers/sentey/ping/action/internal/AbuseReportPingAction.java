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

        // Get the address as a string
        String addressAsString = address.getHostAddress();

        // Get the comment
        String comment = PlaceholderUtil.applyPlaceholdersFromPingAddress(
                config.getString("config.server-list-ping.actions.abuseipdb.comment"), address
        );


        // Create a new array list of categories
        List<AbuseCategory> categories = new ArrayList<>();

        // Get each raw category and parse it
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

        // Check if the list of categories is empty
        if (categories.isEmpty()) {
            // If true, add some default ones:
            categories.add(AbuseCategory.HACKING);
            categories.add(AbuseCategory.PORT_SCAN);
        }

        // Report the address
        abuseIPDB.reportAddress(
                addressAsString,
                comment,
                categories.toArray(new AbuseCategory[0])
        );

        // Report the source address
        AbuseReportResult result = abuseIPDB.reportAddress(
                addressAsString,
                comment,
                categories.toArray(new AbuseCategory[0])
        );

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
