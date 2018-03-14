package de.pschijven.haushaltservice.reporting;

import de.pschijven.haushaltservice.configuration.ReportProperties;
import de.pschijven.haushaltservice.util.DateProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import static de.pschijven.haushaltservice.util.DateProvider.TIMEZONE;

@Component
public class ReportScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportScheduler.class);

    private final ReportEmail reportEmail;
    private final ReportProperties properties;
    private final DateProvider dateProvider;

    public ReportScheduler(ReportEmail reportEmail, ReportProperties properties, DateProvider dateProvider) {
        this.reportEmail = reportEmail;
        this.properties = properties;
        this.dateProvider = dateProvider;
    }

    @Scheduled(cron = "${report.schedule}", zone = TIMEZONE)
    public void scheduleReport() {
        if (isLastDayOfMonth()) {
            sendReport();
        }
    }

    private void sendReport() {
        String[] recipients = properties.getRecipients();
        if (recipients != null && recipients.length > 0) {
            for (String recipient : recipients) {
                sendReportTo(recipient);
            }
        }
    }

    private void sendReportTo(String recipient) {
        try {
            reportEmail.createAndSendTo(recipient);
        } catch (Exception e) {
            LOGGER.error("Error while sending report: " + e.getMessage(), e);
        }
    }

    private boolean isLastDayOfMonth() {
        LocalDate now = dateProvider.currentLocalDate();
        int currentDay = now.getDayOfMonth();
        int lastDayOfMonth = now.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
        return currentDay == lastDayOfMonth;
    }
}
