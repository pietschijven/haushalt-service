package de.pschijven.haushaltservice.service;

import de.pschijven.haushaltservice.domain.Abrechnung;
import de.pschijven.haushaltservice.domain.Transaction;
import de.pschijven.haushaltservice.domain.UserMetadata;
import de.pschijven.haushaltservice.security.Auth0ManagementAPI;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@Service
public class AbrechnungService {

    private final Auth0ManagementAPI api;
    private final TransactionService service;

    public AbrechnungService(Auth0ManagementAPI api, TransactionService service) {
        this.api = api;
        this.service = service;
    }

    public Abrechnung computeAbrechnung(final String month) {
        List<Transaction> transactions = service.transactionsInMonth(month);
        List<UserMetadata> metadata = api.fetchUserMetadata();

        return compute(transactions, metadata);
    }

    private Abrechnung compute(List<Transaction> transactions, List<UserMetadata> metadata) {
        Map<String, BigDecimal> subtotals = computeSubTotals(transactions);
        Map<String, BigDecimal> amountToPay = computeAmountToPay(subtotals, metadata);
        return new Abrechnung(subtotals, amountToPay);
    }

    private Map<String, BigDecimal> computeAmountToPay(Map<String, BigDecimal> subtotals, List<UserMetadata> metadata) {
        Map<String, BigDecimal> metaDataMap = metadata.stream()
                .collect(toMap(UserMetadata::getName, UserMetadata::getSalary));
        Map<String, BigDecimal> salaryFractions = computeSalaryFractions(metaDataMap);
        BigDecimal total = computeTotal(subtotals);

        Map<String, BigDecimal> toPay = new HashMap<>();
        for (String username : subtotals.keySet()) {
            BigDecimal fraction = salaryFractions.get(username);
            BigDecimal subtotal = subtotals.get(username);
            BigDecimal amountToPay = total.multiply(fraction).subtract(subtotal);
            toPay.put(username, amountToPay);
        }
        return toPay;
    }

    private Map<String, BigDecimal> computeSalaryFractions(Map<String, BigDecimal> metaDataMap) {
        BigDecimal totalSalary = metaDataMap.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        return metaDataMap.entrySet().stream()
                .collect(toMap(Map.Entry::getKey, e -> e.getValue().divide(totalSalary, 2, BigDecimal.ROUND_HALF_UP)));
    }

    private BigDecimal computeTotal(Map<String, BigDecimal> subtotals) {
        return subtotals.values().stream()
                .collect(reducing(BigDecimal.ZERO, BigDecimal::add));
    }

    Map<String, BigDecimal> computeSubTotals(List<Transaction> transactions) {
        return transactions.stream()
                .collect(
                        groupingBy(
                                Transaction::getUsername,
                                reducing(
                                        BigDecimal.ZERO,
                                        Transaction::getAmount,
                                        BigDecimal::add
                                )));
    }

}
