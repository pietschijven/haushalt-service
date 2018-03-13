package de.pschijven.haushaltservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.pschijven.haushaltservice.database.TransactionDao;
import de.pschijven.haushaltservice.domain.Transaction;
import de.pschijven.haushaltservice.util.DateProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    private TransactionService service;
    private TransactionDao dao;
    private DateProvider dateProvider;

    @Before
    public void setUp() throws Exception {
        dao = mock(TransactionDao.class);
        dateProvider = mock(DateProvider.class);
        service = new TransactionService(dao, dateProvider);

        when(dateProvider.currentLocalDate()).thenReturn(LocalDate.of(2015, 4, 6));
    }

    @Test
    public void testTimeRangeFrom() throws Exception {
        service.transactionsInCurrentMonth();

        ArgumentCaptor<LocalDate> captor = localDateAC();
        verify(dao).findTransactionsInPeriod(captor.capture(), any(LocalDate.class));
        assertEquals(LocalDate.of(2015, 4, 1), captor.getValue());
    }

    @Test
    public void testTimeRangeTo() throws Exception {
        service.transactionsInCurrentMonth();

        ArgumentCaptor<LocalDate> captor = localDateAC();
        verify(dao).findTransactionsInPeriod(any(LocalDate.class), captor.capture());
        assertEquals(LocalDate.of(2015, 5, 1), captor.getValue());
    }

    private ArgumentCaptor<LocalDate> localDateAC() {
        return ArgumentCaptor.forClass(LocalDate.class);
    }

    @Test
    public void testToCSV() throws JsonProcessingException {
        List<Transaction> testList = new ArrayList<>();
        Transaction transaction1 =  Transaction.create(new BigDecimal(10.10).setScale(2,BigDecimal.ROUND_HALF_DOWN),"Telefon", "Piet@email.com");
        Transaction transaction2 = Transaction.create(new BigDecimal(12),"Kopfhörer", "Lucia@email.com");
        testList.add(transaction1);
        testList.add(transaction2);
        when(dao.findTransactionsInPeriod(any(LocalDate.class),any(LocalDate.class))).thenReturn(testList);
        String CSVString = service.toCSV();
        String testString = "amount,description,username,creationDate\n" +
                "10.10,Telefon,Piet@email.com,11.03.2018\n" +
                "12,Kopfhörer,Lucia@email.com,11.03.2018\n";
        assertEquals(testString,CSVString);
        System.out.println(CSVString);
    }
}