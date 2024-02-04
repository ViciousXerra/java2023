package edu.project3.reportcomposers.statcontentcreators;

import java.util.HashMap;
import java.util.Map;

public interface StatContentCreator {

    String generateContent();

    String getFileName();

    void collectStat();

    enum ReportType {
        DEFAULT,
        MARKDOWN,
        ADOC;

        private final static Map<String, ReportType> TYPE_MAPPING = new HashMap<>() {
            {
                put("markdown", MARKDOWN);
                put("adoc", ADOC);
            }
        };

        public static ReportType resolveReportType(String reportType) {
            return TYPE_MAPPING.getOrDefault(reportType, DEFAULT);
        }

    }

}
