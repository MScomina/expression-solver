package me.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerUtils {
    /**
     * Logs a message to the console.
     * <p>
     * Format: [yyyy-MM-dd HH:mm:ss] [extra] [extra] message
     * </p>
     *
     * @param message The message to log on the stdout.
     * @param extra   Extra information (for example from where the message was logged).
     */
    public static void log(String message, String... extra) {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StringBuilder formattedTime = new StringBuilder("[" + formatter.format(time) + "] ");
        for (String s : extra) {
            formattedTime.append("[").append(s).append("] ");
        }
        System.out.println(formattedTime + message);
    }
}
