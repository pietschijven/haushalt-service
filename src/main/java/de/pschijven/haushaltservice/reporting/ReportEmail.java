package de.pschijven.haushaltservice.reporting;

import com.sendgrid.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.sendgrid.SendGridProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


/**
 * Sends the email containing the montly transaction report
 */
@Component
public class ReportEmail {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportEmail.class);
    private static final String ATTACHMENT_FILENAME = "haushaltskassen-abrechnung.zip";

    private final Report report;
    private final SendGrid sendGrid;
    private final SendGridProperties properties;

    public ReportEmail(Report report, SendGrid sendGrid, SendGridProperties properties) {
        this.report = report;
        this.sendGrid = sendGrid;
        this.properties = properties;
    }

    public void createAndSendTo(final String emailAddress) throws Exception {
        Mail mail = createEmail(emailAddress);
        sendEmail(mail, emailAddress);
    }

    private void sendEmail(Mail mail, String emailAddress) throws IOException {
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        try {
            sendGrid.api(request);
            LOGGER.info("Successfully send the email to " + emailAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Mail createEmail(String emailAddress) throws Exception {
        Email from = new Email(properties.getUsername());
        Email to = new Email(emailAddress);
        Content content = createContent();
        Attachments attachments = createAttachments();

        String subject = "Abrechnung Haushaltskasse für " + currentMonth();
        Mail mail = new Mail(from, subject, to, content);
        mail.addAttachments(attachments);
        return mail;
    }

    private Attachments createAttachments() throws Exception {
        InputStream content = report.createReport();
        return new Attachments.Builder(ATTACHMENT_FILENAME, content)
                .withType("application/zip")
                .build();
    }

    private Content createContent() {
        String body = "<p>Abrechnung für " + currentMonth() + "</p>";
        return new Content("text/html", body);
    }

    private String currentMonth() {
        Locale locale = new Locale("de", "DE");
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM, yyyy", locale);
        return formatter.format(localDate);
    }
}
