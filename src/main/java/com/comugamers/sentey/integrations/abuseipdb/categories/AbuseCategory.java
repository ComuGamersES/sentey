package com.comugamers.sentey.integrations.abuseipdb.categories;

import java.util.Arrays;

public enum AbuseCategory {

    DNS_COMPROMISE(1),
    DNS_POISONING(2),
    FRAUD_ORDERS(3),
    DDOS_ATTACK(4),
    FTP_BRUTE_FORCE(5),
    PING_OF_DEATH(6),
    PHISHING(7),
    FRAUD_VOIP(8),
    OPEN_PROXY(9),
    WEB_SPAM(10),
    EMAIL_SPAM(11),
    BLOG_SPAM(12),
    VPN_IP(13),
    PORT_SCAN(14),
    HACKING(15),
    SQL_INJECTION(16),
    SPOOFING(17),
    BRUTE_FORCE(18),
    BAD_WEB_BOT(19),
    EXPLOITED_HOST(20),
    WEB_APP_ATTACK(21),
    SSH(22),
    IOT_TARGETED(23);

    private final int id;

    AbuseCategory(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    /**
     * Finds an {@link AbuseCategory} by its <a href="https://abuseipdb.com">identifier</a>.
     *
     * @param id The <a href="https://abuseipdb.com">identifier</a> of the {@link AbuseCategory}.
     * @return The {@link AbuseCategory}.
     */
    public static AbuseCategory fromId(int id) {
        return Arrays.stream(AbuseCategory.values())
                .filter(category -> category.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No abuse category with ID " + id + " was found!"));
    }
}
