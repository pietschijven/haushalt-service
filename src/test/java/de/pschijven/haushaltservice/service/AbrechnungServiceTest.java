package de.pschijven.haushaltservice.service;

import com.google.common.collect.Lists;
import de.pschijven.haushaltservice.domain.Transaction;
import de.pschijven.haushaltservice.security.Auth0ManagementAPI;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class AbrechnungServiceTest {

    private List<Transaction> transactions;
    private Auth0ManagementAPI api;
    private TransactionService transactionService;
    private AbrechnungService service;


    @Before
    public void setUp() throws Exception {
        Transaction t1 = Transaction.create(BigDecimal.valueOf(2020, 2), "Test", "piet");
        Transaction t2 = Transaction.create(BigDecimal.valueOf(200, 2), "Test", "piet");
        Transaction t3 = Transaction.create(BigDecimal.valueOf(2090, 2), "Test", "lucia");
        this.transactions = Lists.newArrayList(t1, t2, t3);

        api = mock(Auth0ManagementAPI.class);
        transactionService = mock(TransactionService.class);
        service = new AbrechnungService(api, transactionService);
    }

    @Test
    public void testSalaryFractions() throws Exception {
        Map<String, BigDecimal> salaryMap = new HashMap<>();
        salaryMap.put("piet", new BigDecimal("1445.5"));
        salaryMap.put("lucia", new BigDecimal("1931.93"));

        Map<String, BigDecimal> salaryFractions = service.computeSalaryFractions(salaryMap);

        Map<String, BigDecimal> expected = new HashMap<>();
        expected.put("piet", new BigDecimal("0.43"));
        expected.put("lucia", new BigDecimal("0.57"));

        assertEquals(expected, salaryFractions);
    }
}