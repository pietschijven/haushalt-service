package de.pschijven.haushaltservice.application;

import de.pschijven.haushaltservice.domain.AbrechnungFormBean;
import de.pschijven.haushaltservice.domain.Transaction;
import de.pschijven.haushaltservice.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Controller
public class AbrechnungsController {

    private final TransactionService transactionService;

    public AbrechnungsController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/abrechnung")
    public String index(final Model model) {
        model.addAttribute("abrechnungFormBean", new AbrechnungFormBean());
        return "abrechnung";
    }

    @PostMapping("/abrechnung")
    public ModelAndView show(final AbrechnungFormBean formBean) {
        ModelAndView modelAndView = new ModelAndView("abrechnung");

        if (formBean.getMonth() != null && !formBean.getMonth().isEmpty()) {
            List<Transaction> transactions = transactionService.transactionsInMonth(formBean.getMonth());

            modelAndView.addObject("abrechnungFormBean", formBean);
            modelAndView.addObject("transactions", transactions);
            modelAndView.addObject("showTransactions", true);
            modelAndView.addObject("currentMonth", selectedMonth(formBean.getMonth()));
            return modelAndView;
        }
        modelAndView.addObject("abrechnungFormBean", new AbrechnungFormBean());
        return modelAndView;
    }

    private String selectedMonth(final String month) {
        Locale locale = new Locale("de", "DE");
        LocalDate localDate = YearMonth.parse(month).atDay(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM, yyyy", locale);
        return formatter.format(localDate);
    }
}
