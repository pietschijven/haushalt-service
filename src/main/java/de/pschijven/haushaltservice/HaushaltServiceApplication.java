package de.pschijven.haushaltservice;

import de.pschijven.haushaltservice.domain.Transaction;
import de.pschijven.haushaltservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
public class HaushaltServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HaushaltServiceApplication.class, args);
	}

}
