package de.pschijven.haushaltservice.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

@Component
public class DateProvider {

    public static final String TIMEZONE = "Europe/Berlin";

    public LocalDate currentLocalDate() {
        return LocalDate.now(ZoneId.of("Europe/Berlin"));
    }

}
