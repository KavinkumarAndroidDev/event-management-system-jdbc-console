package com.ems.util;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.ems.model.BookingDetail;
import com.ems.model.Category;
import com.ems.model.Event;
import com.ems.model.Offer;
import com.ems.model.Ticket;
import com.ems.model.User;
import com.ems.model.UserEventRegistration;
import com.ems.model.Venue;
import com.ems.service.EventService;

/**
 * Utility class that provides reusable console display helpers
 * for menu-driven application flows.
 *
 * Responsibilities:
 * - Format and render domain objects for console output
 * - Centralize presentation logic for menus
 * - Keep menu classes focused on navigation and user interaction
 *
 * Acts as a lightweight view layer for the CLI application.
 */
public class MenuHelper {
	
	private static final int TABLE_WIDTH = 110;
	private static final String SEPARATOR = "=".repeat(TABLE_WIDTH);
	private static final String SUB_SEPARATOR = "-".repeat(TABLE_WIDTH);

    /**
     * Service dependency used to fetch additional event-related details
     * required for display purposes.
     */
    private static EventService eventService = ApplicationUtil.eventService();

    /**
     * Displays detailed information for a list of events.
     *
     * @param events list of events to display
     */
    public static void printEventDetails(List<Event> events) {
        if (events == null || events.isEmpty()) {
            System.out.println("No events found.");
            return;
        }

        for (Event event : events) {
            String category = eventService.getCategory(event.getCategoryId()).getName();
            String venueName = eventService.getVenueName(event.getVenueId());
            String venueAddress = eventService.getVenueAddress(event.getVenueId());
            int totalAvailable = eventService.getAvailableTickets(event.getEventId());
            List<Ticket> tickets = eventService.getTicketTypes(event.getEventId());

            System.out.println(SEPARATOR);
            System.out.println("EVENT DETAILS");
            System.out.println(SEPARATOR);

            System.out.printf("Event ID        : %s%n", event.getEventId());
            System.out.printf("Title           : %s%n", event.getTitle());

            if (event.getDescription() != null) {
                System.out.printf("Description     : %s%n", event.getDescription());
            }

            System.out.printf("Category        : %s%n", category);
            System.out.printf(
                    "Duration        : %s to %s%n",
                    DateTimeUtil.formatDateTime(event.getStartDateTime()),
                    DateTimeUtil.formatDateTime(event.getEndDateTime())
            );
            System.out.printf("Total Tickets   : %d%n", totalAvailable);

            System.out.println();
            System.out.println("TICKET TYPES");
            System.out.println(SUB_SEPARATOR);

            for (Ticket t : tickets) {
                System.out.printf(
                        "%-20s ₹%-8.2f Available: %-5d%n",
                        t.getTicketType(),
                        t.getPrice(),
                        t.getAvailableQuantity()
                );
            }

            System.out.println();
            System.out.println("VENUE");
            System.out.println(SUB_SEPARATOR);
            System.out.println("Name    : " + venueName);
            System.out.println("Address : " + venueAddress);

            System.out.println(SEPARATOR);
        }
    }


    /**
     * Displays summarized information for a list of events.
     *
     * @param events list of events to display
     */
	public static void printEventSummaries(List<Event> events) {
	    if (events == null || events.isEmpty()) {
	        System.out.println("No events found.");
	        return;
	    }
	
	    System.out.println("\nAVAILABLE EVENTS");
	    System.out.println(SEPARATOR);
	
	    System.out.printf(
	            "%-5s %-30s %-20s %-20s %-10s%n",
	            "NO", "TITLE", "CATEGORY", "START DATE", "TICKETS"
	    );
	
	    System.out.println(SUB_SEPARATOR);
	
	    int index = 1;
	    for (Event event : events) {
	        String category = eventService.getCategory(event.getCategoryId()).getName();
	        int available = eventService.getAvailableTickets(event.getEventId());
	
	        System.out.printf(
	                "%-5d %-30s %-20s %-20s %-10d%n",
	                index++,
	                truncate(event.getTitle(), 29),
	                category,
	                DateTimeUtil.formatDateTime(event.getStartDateTime()),
	                available
	        );
	    }
	
	    System.out.println(SEPARATOR);
	}


