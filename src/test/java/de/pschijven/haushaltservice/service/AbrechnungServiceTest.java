package de.pschijven.haushaltservice.service;

import com.google.common.collect.Lists;
import de.pschijven.haushaltservice.domain.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class AbrechnungServiceTest {

    private List<Transaction> transactions;

    @Before
    public void setUp() throws Exception {
        Transaction t1 = Transaction.create(BigDecimal.valueOf(2020, 2), "Test", "piet");
        Transaction t2 = Transaction.create(BigDecimal.valueOf(200, 2), "Test", "piet");
        Transaction t3 = Transaction.create(BigDecimal.valueOf(2090, 2), "Test", "lucia");
        this.transactions = Lists.newArrayList(t1, t2, t3);
    }

    @Test
    public void name() throws Exception {
        AbrechnungService service = new AbrechnungService(null, null);
        Map<String, BigDecimal> subtotals = service.computeSubTotals(transactions);
    }
}