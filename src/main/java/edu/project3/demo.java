package edu.project3;

import edu.project3.logstreamextractors.LogStreamExtractor;
import edu.project3.logstreamextractors.URLLogStreamExtractor;
import java.util.Arrays;

public class demo {
    public static void main(String[] args) {
        LogStreamExtractor e = new URLLogStreamExtractor(
            "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs");
        LogStatReportComposer c = new LogStatReportComposer(
            "2015-12-17",
            "2015-12-31",
            e.extract(),
            Arrays.stream(e.getSourceName()),
            LogStatReportComposer.ReportType.DEFAULT,
            ""
            );
        c.collectStat();
        System.out.println(c.getTxtString());
    }
}
