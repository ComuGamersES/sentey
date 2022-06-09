package com.comugamers.sentey.report;

import com.comugamers.sentey.report.abuseipdb.AbuseIPDB;
import com.comugamers.sentey.report.result.AbuseReportResult;

import java.net.InetAddress;

/**
 * Represents an abuse database wrapper. An example would be <a href="https://abuseipdb.com">AbuseIPDB</a>,
 * which has its own {@link AbuseIPDB implementation} of this interface.
 * @author Pabszito
 */
public interface AbuseDatabase {

    /**
     * Reports an IP address to an external abuse database.
     * @param address The IP address to report as a {@link String}.
     * @param comment A comment explaining the reason of the report.
     * @return an {@link AbuseReportResult}.
     */
    AbuseReportResult reportAddress(String address, String comment);

    /**
     * Reports an IP address to an external abuse database.
     * @param address The IP address to report as a {@link InetAddress}.
     * @param comment A comment explaining the reason of the report.
     * @return an {@link AbuseReportResult}.
     */
    default AbuseReportResult reportAddress(InetAddress address, String comment) {
        return reportAddress(address.getHostAddress(), comment);
    }

    /**
     * Reports an IP address to an external abuse database without a comment.
     * @param address The IP address to report as a {@link String}.
     * @return an {@link AbuseReportResult}.
     */
    default AbuseReportResult reportAddress(String address) {
        return reportAddress(address, "sentey: no comment specified");
    }

    /**
     * Reports an IP address to an external abuse database without a comment.
     * @param address The IP address to report as a {@link InetAddress}.
     * @return an {@link AbuseReportResult}.
     */
    default AbuseReportResult reportAddress(InetAddress address) {
        return reportAddress(address, "sentey: no comment specified");
    }

    /**
     * Updates the authorization key being used for this {@link AbuseDatabase}.
     * @param key The authorization key as a {@link String}.
     */
    void updateKey(String key);
}
