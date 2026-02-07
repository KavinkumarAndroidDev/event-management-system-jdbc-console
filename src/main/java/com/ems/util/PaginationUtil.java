package com.ems.util;

import java.util.List;

public class PaginationUtil {

    private static final int PAGE_SIZE = 10;

    public static <T> void paginate(
            List<T> data,
            java.util.function.Consumer<List<T>> printer
    ) {
        if (data == null || data.isEmpty()) {
            System.out.println("No data found.");
            return;
        }

        int totalItems = data.size();
        int totalPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);
        int currentPage = 1;

        while (true) {

            int start = (currentPage - 1) * PAGE_SIZE;
            int end = Math.min(start + PAGE_SIZE, totalItems);

            System.out.println("\nPage " + currentPage + " of " + totalPages);

            printer.accept(data.subList(start, end));

            System.out.println("\nN - Next | P - Previous | Q - Quit");

            String choice = InputValidationUtil.readNonEmptyString(
                    ScannerUtil.getScanner(),
                    "Choice: "
            ).toUpperCase();

            if ("N".equals(choice)) {
                if (currentPage < totalPages) currentPage++;
                else System.out.println("Already at last page.");
            } else if ("P".equals(choice)) {
                if (currentPage > 1) currentPage--;
                else System.out.println("Already at first page.");
            } else if ("Q".equals(choice)) {
                break;
            } else {
                System.out.println("Invalid option.");
            }
        }
    }
}
