package de.pschijven.haushaltservice.reporting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import de.pschijven.haushaltservice.configuration.ReportProperties;
import de.pschijven.haushaltservice.domain.Transaction;
import de.pschijven.haushaltservice.service.TransactionService;
import de.pschijven.haushaltservice.util.DateProvider;
import net.lingala.zip4j.io.ZipOutputStream;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.springframework.stereotype.Component;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Constructs the montly report of all transactions in the current month.
 * The output is a CSV file, containing all relevant transactions.
 */
@Component
public class Report {

    private final TransactionService service;
    private final DateProvider dateProvider;
    private final ReportProperties properties;

    public Report(TransactionService service, DateProvider dateProvider, ReportProperties properties) {
        this.service = service;
        this.dateProvider = dateProvider;
        this.properties = properties;
    }

    public InputStream createReport() throws Exception {
        List<Transaction> transactions = service.transactionsInCurrentMonth();
        byte[] csv = createCSV(transactions);
        return createZip(csv);
    }

    private byte[] createCSV(final List<Transaction> transactions) throws JsonProcessingException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(Transaction.class).withHeader();
        return mapper.writer(schema).writeValueAsBytes(transactions);
    }

    private InputStream createZip(byte[] csv) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
        ZipOutputStream outputStream = new ZipOutputStream(bufferedOutputStream);

        ZipParameters parameters = new ZipParameters();

        if (properties.getZipPassword() != null) {
            parameters.setPassword(properties.getZipPassword());
        }
        parameters.setFileNameInZip(filenameInZip());
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        parameters.setEncryptFiles(true);
        parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
        parameters.setSourceExternalStream(true);

        outputStream.putNextEntry(null, parameters);
        outputStream.write(csv);
        outputStream.closeEntry();
        outputStream.finish();
        outputStream.close();

        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    private String filenameInZip() {
        LocalDate now = dateProvider.currentLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
        return "transactions-" + formatter.format(now) + ".csv";
    }
}
