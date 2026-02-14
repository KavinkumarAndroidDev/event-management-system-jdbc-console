package com.ems.util;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.ems.model.Category;
import com.ems.model.Event;
import com.ems.model.EventRegistrationReport;
import com.ems.model.EventRevenueReport;
import com.ems.model.Offer;
import com.ems.model.OrganizerEventSummary;
import com.ems.model.SystemLog;
import com.ems.model.Ticket;
import com.ems.model.Venue;
import com.ems.service.EventService;

public class AdminMenuHelper {

    private static final EventService eventService = ApplicationUtil.eventService();
    
    private static final int TABLE_WIDTH = 110;
    private static final String SEPARATOR = "=".repeat(TABLE_WIDTH);
    private static final String SUB_SEPARATOR = "-".repeat(TABLE_WIDTH);
    
    /**
     * Helper function to print the event along with the status of the event
     * 
     * @param events list of events to be displayed
     */
    public static void printAllEventsWithStatus(List<Event> events) {
        if (events == null || events.isEmpty()) {
            System.out.println("No events found.");
            return;
        }

        System.out.println("\nAVAILABLE EVENTS");
        System.out.println(SEPARATOR);

        System.out.printf(
                "%-5s %-30s %-20s %-20s %-10s %-10s%n",
                "NO", "TITLE", "CATEGORY", "START DATE", "TICKETS", "STATUS"
        );

        System.out.println(SUB_SEPARATOR);

        int index = 1;
        for (Event event : events) {
            String category = eventService
                    .getCategory(event.getCategoryId())
                    .getName();

            int available = eventService
                    .getAvailableTickets(event.getEventId());

            System.out.printf(
                    "%-5d %-30s %-20s %-20s %-10d %-10s%n",
                    index++,
                    truncate(event.getTitle(), 29),
                    category,
                    DateTimeUtil.formatDateTime(event.getStartDateTime()),
                    available,
                    event.getStatus()
            );
        }

        System.out.println(SEPARATOR);
    }
    
    /**
     * print the ticket details in the formatted way
     * 
     * @param tickets list of tickets
     */
    public static void printTicketDetails(List<Ticket> tickets) {
        if (tickets == null || tickets.isEmpty()) {
            System.out.println("No ticket types found.");
            return;
        }

        System.out.println("\nTICKET DETAILS");
        System.out.println(SEPARATOR);

        System.out.printf(
                "%-5s %-20s %-10s %-15s%n",
                "NO", "TYPE", "PRICE", "CAPACITY"
        );

        System.out.println(SUB_SEPARATOR);

        int index = 1;
        for (Ticket t : tickets) {
            System.out.printf(
                    "%-5d %-20s ₹%-9.2f %-7d/%-7d%n",
                    index++,
                    t.getTicketType(),
                    t.getPrice(),
                    t.getAvailableQuantity(),
                    t.getTotalQuantity()
            );
        }

        System.out.println(SEPARATOR);
    }
    
    /**
     * To display the event summary of the organizer
     * 
     * @param organizerEventSummary list of organizer event summary
     */
    public static void printOrganizerEventSummary(List<OrganizerEventSummary> summaries) {
        if (summaries == null || summaries.isEmpty()) {
            System.out.println("No organizer data found.");
            return;
        }
        
        List<OrganizerEventSummary> orderedList =
                summaries.stream()
                        .sorted(
                                Comparator
                                        .comparingInt(OrganizerEventSummary::getStatusPriority)
                                        .thenComparing(OrganizerEventSummary::getTitle)
                        )
                        .toList();

        System.out.println("\nORGANIZER EVENT SUMMARY");
        System.out.println(SEPARATOR);

        System.out.printf(
                "%-5s %-45s %-12s %-15s %-15s%n",
                "NO", "EVENT TITLE", "STATUS", "BOOKED", "TOTAL"
        );

        System.out.println(SUB_SEPARATOR);
        int index = 1;
        for (OrganizerEventSummary s : orderedList) {
            System.out.printf(
                    "%-5d %-45s %-12s %-15d %-15d%n",
                    index++,
                    truncate(s.getTitle(), 44),
                    s.getStatus(),
                    s.getBookedTickets(),
                    s.getTotalTickets()
            );
        }

        System.out.println(SEPARATOR);
    }
    
    /**
     * Print the summary of sold and available tickets
     * 
     * @param tickets
     */
    public static void printTicketCapacitySummary(List<Ticket> tickets) {
        int total = 0;
        int available = 0;

        for (Ticket t : tickets) {
            total += t.getTotalQuantity();
            available += t.getAvailableQuantity();
        }

        System.out.println("\nEVENT CAPACITY SUMMARY");
        System.out.println(SEPARATOR);
        System.out.println("Total Tickets     : " + total);
        System.out.println("Available Tickets : " + available);
        System.out.println(SEPARATOR);
    }

