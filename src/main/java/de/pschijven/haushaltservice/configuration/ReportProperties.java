package de.pschijven.haushaltservice.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "report")
@Component
public class ReportProperties {

    private String zipPassword;
    private String[] recipients;

    public String getZipPassword() {
        return zipPassword;
    }

    public void setZipPassword(String zipPassword) {
        this.zipPassword = zipPassword;
    }

    public String[] getRecipients() {
        return recipients;
    }

    public void setRecipients(String[] recipients) {
        this.recipients = recipients;
    }
}
