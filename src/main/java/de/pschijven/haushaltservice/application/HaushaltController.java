package de.pschijven.haushaltservice.application;

import de.pschijven.haushaltservice.domain.Auth0Properties;
import de.pschijven.haushaltservice.domain.Transaction;
import de.pschijven.haushaltservice.domain.TransactionFormBean;
import de.pschijven.haushaltservice.security.TokenAuthentication;
import de.pschijven.haushaltservice.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
public class HaushaltController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HaushaltController.class);
    private final Auth0Properties properties;
    private final TransactionService transactionService;

    public HaushaltController(Auth0Properties properties, TransactionService transactionService) {
        this.properties = properties;
        this.transactionService = transactionService;
    }

    @GetMapping("/")
    public String index(final Model model, final Principal principal) {
        model.addAttribute("transactionFormBean", new TransactionFormBean());
        model.addAttribute("principal", usernameFor(principal));
        model.addAttribute("recentTransactions", transactionsForCurrentUser(principal));
        model.addAttribute("currentMonth", currentMonth());
        return "index";
    }

    private String currentMonth() {
        Locale locale = new Locale("de", "DE");
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM, yyyy", locale);
        return formatter.format(localDate);
    }

    private List<TransactionFormBean> transactionsForCurrentUser(final Principal principal) {
        List<Transaction> transactions = transactionService.transactionsInCurrentMonth();
        String currentUser = usernameFor(principal);
        List<TransactionFormBean> formBeans = transactions.stream()
                .filter(t -> t.getUsername().equals(currentUser))
                .map(t -> new TransactionFormBean(t.getAmount(), t.getDescription()))
                .collect(Collectors.toList());
        Collections.reverse(formBeans);
        return formBeans;
    }

    private String usernameFor(Principal principal) {
        if (principal instanceof TokenAuthentication) {
            TokenAuthentication auth = (TokenAuthentication)principal;
            return auth.getUsername();
        }
        return principal.getName();
    }

    @PostMapping("/transaction")
    public String persistTransaction(@ModelAttribute TransactionFormBean formBean,
                                     final Principal principal,
                                     final RedirectAttributes redirectAttributes) {
        LOGGER.info(formBean.toString());
        String username = usernameFor(principal);
        transactionService.persistTransaction(formBean.getAmount(), username, formBean.getDescription());
        return "redirect:";
    }
}
