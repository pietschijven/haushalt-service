package de.pschijven.haushaltservice.service;

import de.pschijven.haushaltservice.database.TransactionDao;
import de.pschijven.haushaltservice.domain.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService {

    private final TransactionDao transactionDao;

    public TransactionService(final TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    public void persistTransaction(final BigDecimal amount, final String username, final String description) {
        Transaction transaction = Transaction.create(amount, description, username);
        transactionDao.persist(transaction);
    }
}
