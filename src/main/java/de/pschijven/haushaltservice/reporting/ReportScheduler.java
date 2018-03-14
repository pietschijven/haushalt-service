package de.pschijven.haushaltservice.reporting;

import de.pschijven.haushaltservice.configuration.ReportProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReportScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportScheduler.class);

    private final ReportEmail reportEmail;
    private final ReportProperties properties;

    public ReportScheduler(ReportEmail reportEmail, ReportProperties properties) {
        this.reportEmail = reportEmail;
        this.properties = properties;
    }

    @Scheduled(cron = "${report.schedule}", zone = "Europe/Berlin")
    public void sendReport() {
        String[] recipients = properties.getRecipients();
        if (recipients != null && recipients.length > 0) {
            for (String recipient : recipients) {
                try {
                    reportEmail.createAndSendTo(recipient);
                } catch (Exception e) {
                    LOGGER.error("Error while sending report: " + e.getMessage(), e);
                }
            }
        }
    }
}
