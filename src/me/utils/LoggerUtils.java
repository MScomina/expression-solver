package me.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerUtils {
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
