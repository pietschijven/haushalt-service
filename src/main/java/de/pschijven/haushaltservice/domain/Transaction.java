package de.pschijven.haushaltservice.domain;

import java.util.Date;

/**
 * Created by pschijven on 02.02.2018.
 */
public class Transaction {

    private int id;
    private long amount;
    private String description;
    private Date creationDate;
    private String username;

    public static Transaction create(final long amount, final String description, final String user) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setUsername(user);
        transaction.setCreationDate(new Date());
        return transaction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
