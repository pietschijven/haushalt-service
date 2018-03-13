package de.pschijven.haushaltservice.application;

import de.pschijven.haushaltservice.configuration.ReportProperties;
import de.pschijven.haushaltservice.reporting.ReportEmail;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SendGridController {

    private final ReportEmail reportEmail;
    private final ReportProperties properties;

    public SendGridController(ReportEmail reportEmail, ReportProperties properties) {
        this.reportEmail = reportEmail;
        this.properties = properties;
    }

    @GetMapping("/sendgrid")
    public String sendgridTest() throws Exception {
        if (properties.getRecipients() != null) {
            for (String recipient : properties.getRecipients()) {
                reportEmail.createAndSendTo(recipient);
            }
        }
        return "redirect:index";
    }


}
