package org.example.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class DateTimeInput {

    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public static LocalDateTime readLocalDateTime(Scanner scanner, String msg) {
        while (true) {
            System.out.print(msg);
            String input = scanner.nextLine();

            try {
                return LocalDateTime.parse(input, FORMAT);
            } catch (DateTimeParseException e) {
                System.out.println("Неверный формат! Используй dd-MM-yyyy HH:mm");
            }
        }
    }
}