package com.ems.util;

import java.util.List;

import com.ems.model.BookingDetail;
import com.ems.model.Category;
import com.ems.model.Event;
import com.ems.model.Notification;
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
	/**
	 * Common table separators for console layout consistency
	 */
	private static final int TABLE_WIDTH = 110;
	private static final String SEPARATOR = "=".repeat(TABLE_WIDTH);
	private static final String SUB_SEPARATOR = "-".repeat(TABLE_WIDTH);

    /**
     * Service dependency used to fetch additional event-related details
     * required for display purposes.
     */
    private static EventService eventService = ApplicationUtil.eventService();
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
        //System.out.println("Event ID        : " + event.getEventId());
        System.out.println("Title           : " + event.getTitle());

        if (event.getDescription() != null) {
            System.out.println("Description     : " + event.getDescription());
        }

        System.out.println("Category        : " + category);
        System.out.println("Duration        : "
                + DateTimeUtil.formatForDisplay(event.getStartDateTime())
                + " to "
                + DateTimeUtil.formatForDisplay(event.getEndDateTime()));

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
	                DateTimeUtil.formatForDisplay(event.getStartDateTime()),
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
     * Displays a numbered list of available categories.
     *
     * @param categories list of categories to display
     */
    public static void displayCategories(List<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            System.out.println("No categories found.");
            return;
        }

        System.out.println();
        System.out.println("Select a Category");
        System.out.println(SUB_SEPARATOR);

        int index = 1;
        for (Category c : categories) {
            System.out.printf("  %2d) %s%n", index++, c.getName());
        }

        System.out.println(SUB_SEPARATOR);
    }

    


    /**
     * Displays a numbered list of available venues.
     *
     * @param venues list of venues to display
     */
    public static void displayVenues(List<Venue> venues) {
        if (venues == null || venues.isEmpty()) {
            System.out.println("No venues found.");
            return;
        }

        System.out.println();
        System.out.println("AVAILABLE VENUES");
        System.out.println(SEPARATOR);

        System.out.printf(
            "%-5s %-30s %-30s%n",
            "NO", "VENUE NAME", "CITY"
        );

        System.out.println(SUB_SEPARATOR);

        int index = 1;
        for (Venue venue : venues) {
            System.out.printf(
                "%-5d %-30s %-30s%n",
                index++,
                truncate(venue.getName(), 29),
                venue.getCity()
            );
        }

        System.out.println(SEPARATOR);
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
        System.out.println(SEPARATOR);

        System.out.printf(
                "%-3s %-22s %-12s %-12s %-18s %-7s%n",
                "No", "Title", "Category", "Ticket Type", "Date & Time", "Tickets"
        );
        System.out.println(SUB_SEPARATOR);

        int i = 1;

        for (UserEventRegistration r : events) {

            System.out.printf(
                    "%-3d %-22s %-12s %-12s %-18s %-7d%n",
                    i++,
                    truncate(r.getTitle(), 22),
                    truncate(r.getCategory(),12),
                    truncate(r.getTicketType(), 9),
                    DateTimeUtil.formatForDisplay(r.getStartDateTime()),
                    r.getTicketsPurchased()
            );
        }
        System.out.println(SEPARATOR);
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
    
    /**
     * Displays a formatted list of notifications.
     *
     * @param notifications list of notifications to display
     */
    public static void displayNotifications(List<Notification> notifications) {
        if (notifications == null || notifications.isEmpty()) {
            System.out.println("\nNo notifications.");
            return;
        }

        System.out.println();
        System.out.println("NOTIFICATIONS");
        System.out.println(SEPARATOR);

        int index = 1;
        for (Notification n : notifications) {
            System.out.println(SUB_SEPARATOR);
            System.out.printf(
                    "%2d) [%s] %s%n",
                    index++,
                    n.getType(),
                    n.getMessage()
            );
            System.out.println("    Time : " + DateTimeUtil.formatForDisplay(n.getCreatedAt()));
        }

        System.out.println(SEPARATOR);
    }

    
    /**
     * Helper function used to truncate the long string and 
     * add repeated dots to notify that the original string is lengthier than the display string
     * 
     * @param value Input string to be truncated
     * @param max maximum display length of the string
     * @return
     */
    private static String truncate(String value, int max) {
        if (value == null || value.length() <= max) {
            return value;
        }
        return value.substring(0, max - 3) + "...";
    }
}
