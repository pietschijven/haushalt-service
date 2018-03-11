package de.pschijven.haushaltservice.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DateProvider {

    public LocalDate currentLocalDate() {
        return LocalDate.now();
    }

}
