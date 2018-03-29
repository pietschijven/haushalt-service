package de.pschijven.haushaltservice.database;

import de.pschijven.haushaltservice.domain.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class TransactionDao {

    private static final String INSERT_TRANSACTION =
            "insert into transactions (id, amount, description, creation_date, username) values (?, ?, ?, ?, ?)";

    private static final String SELECT_TRANSACTIONS_IN_PERIOD =
            "select id, amount, description, creation_date, username from transactions " +
                    "where creation_date >= ? and creation_date < ? " +
                    "order by username, creation_date";

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

    public List<Transaction> findTransactionsInPeriod(LocalDate from, LocalDate to) {
        List<Map<String, Object>> result = jdbcTemplate
                .queryForList(SELECT_TRANSACTIONS_IN_PERIOD, convert(from), convert(to));

        if (result.isEmpty()) {
            return new ArrayList<>();
        }
        return result.stream().map(transactionRowMapper()).collect(Collectors.toList());
    }

    private Function<Map<String, Object>, Transaction> transactionRowMapper() {
        return m -> {
            Transaction transaction = new Transaction();
            transaction.setId((String) m.get("ID"));
            transaction.setAmount((BigDecimal) m.get("AMOUNT"));
            transaction.setUsername((String) m.get("USERNAME"));
            transaction.setCreationDate((java.util.Date) m.get("CREATION_DATE"));
            transaction.setDescription((String) m.get("DESCRIPTION"));
            return transaction;
        };
    }

    private Date convert(LocalDate localDate) {
        return Date.valueOf(localDate);
    }

}
