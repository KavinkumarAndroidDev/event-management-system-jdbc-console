package com.ems.util;

import java.util.List;
import java.util.function.BiConsumer;

public class PaginationUtil {

    private static final int PAGE_SIZE = 10;

    // Splits a list into pages and prints one page at a time
    public static <T> void paginate(List<T> data, BiConsumer<List<T>, Integer> printer) {

        if (data == null || data.isEmpty()) {
            System.out.println("No data found.");
            return;
        }

        int totalItems = data.size();

        // If everything fits on one page, just print once
        if (totalItems <= PAGE_SIZE) {
            printer.accept(data, 1);
            return;
        }

        int totalPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);
        int currentPage = 1;

        while (true) {
            int start = (currentPage - 1) * PAGE_SIZE;
            int end = Math.min(start + PAGE_SIZE, totalItems);
            int startIndex = start + 1;

            System.out.println("\nPage " + currentPage + " of " + totalPages);
            printer.accept(data.subList(start, end), startIndex);

            System.out.println("\nN - Next | P - Previous | Q - Quit");
            String choice = InputValidationUtil
                    .readNonEmptyString(ScannerUtil.getScanner(), "Choice: ")
                    .toUpperCase();

            switch (choice) {
                case "N":
                    if (currentPage < totalPages) {
                        currentPage++;
                    } else {
                        System.out.println("Already at the last page.");
                    }
                    break;

                case "P":
                    if (currentPage > 1) {
                        currentPage--;
                    } else {
                        System.out.println("Already at the first page.");
                    }
                    break;

                case "Q":
                    return;

                default:
                    System.out.println("Invalid option. Enter N, P, or Q.");
            }
        }
    }
}