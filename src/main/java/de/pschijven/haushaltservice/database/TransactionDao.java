package de.pschijven.haushaltservice.database;

import de.pschijven.haushaltservice.domain.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class TransactionDao {

    private static final String INSERT_TRANSACTION =
            "insert into transactions (id, amount, description, creation_date, username) values (?, ?, ?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;

    public TransactionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void persist(final Transaction transaction) {
        jdbcTemplate.update(INSERT_TRANSACTION,
                UUID.randomUUID().toString(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getCreationDate(),
                transaction.getUsername());
    }
}
