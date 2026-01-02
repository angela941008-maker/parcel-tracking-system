package models;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//產生唯一追蹤單號
public class TrackingGenerator {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    //系統產生單號
    public static String generate() {
        return LocalDateTime.now().format(FORMATTER);
    }
}