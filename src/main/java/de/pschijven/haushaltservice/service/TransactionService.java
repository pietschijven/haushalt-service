package de.pschijven.haushaltservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import de.pschijven.haushaltservice.database.TransactionDao;
import de.pschijven.haushaltservice.domain.Transaction;
import de.pschijven.haushaltservice.util.DateProvider;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.firstDayOfNextMonth;

@Service
public class TransactionService {

    private final TransactionDao transactionDao;
    private final DateProvider dateProvider;

    public TransactionService(final TransactionDao transactionDao, DateProvider dateProvider) {
        this.transactionDao = transactionDao;
        this.dateProvider = dateProvider;
    }

    public void persistTransaction(final BigDecimal amount, final String username, final String description) {
        Transaction transaction = Transaction.create(amount, description, username);
        transactionDao.persist(transaction);
    }

     public List<Transaction> transactionsInCurrentMonth() {
        LocalDate now = dateProvider.currentLocalDate();
        LocalDate firstDayOfMonth = now.with(firstDayOfMonth());
        LocalDate firstDayOfNextMonth = now.with(firstDayOfNextMonth());
        return transactionDao.findTransactionsInPeriod(firstDayOfMonth, firstDayOfNextMonth);
    }


    public String toCSV () throws JsonProcessingException {
        List<Transaction> transactions = transactionsInCurrentMonth();
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(Transaction.class).withHeader();
        return mapper.writer(schema).writeValueAsString(transactions);
    }
}
