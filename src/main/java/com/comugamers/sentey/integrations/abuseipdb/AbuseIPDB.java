package com.comugamers.sentey.integrations.abuseipdb;

import com.comugamers.sentey.integrations.abuseipdb.categories.AbuseCategory;
import com.comugamers.sentey.integrations.abuseipdb.result.AbuseReportResult;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.bukkit.plugin.Plugin;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class AbuseIPDB {

    private final Plugin plugin;
    private final List<String> onCooldown;
    private String key;

    public AbuseIPDB(Plugin plugin, String key) {
        this.plugin = plugin;
        this.onCooldown = new ArrayList<>();
        this.key = key;
    }

    private JsonObject submitReport(String address, String comment, int... categories) {
        if (categories == null || categories.length == 0) {
            throw new IllegalArgumentException("No categories were provided!");
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost("https://api.abuseipdb.com/api/v2/report");
            request.addHeader("Key", key);
            request.addHeader("Content-Type", "application/json");
            request.addHeader("User-Agent", "ComuGamers Sentey");

            JsonObject entity = new JsonObject();
            String parsedCategories = StringUtils.join(ArrayUtils.toObject(categories), ",");

            entity.addProperty("ip", address);
            entity.addProperty("categories", parsedCategories);
            entity.addProperty("comment", comment);

            StringEntity stringEntity = new StringEntity(entity.toString());
            request.setEntity(stringEntity);

            HttpResponse response = httpClient.execute(request);

            try (InputStream inputStream = response.getEntity().getContent()) {
                String rawResponse = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                return new JsonParser()
                        .parse(rawResponse)
                        .getAsJsonObject();
            }
        } catch (Exception ex) {
            plugin.getLogger().log(Level.SEVERE,
                    "An error occurred while reporting an address to AbuseIPDB (is the API key valid?):", ex);

            return null;
        }
    }

    /**
     * Reports an IP address to <a href="https://www.abuseipdb.com">AbuseIPDB</a>.
     *
     * @param address The IP address to report.
     * @param comment The comment to include with the report.
     * @return An {@link AbuseReportResult}, which can be <code>SUCCESS</code> or an error.
     * @see #reportAddress(String, String, AbuseCategory...)
     * @see #reportAddress(String, String, int...)
     * @deprecated Please provide categories by either using the {@link AbuseCategory} enum or
     * <a href="https://www.abuseipdb.com/categories">category IDs</a>.
     */
    @Deprecated
    public AbuseReportResult reportAddress(String address, String comment) {
        return reportAddress(address, comment, 14, 15);
    }

    /**
     * Reports an IP address to <a href="https://www.abuseipdb.com">AbuseIPDB</a>.
     *
     * @param address    The IP address to report.
     * @param comment    The comment to include with the report.
     * @param categories An array of {@link AbuseCategory abuse categories}.
     * @return An {@link AbuseReportResult}, which can be <code>SUCCESS</code> or an error.
     * @see #submitReport(String, String, int...)
     */
    public AbuseReportResult reportAddress(String address, String comment, AbuseCategory... categories) {
        return reportAddress(
                address,
                comment,
                Arrays.stream(categories)
                        .mapToInt(AbuseCategory::getId)
                        .toArray()
        );
    }

    /**
     * Reports an IP address to <a href="https://www.abuseipdb.com">AbuseIPDB</a>.
     *
     * @param address    The IP address to report.
     * @param comment    The comment to include with the report.
     * @param categories A list of <a href="">IDs</a> of each category.
     * @return An {@link AbuseReportResult}, which can be <code>SUCCESS</code> or an error.
     * @see #reportAddress(String, String, AbuseCategory...)
     */
    public AbuseReportResult reportAddress(String address, String comment, int... categories) {
        if (onCooldown.contains(address)) {
            return AbuseReportResult.ON_COOLDOWN;
        }

        JsonObject jsonObject = submitReport(address, comment, categories);
        if (jsonObject == null) {
            return AbuseReportResult.ERROR;
        }

        onCooldown.add(address);

        if (jsonObject.has("errors")) {
            onCooldown.remove(address);

            JsonElement element = jsonObject.get("errors")
                    .getAsJsonArray()
                    .get(0)
                    .getAsJsonObject();

            int statusCode = element.getAsJsonObject()
                    .get("status")
                    .getAsInt();

            String detail = element.getAsJsonObject()
                    .get("detail")
                    .getAsString();

            if (statusCode == 403) {
                plugin.getLogger().warning(
                        "Unable to report abuse for " + address + ": 'On cooldown, not known locally.'"
                );

                return AbuseReportResult.ON_COOLDOWN;
            }

            if (statusCode == 429) {
                return AbuseReportResult.RATE_LIMIT_EXCEEDED;
            }

            plugin.getLogger().warning(
                    "Unable to report abuse for " + address + ": 'Request failed with status code "
                            + statusCode + " (" + detail + ")'"
            );

            return AbuseReportResult.ERROR;
        } else {
            plugin.getServer().getScheduler().runTaskLater(
                    plugin, () -> onCooldown.remove(address), 18000L
            );

            return AbuseReportResult.SUCCESS;
        }
    }

    /**
     * Updates the authorization key being used for making requests.
     *
     * @param key The authorization key as a {@link String}.
     */
    public void updateKey(String key) {
        this.key = key;
    }
}
