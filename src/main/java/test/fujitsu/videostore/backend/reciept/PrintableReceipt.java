package test.fujitsu.videostore.backend.reciept;

import java.time.format.DateTimeFormatter;

/**
 * Printer interface
 */
public interface PrintableReceipt {

    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-YY");

    String print();

}