    /**
     * Print the venue details in table format
     * 
     * @param venues
     */
    public static void printVenues(List<Venue> venues) {
        if (venues == null || venues.isEmpty()) {
            System.out.println("No venues found.");
            return;
        }

        System.out.println("\nAVAILABLE VENUES");
        System.out.println(SEPARATOR);

        System.out.printf(
                "%-5s %-22s %-18s %-15s %-12s %-10s %-12s%n",
                "NO", "VENUE NAME", "STREET", "CITY", "STATE", "CAPACITY", "STATUS"
        );

        System.out.println(SUB_SEPARATOR);

        int index = 1;
        for (Venue v : venues) {

            String status = v.getStatus() ? "ACTIVE" : "INACTIVE";

            System.out.printf(
                    "%-5d %-22s %-18s %-15s %-12s %-10d %-12s%n",
                    index++,
                    truncate(v.getName(), 21),
                    truncate(v.getStreet(), 17),
                    v.getCity(),
                    v.getState(),
                    v.getMaxCapacity(),
                    status
            );
        }

        System.out.println(SEPARATOR);
    }

    /**
     * Print a venue details
     * 
     * @param v
     */
    public static void printVenueDetails(Venue v) {
        if (v == null) {
            System.out.println("Venue not found.");
            return;
        }

        System.out.println("\nVENUE DETAILS");
        System.out.println(SEPARATOR);

        System.out.println("Name        : " + v.getName());
        System.out.println("Street      : " + v.getStreet());
        System.out.println("City        : " + v.getCity());
        System.out.println("State       : " + v.getState());
        System.out.println("Pincode     : " + v.getPincode());
        System.out.println("Capacity    : " + v.getMaxCapacity());

        System.out.println(SEPARATOR);
    }
    
    /**
     * Print the event registration report
     * 
     * @param reports list of event registration report
     */
    public static void printEventRegistrationReport(List<EventRegistrationReport> reports) {
        if (reports == null || reports.isEmpty()) {
            System.out.println("No registration records found.");
            return;
        }

        System.out.println("\nEVENT REGISTRATION REPORT");
        System.out.println(SEPARATOR);

        System.out.printf(
                "%-5s %-30s %-20s %-15s %-10s %-20s%n",
                "NO", "EVENT TITLE", "USER", "TICKET TYPE", "QTY", "REGISTERED ON"
        );

        System.out.println(SUB_SEPARATOR);

        int index = 1;
        for (EventRegistrationReport r : reports) {
            System.out.printf(
                    "%-5d %-30s %-20s %-15s %-10d %-20s%n",
                    index++,
                    truncate(r.getEventTitle(), 29),
                    truncate(r.getUserName(), 19),
                    truncate(r.getTicketType(), 14),
                    r.getQuantity(),
                    DateTimeUtil.formatDateTime(r.getRegistrationDate())
            );
        }

        System.out.println(SEPARATOR);
    }
    
    /**
     * Print offer codes' usage report
     * 
     * @param report list of offers and its usage
     */
    public static void printOfferUsageReport(Map<String, Integer> report) {
        if (report == null || report.isEmpty()) {
            System.out.println("No offer usage data found.");
            return;
        }

        System.out.println("\nOFFER USAGE REPORT");
        System.out.println(SEPARATOR);

        System.out.printf(
                "%-5s %-20s %-15s%n",
                "NO", "OFFER CODE", "USAGE COUNT"
        );

        System.out.println(SUB_SEPARATOR);

        int index = 1;
        for (Map.Entry<String, Integer> entry : report.entrySet()) {
            System.out.printf(
                    "%-5d %-20s %-15d%n",
                    index++,
                    truncate(entry.getKey(), 19),
                    entry.getValue()
            );
        }

        System.out.println(SEPARATOR);
    }

    /**
     * Print all offers
     * 
     * @param offers
     */
    public static void printOffers(List<Offer> offers) {
        if (offers == null || offers.isEmpty()) {
            System.out.println("No offers found.");
            return;
        }

        System.out.println("\nOFFERS REPORT");
        System.out.println(SEPARATOR);

        System.out.printf(
                "%-5s %-10s %-15s %-12s %-20s %-20s %-12s%n",
                "NO", "OFFER ID", "CODE", "DISCOUNT", "VALID FROM", "VALID TO", "STATUS"
        );

        System.out.println(SUB_SEPARATOR);

        LocalDateTime now = LocalDateTime.now();
        int index = 1;

        for (Offer o : offers) {

            String discount = o.getDiscountPercentage() != null
                    ? o.getDiscountPercentage() + "%"
                    : "NA";

            String status;

            if (o.getValidFrom() != null && o.getValidTo() != null) {

                if (now.isBefore(o.getValidFrom())) {
                    status = "UPCOMING";
                } else if (!now.isAfter(o.getValidTo())) {
                    status = "ACTIVE";
                } else {
                    status = "EXPIRED";
                }

            } else {
                status = "UNKNOWN";
            }


            System.out.printf(
                    "%-5d %-10d %-15s %-12s %-20s %-20s %-12s%n",
                    index++,
                    o.getOfferId(),
                    truncate(o.getCode(), 14),
                    discount,
                    DateTimeUtil.formatDateTime(o.getValidFrom()),
                    DateTimeUtil.formatDateTime(o.getValidTo()),
                    status
            );
        }

        System.out.println(SEPARATOR);
    }
    