    /**
     * Displays a formatted list of users.
     *
     * @param users list of users to display
     */
    public static void displayUsers(List<User> users) {
	    if (users == null || users.isEmpty()) {
	        System.out.println("No users found.");
	        return;
	    }
	
	    System.out.println(SEPARATOR);
	    System.out.printf(
	            "%-5s %-5s %-20s %-10s %-25s %-15s %-10s%n",
	            "NO", "ID", "NAME", "GENDER", "EMAIL", "PHONE", "STATUS"
	    );
	    System.out.println(SUB_SEPARATOR);
	
	    int index = 1;
	    for (User user : users) {
	        System.out.printf(
	                "%-5d %-5d %-20s %-10s %-25s %-15s %-10s%n",
	                index++,
	                user.getUserId(),
	                truncate(user.getFullName(), 19),
	                user.getGender(),
	                user.getEmail(),
	                user.getPhone() == null ? "-" : user.getPhone(),
	                user.getStatus()
	        );
	    }
	
	    System.out.println(SEPARATOR);
	}


    /**
     * Displays detailed information for a single event.
     *
     * @param event the event to display
     */
    public static void printEventDetails(Event event) {
        String category = eventService.getCategory(event.getCategoryId()).getName();
        String venueName = eventService.getVenueName(event.getVenueId());
        String venueAddress = eventService.getVenueAddress(event.getVenueId());
        int totalAvailable = eventService.getAvailableTickets(event.getEventId());
        List<Ticket> tickets = eventService.getTicketTypes(event.getEventId());

        System.out.println("\n==============================================");
        System.out.println("Event ID        : " + event.getEventId());
        System.out.println("Title           : " + event.getTitle());

        if (event.getDescription() != null) {
            System.out.println("Description     : " + event.getDescription());
        }

        System.out.println("Category        : " + category);
        System.out.println("Duration        : "
                + DateTimeUtil.formatDateTime(event.getStartDateTime())
                + " to "
                + DateTimeUtil.formatDateTime(event.getEndDateTime()));

        System.out.println("Total Tickets   : " + totalAvailable);

        System.out.println("\nTicket Types");
        System.out.println("----------------------------------------------");

        for (Ticket ticket : tickets) {
            System.out.println("• "
                    + ticket.getTicketType()
                    + " | Price: ₹"
                    + ticket.getPrice()
                    + " | Available: "
                    + ticket.getAvailableQuantity());
        }

        System.out.println("\nVenue");
        System.out.println("----------------------------------------------");
        System.out.println("Name            : " + venueName);
        System.out.println("Address         : " + venueAddress);

        System.out.println("==============================================");
    }

    /**
     * Displays a numbered list of available categories.
     *
     * @param categories list of categories to display
     */
    public static void displayCategories(List<Category> categories) {
        int index = 1;
        System.out.println("\nAvailable categories:");
        for (Category c : categories) {
            System.out.println(index + ". " + c.getName());
            index++;
        }
    }

    /**
     * Displays a numbered list of available venues.
     *
     * @param venues list of venues to display
     */
    public static void displayVenues(List<Venue> venues) {
        int index = 1;
        System.out.println("\nAvailable venues:");
        for (Venue venue : venues) {
            System.out.println(index + ". " + venue.getName());
            index++;
        }
    }

