package com.comugamers.sentey.common.report;

import com.comugamers.sentey.common.report.result.AbuseReportResult;

public interface AbuseDatabase {

    AbuseReportResult reportAddress(String address, String comment);

    void updateKey(String key);
}
