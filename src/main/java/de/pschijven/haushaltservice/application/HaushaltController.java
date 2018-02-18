package de.pschijven.haushaltservice.application;

import de.pschijven.haushaltservice.domain.TransactionFormBean;
import de.pschijven.haushaltservice.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HaushaltController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HaushaltController.class);
    private final TransactionService transactionService;

    public HaushaltController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public String index(final Model model) {
        model.addAttribute("transactionFormBean", new TransactionFormBean());
        return "index";
    }

    @PostMapping("/transaction")
    public String persistTransaction(@ModelAttribute TransactionFormBean formBean) {
        LOGGER.info(formBean.toString());
        transactionService.persistTransaction(formBean.getAmount(), "piet", formBean.getDescription());
        return "redirect:index";
    }
}