    /**
     * Displays available offers along with event and validity details.
     *
     * @param offers list of offers to display
     */
    public static void displayOffers(List<Offer> offers) {
        int index = 1;
        System.out.println("\nAvailable offers");

        for (Offer o : offers) {
            Event event = eventService.getEventById(o.getEventId());

            System.out.println(
                    index + ". "
                            + "Event: " + event.getTitle()
                            + "\nOffer code: " + o.getCode()
                            + "\nDiscount: " + o.getDiscountPercentage() + "%"
                            + "\nValidity: " + DateTimeUtil.formatDateTime(o.getValidFrom())
                            + " to " + DateTimeUtil.formatDateTime(o.getValidTo())
            );

            System.out.println("----------------------------------------------");
            index++;
        }
    }

    /**
     * Displays summarized ticket information for an event.
     *
     * @param tickets list of ticket types to display
     */
	public static void printTicketSummaries(List<Ticket> tickets) {
	    if (tickets == null || tickets.isEmpty()) {
	        System.out.println("No ticket types found.");
	        return;
	    }
	
	    System.out.println("\nTICKET SUMMARY");
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
     * Displays booking details for a user's registrations.
     *
     * @param bookingDetails list of booking details to display
     */
	public static void printBookingDetails(List<BookingDetail> bookingDetails) {
	    if (bookingDetails == null || bookingDetails.isEmpty()) {
	        System.out.println("No bookings found.");
	        return;
	    }
	
	    System.out.println("\nBOOKING DETAILS");
	    System.out.println(SEPARATOR);
	
	    for (BookingDetail b : bookingDetails) {
	        System.out.println(SUB_SEPARATOR);
	        System.out.println("Event   : " + b.getEventName());
	        System.out.println("Venue   : " + b.getVenueName() + " (" + b.getCity() + ")");
	        System.out.println("Tickets : " + b.getTicketType() + " x " + b.getQuantity());
	        System.out.println("Total   : ₹" + b.getTotalCost());
	    }
	
	    System.out.println(SEPARATOR);
	}


    /**
     * Displays a formatted list of user event registrations.
     *
     * @param events list of user event registrations
     */
    public static void printEventsList(List<UserEventRegistration> events) {
        System.out.println("\n--------------------------------------------------------------------------------------");
        System.out.printf(
                "%-3s %-22s %-12s %-12s %-18s %-7s%n",
                "No", "Title", "Category", "Ticket Type", "Date & Time", "Tickets"
        );
        System.out.println("--------------------------------------------------------------------------------------");

        int i = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");

        for (UserEventRegistration r : events) {
            String title = r.getTitle();
            String displayTitle = (title != null && title.length() > 22)
                    ? title.substring(0, 22 - 3) + "..."
                    : title;

            String category = r.getCategory();
            String displayCategory = (category != null && category.length() > 12)
                    ? category.substring(0, 12 - 3) + "..."
                    : category;

            String ticketType = r.getTicketType();
            String displayTicketType =
                    (ticketType != null && ticketType.length() > 12)
                            ? ticketType.substring(0, 9) + "..."
                            : ticketType;

            System.out.printf(
                    "%-3d %-22s %-12s %-12s %-18s %-7d%n",
                    i++,
                    displayTitle,
                    displayCategory,
                    displayTicketType,
                    r.getStartDateTime().format(formatter),
                    r.getTicketsPurchased()
            );
        }

        System.out.println("--------------------------------------------------------------------------------------");
    }

    /**
     * Prompts the user to select an option from a numbered list.
     *
     * @param max maximum valid option
     * @param prompt prompt message to display
     * @return validated user selection
     */
    public static int selectFromList(int max, String prompt) {
        int choice;

        while (true) {
            choice = InputValidationUtil.readInt(
                    ScannerUtil.getScanner(),
                    prompt + " (1-" + max + "): "
            );

            if (choice >= 1 && choice <= max) {
                return choice;
            }

            System.out.println("Invalid selection. Please try again.");
        }
    }
    private static String truncate(String value, int max) {
        if (value == null || value.length() <= max) {
            return value;
        }
        return value.substring(0, max - 3) + "...";
    }
}
