package com.comugamers.sentey.login.action.internal;

import com.comugamers.sentey.Sentey;
import com.comugamers.sentey.integrations.abuseipdb.AbuseIPDB;
import com.comugamers.sentey.integrations.abuseipdb.categories.AbuseCategory;
import com.comugamers.sentey.integrations.abuseipdb.result.AbuseReportResult;
import com.comugamers.sentey.util.file.YamlFile;
import com.comugamers.sentey.login.action.LoginAction;
import com.comugamers.sentey.login.structure.LoginContext;
import com.comugamers.sentey.util.PlaceholderUtil;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class AbuseReportLoginAction implements LoginAction {

    @Inject
    private AbuseIPDB abuseIPDB;

    @Inject
    private YamlFile config;

    @Inject
    private Sentey plugin;

    @Override
    public void handle(LoginContext context, String detection) {
        if (!config.getBoolean("config.integrations.abuseipdb.enabled")) {
            return;
        }

        if (config.getBoolean("config.login.actions.abuseipdb.ignore-login")) {
            return;
        }

        String addressAsString = context.getHandshakeAddress().getHostAddress();
        String comment = PlaceholderUtil.applyPlaceholdersFromLoginContext(
                config.getString("config.login.actions.abuseipdb.comment"), detection, context
        );

        List<AbuseCategory> categories = new ArrayList<>();
        config.getStringList("config.login.actions.abuseipdb.categories").forEach(category -> {
            try {
                categories.add(
                        AbuseCategory.valueOf(
                                category.toUpperCase()
                        )
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
