package de.pschijven.haushaltservice.service;

import de.pschijven.haushaltservice.database.TransactionDao;
import de.pschijven.haushaltservice.util.DateProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;

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
}