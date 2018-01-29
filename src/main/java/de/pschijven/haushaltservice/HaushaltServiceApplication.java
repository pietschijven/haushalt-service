package de.pschijven.haushaltservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class HaushaltServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HaushaltServiceApplication.class, args);
	}

	@Controller
	public static class HaushaltController {

		@GetMapping
		public String index() {
			return "index";
		}
	}
}