    /**
     * Print event revenue reports
     * 
     * @param reports ,list of event revenue report
     */
    public static void printEventRevenueReport(List<EventRevenueReport> reports) {

        if (reports == null || reports.isEmpty()) {
            System.out.println("No revenue data available.");
            return;
        }

        System.out.println("\nEVENT WISE REVENUE REPORT");
        System.out.println(SEPARATOR);

        System.out.printf(
                "%-5s %-35s %-12s %-12s %-15s %-15s%n",
                "NO",
                "EVENT TITLE",
                "REGS",
                "TICKETS",
                "REVENUE",
                "AVG PRICE"
        );

        System.out.println(SUB_SEPARATOR);

        int index = 1;
        double grandTotal = 0;

        for (EventRevenueReport report : reports) {

            grandTotal += report.getTotalRevenue();

            System.out.printf(
                    "%-5d %-35s %-12d %-12d ₹%-14.2f ₹%-14.2f%n",
                    index++,
                    truncate(report.getEventTitle(), 34),
                    report.getTotalRegistrations(),
                    report.getTicketsSold(),
                    report.getTotalRevenue(),
                    report.getAvgTicketPrice()
            );
        }

        System.out.println(SEPARATOR);
        System.out.printf("TOTAL REVENUE: ₹%.2f%n", grandTotal);
    }
    
    /**
     * Print the system generated logs
     * 
     * @param logs
     */
	public static void printSystemLogs(List<SystemLog> logs) {
	
	    if (logs == null || logs.isEmpty()) {
	        System.out.println("No logs found.");
	        return;
	    }
	
	    System.out.println("\nSYSTEM LOGS");
	    System.out.println(SEPARATOR);
	
	    System.out.printf(
	        "%-5s %-20s %-10s %-22s %-15s %-40s%n",
	        "NO", "TIME", "USER", "ACTION", "ENTITY", "MESSAGE"
	    );
	
	    System.out.println(SUB_SEPARATOR);
	
	    int index = 1;
	    for (SystemLog log : logs) {
	
	        System.out.printf(
	            "%-5d %-20s %-10s %-22s %-15s %-40s%n",
	            index++,
	            DateTimeUtil.formatDateTime(log.getCreatedAt()),
	            log.getUserId() == null ? "SYSTEM" : log.getUserId().toString(),
	            truncate(log.getAction(), 21),
	            truncate(log.getEntity(), 14),
	            truncate(log.getMessage(), 39)
	        );
	    }
	
	    System.out.println(SEPARATOR);
	}

	/**
	 * Print categories and its status
	 * 
	 * @param categories
	 */
	public static void printCategories(List<Category> categories) {

	    if (categories == null || categories.isEmpty()) {
	        System.out.println("No categories found.");
	        return;
	    }

	    System.out.println("\nAVAILABLE CATEGORIES");
	    System.out.println(SEPARATOR);

	    System.out.printf(
	            "%-5s %-30s %-12s%n",
	            "NO", "CATEGORY NAME", "STATUS"
	    );

	    System.out.println(SUB_SEPARATOR);

	    int index = 1;
	    for (Category c : categories) {

	        String status = c.getIsActive() == 1 ? "ACTIVE" : "INACTIVE";

	        System.out.printf(
	                "%-5d %-30s %-12s%n",
	                index++,
	                truncate(c.getName(), 29),
	                status
	        );
	    }

	    System.out.println(SEPARATOR);
	}
	
	/* ===================== HELPER FUNCTIONS ===================== */
    public static List<Offer> filterActiveOffers(List<Offer> offers) {
        LocalDateTime now = LocalDateTime.now();
        return offers.stream()
                .filter(o -> o.getValidTo() != null && o.getValidTo().isAfter(now))
                .toList();
    }

    public static List<Offer> filterExpiredOffers(List<Offer> offers) {
        LocalDateTime now = LocalDateTime.now();
        return offers.stream()
                .filter(o -> o.getEventId() != 0
                        && o.getValidTo() != null
                        && o.getValidTo().isBefore(now))
                .toList();
    }
    /**
     * Truncate the given string, followed by three trailing dots
     * 
     * @param value
     * @param max
     * @return Truncated string
     */
    private static String truncate(String value, int max) {
        if (value == null || value.length() <= max) {
            return value;
        }
        return value.substring(0, max - 3) + "...";
    }
}
