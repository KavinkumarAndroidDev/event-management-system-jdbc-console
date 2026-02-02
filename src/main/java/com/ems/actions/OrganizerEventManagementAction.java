package com.ems.actions;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.ems.enums.EventStatus;
import com.ems.model.Category;
import com.ems.model.Event;
import com.ems.model.Ticket;
import com.ems.model.Venue;
import com.ems.service.EventService;
import com.ems.service.OrganizerService;
import com.ems.util.ApplicationUtil;
import com.ems.util.DateTimeUtil;
import com.ems.util.InputValidationUtil;
import com.ems.util.MenuHelper;
import com.ems.util.ScannerUtil;

/**
 * Action class responsible for organizer event management operations.
 * Handles user interaction and validation logic only.
 * Delegates business logic to OrganizerService and EventService.
 */
public class OrganizerEventManagementAction {

    private final OrganizerService organizerService;
    private final EventService eventService;

    public OrganizerEventManagementAction() {
        this.organizerService = ApplicationUtil.organizerService();
        this.eventService = ApplicationUtil.eventService();
    }

    /* ===================== Event creation ===================== */
    /**
     * Handles full event creation workflow for an organizer.
     * Includes category selection, venue selection, date validation,
     * availability checks, and capacity validation.
     *
     * @param userId the organizer ID
     */
    public void createEvent(int userId) {
    	
        String title = InputValidationUtil.readNonEmptyString(
            ScannerUtil.getScanner(),
            "Enter event title (15 to 150 characters):\n"
        );
        
        while(title.length() > 150 || title.length() < 15) {
        	title = InputValidationUtil.readNonEmptyString(
                    ScannerUtil.getScanner(),
                    "Title must be between 15 and 150 characters. Try again:\n"
                );
        }
        String description = InputValidationUtil.readNonEmptyString(
            ScannerUtil.getScanner(),
            "\nEnter event description (minimum 150 characters):\n"
        );
        while(description.length() > 16383 || description.length() < 150) {
        	description = InputValidationUtil.readNonEmptyString(
                    ScannerUtil.getScanner(),
                    "Description must be at least 150 characters. Try again:\n");
        }
        System.out.println();
        List<Category> categories = getAllCategory();
        if (categories.isEmpty()) {
            System.out.println("There are no available category!");
            return;
        }
        int defaultIndex = 1;
        for (Category category : categories) {
            System.out.println(defaultIndex + ". " + category.getName());
            defaultIndex++;
        }

        int choice = InputValidationUtil.readInt(
            ScannerUtil.getScanner(),
            "Select category number: "
        );

        while (choice < 1 || choice > categories.size()) {
            choice = InputValidationUtil.readInt(
                ScannerUtil.getScanner(),
                "PInvalid choice. Please select a number from the list:\n"
            );
        }

        Category selectedCategory = categories.get(choice - 1);
        int categoryId = selectedCategory.getCategoryId();
        System.out.println();
        List<Venue> venues = getAllVenues();
        if (venues.isEmpty()) {
            System.out.println("No venues available at the moment.");
            return;
        }
        defaultIndex = 1;
        for (Venue venue : venues) {
            System.out.println(
                defaultIndex + ". " + venue.getName() + ", \n"
                + getVenueAddress(venue.getVenueId()) + "\n"
            );
            defaultIndex++;
        }

        int venueChoice = InputValidationUtil.readInt(
            ScannerUtil.getScanner(),
            "Select venue number: "
        );

        while (venueChoice < 1 || venueChoice > venues.size()) {
            venueChoice = InputValidationUtil.readInt(
                ScannerUtil.getScanner(),
                "Please enter a valid number from the list.: "
            );
        }

        Venue selectedVenue = venues.get(venueChoice - 1);
        int venueId = selectedVenue.getVenueId();

        LocalDateTime startTime = null;
        while (startTime == null) {
            String input = InputValidationUtil.readString(
                ScannerUtil.getScanner(),
                "Enter event start date and time (dd-MM-yyyy HH:mm):\n"
            );
            startTime = DateTimeUtil.parseLocalDateTime(input);
            if (startTime == null) {
                System.out.println("Invalid start date time. Please try again.");
            }
        }

        LocalDateTime endTime = null;
        while (endTime == null) {
            String input = InputValidationUtil.readString(
                ScannerUtil.getScanner(),
                "Enter the event end date and time (dd-MM-yyyy HH:mm): \n"
            );
            endTime = DateTimeUtil.parseLocalDateTime(input);
            if (endTime == null || endTime.isBefore(startTime)) {
                System.out.println("End date and time must be after the start time. Try again:\n");
                endTime = null;
            }
        }

        while (!isVenueAvailable(venueId, startTime, endTime)) {
            System.out.println(
                "Selected venue is not available for this time.\n\n"
                + "1. Choose a different venue\n"
                + "2. Change event date or time\n"
                + "3. Cancel event creation\n\n>"
            );

            int c = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");

            switch (c) {
            case 1:
                venues = getAllVenues();
                defaultIndex = 1;
                for (Venue venue : venues) {
                	System.out.println(
                            defaultIndex + ". " + venue.getName()
                            + getVenueAddress(venue.getVenueId())
                        );
                        defaultIndex++;
                }
                venueChoice = InputValidationUtil.readInt(
                    ScannerUtil.getScanner(),
                    "Select venue number: "
                );
                selectedVenue = venues.get(venueChoice - 1);
                venueId = selectedVenue.getVenueId();
                break;

            case 2:
                startTime = null;
                endTime = null;
                break;

            case 3:
                return;

            default:
                System.out.println("Enter the valid option!");
            }
        }

        int eventCapacity = InputValidationUtil.readInt(
            ScannerUtil.getScanner(),
            "Maximum capacity of the selected venue: "
            + selectedVenue.getMaxCapacity()
            + "\nEnter the event capacity:"
        );

        while (eventCapacity <= 0 || eventCapacity > selectedVenue.getMaxCapacity()) {
            eventCapacity = InputValidationUtil.readInt(
                ScannerUtil.getScanner(),
                "Enter the valid event capacity:"
            );
        }

        Event event = new Event();
        event.setOrganizerId(userId);
        event.setTitle(title);
        event.setDescription(description);
        event.setVenueId(venueId);
        event.setStartDateTime(startTime);
        event.setEndDateTime(endTime);
        event.setCapacity(eventCapacity);
        event.setCategoryId(categoryId);

        int id = organizerService.createEvent(event);
        System.out.println("Event created with ID: " + id);
    }
    
    /* ===================== Event details ===================== */
    /**
     * Displays detailed view of organizer owned events.
     *
     * @param userId organizer ID
     */
    public void viewMyEventDetails(int userId) {
        List<Event> events = getOrganizerEvents(userId);

        if (events.isEmpty()) {
            System.out.println("You have not created any events yet.");
            return;
        }

        MenuHelper.printEventSummaries(events);

        int choice = MenuHelper.selectFromList(events.size(), "Select an event");

        Event selectedEvent = events.get(choice - 1);

        Event event = getOrganizerEventById(
            userId,
            selectedEvent.getEventId()
        );

        if (event == null) {
            System.out.println("You are not authorized to view this event");
            return;
        }

        MenuHelper.printEventDetails(event);
    }
    
    /* ===================== EVENT DETAILS UPDATION ===================== */
    /**
     * Updates editable event details.
     * Venue modification is intentionally restricted.
     *
     * @param userId organizer ID
     */
    public void updateEventDetails(int userId) {

        List<Event> events = getOrganizerEvents(userId);
        if (events.isEmpty()) {
            System.out.println("The organizer hasn't hosted any events");
            return;
        }

        List<Event> sortedEvents = events.stream()
            .filter(e ->
                EventStatus.DRAFT.toString().equals(e.getStatus())
                && e.getStartDateTime().isAfter(LocalDateTime.now())
            )
            .sorted(Comparator.comparing(Event::getStartDateTime))
            .toList();

        if (sortedEvents.isEmpty()) {
            System.out.println("No upcoming events available for editing.");
            return;
        }

        MenuHelper.printEventSummaries(sortedEvents);

        int choice = MenuHelper.selectFromList(sortedEvents.size(), "Select an event");

        Event selectedEvent = sortedEvents.get(choice - 1);

        System.out.println("Press Enter to keep the current value.");

        String title = InputValidationUtil.readString(
            ScannerUtil.getScanner(),
            "Title (" + selectedEvent.getTitle() + "): "
        );
        if (title.isBlank()) {
            title = selectedEvent.getTitle();
        }

        String description = InputValidationUtil.readString(
            ScannerUtil.getScanner(),
            "Description (" + selectedEvent.getDescription() + "): "
        );
        if (description.isBlank()) {
            description = selectedEvent.getDescription();
        }

        List<Category> categories = getAllCategory();
        if (categories.isEmpty()) {
            System.out.println("No categories available");
            return;
        }

        System.out.println("Categories (enter 0 to keep current category)");
        int index = 1;
        for (Category category : categories) {
            System.out.println(index + ". " + category.getName());
            index++;
        }

        int categoryChoice = InputValidationUtil.readInt(
            ScannerUtil.getScanner(),
            "Category (" + selectedEvent.getCategoryId() + "): "
        );

        int categoryId;
        if (categoryChoice == 0) {
            categoryId = selectedEvent.getCategoryId();
        } else {
            while (categoryChoice < 1 || categoryChoice > categories.size()) {
                categoryChoice = InputValidationUtil.readInt(
                    ScannerUtil.getScanner(),
                    "Please enter a valid number from the list.: "
                );
            }
            categoryId = categories.get(categoryChoice - 1).getCategoryId();
        }

        boolean result = updateEventDetails(
            selectedEvent.getEventId(),
            title,
            description,
            categoryId,
            selectedEvent.getVenueId()
        );

        System.out.println(
            result
            ? "Event details updated successfully.\n"
            : "Failed to update event details.\n"
        );
    }

    /* ===================== UPDATE EVENT CAPACITY ===================== */
    /**
     * Allows organizer to update capacity for upcoming events.
     * Capacity cannot be less than already booked tickets.
     *
     * @param userId the organizer ID
     */
    public void updateEventCapacity(int userId) {
        List<Event> events = getOrganizerEvents(userId);
        if (events.isEmpty()) {
            System.out.println("You have not created any events yet.");
            return;
        }

        List<Event> sortedEvents = events.stream()
            .filter(e ->
                (EventStatus.DRAFT.toString().equals(e.getStatus()))
                && e.getStartDateTime().isAfter(LocalDateTime.now())
            )
            .sorted(Comparator.comparing(Event::getStartDateTime))
            .collect(Collectors.toList());

        if (sortedEvents.isEmpty()) {
            System.out.println("The organizer has'nt have any upcoming events");
            return;
        }

        MenuHelper.printEventSummaries(sortedEvents);

        int choice = MenuHelper.selectFromList(sortedEvents.size(), "Select an event");

        Event selectedEvent = sortedEvents.get(choice - 1);
        int eventId = selectedEvent.getEventId();

        int booked = organizerService.viewEventRegistrations(eventId);

        System.out.println(
            "Current capacity: " + selectedEvent.getCapacity()
            + " | Tickets already booked: " + booked
        );

        Venue venue = eventService.getVenueById(selectedEvent.getVenueId());
        System.out.println("The maximum capacity of venue is: " + venue.getMaxCapacity());

        int capacity = InputValidationUtil.readInt(
            ScannerUtil.getScanner(),
            "Enter the new capacity: "
        );

        while (capacity < booked || capacity > venue.getMaxCapacity()) {
            capacity = InputValidationUtil.readInt(
                ScannerUtil.getScanner(),
                "Enter new capacity (between "
                + booked + " and " + venue.getMaxCapacity() + "): "
            );
        }

        boolean result = updateEventCapacity(eventId, capacity);
        System.out.println(result ? "Capacity updated" : "Update failed");
    }
    
    /* ===================== PUBLISH EVENT ===================== */
	

    /**
     * Publishes an approved event.
     * Allows optional final edits before publishing.
     * Requires ticket types to be created before publishing.
     *
     * @param userId the organizer ID
     */
	public void publishEvent(int userId) {

		List<Event> events = getOrganizerEvents(userId);

		if (events.isEmpty()) {
			System.out.println("No events found.");
			return;
		}

		List<Event> eligibleEvents = events.stream()
				.filter(e -> EventStatus.APPROVED.toString().equals(e.getStatus())
						&& e.getStartDateTime().isAfter(LocalDateTime.now()) && e.getUpdatedAt() != null)
				.sorted(Comparator.comparing(Event::getStartDateTime)).collect(Collectors.toList());

		if (eligibleEvents.isEmpty()) {
			System.out.println("No eligible events available for publishing");
			return;
		}

		MenuHelper.printEventSummaries(eligibleEvents);

		int choice = MenuHelper.selectFromList(eligibleEvents.size(), "Select an event");

		Event selectedEvent = eligibleEvents.get(choice - 1);

		int eventId = selectedEvent.getEventId();
		int capacity = selectedEvent.getCapacity();

		int remainingCapacity = capacity;

		while (remainingCapacity > 0) {

			System.out.println("\nEvent Capacity: " + capacity + "\nRemaining Capacity: " + remainingCapacity);

			System.out.println("\n1. Add ticket type\n" + "2. Cancel publishing\n\n>");

			int option = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");

			if (option == 2) {
				return;
			}

			if (option != 1) {
				System.out.println("Invalid option. Please select from the menu.");
				continue;
			}

			Ticket ticket = new Ticket();
			ticket.setEventId(eventId);
			String ticketType = InputValidationUtil.readNonEmptyString(ScannerUtil.getScanner(), "Ticket Type: ");
			while(ticketType.length() < 3 || ticketType.length() > 30) {
				ticketType = InputValidationUtil.readNonEmptyString(ScannerUtil.getScanner(), "Ticket Type (min 3 - 30 characters): ");
			}
			ticket.setTicketType(ticketType);
			
			double price;
			do {
			    price = InputValidationUtil.readDouble(ScannerUtil.getScanner(), "Ticket Price (₹): ");
			} while (price <= 0);

			ticket.setPrice(price);

			int qty = InputValidationUtil.readInt(ScannerUtil.getScanner(),
					"Ticket Quantity (max " + remainingCapacity + "): ");

			while (qty <= 0 || qty > remainingCapacity) {
				qty = InputValidationUtil.readInt(ScannerUtil.getScanner(),
						"Enter valid quantity (1-" + remainingCapacity + "): ");
			}

			ticket.setTotalQuantity(qty);

			organizerService.createTicket(ticket);

			remainingCapacity -= qty;
		}

		boolean published = organizerService.publishEvent(eventId);

		System.out.println(published ? "Event published successfully" : "Publish failed");
	}
	
	/* ===================== EVENT CANCELLATION ===================== */
    /**
     * Cancels an event created by the organizer.
     * Published events require admin approval.
     */
	public void cancelEvent(int userId) {

		List<Event> events = organizerService.getOrganizerEvents(userId);

		if (events.isEmpty()) {
			System.out.println("No events found.");
			return;
		}

		List<Event> cancellableEvents = events.stream()
				.filter(e -> EventStatus.DRAFT.toString().equals(e.getStatus())
						|| EventStatus.PUBLISHED.toString().equals(e.getStatus()))
				.sorted(Comparator.comparing(Event::getStartDateTime)).collect(Collectors.toList());

		if (cancellableEvents.isEmpty()) {
			System.out.println("No events available for cancellation");
			return;
		}

		MenuHelper.printEventSummaries(cancellableEvents);

		int choice = MenuHelper.selectFromList(cancellableEvents.size(), "Select an event");

		Event selectedEvent = cancellableEvents.get(choice - 1);

		char confirm = InputValidationUtil.readChar(ScannerUtil.getScanner(),
				"Are you sure you want to cancel this event (Y/N): ");

		if (Character.toUpperCase(confirm) != 'Y') {
			System.out.println("Event cancellation cancelled.\n");
			return;
		}
		if(selectedEvent.getStatus().equals(EventStatus.PUBLISHED.toString())) {
			System.out.println("The events which are already published cannot be cancelled by the organizer");
			String message = InputValidationUtil.readNonEmptyString(ScannerUtil.getScanner(), 
					"Enter the message detaily requesting the event cancellation to the admin\n"
					+ "Event only cancelled if the admin decided:\n");
			organizerService.sendCancellationRequest(selectedEvent, message);
			System.out.println("Event cancellation request sent!");
			
		}else {
			boolean result = organizerService.cancelEvent(selectedEvent.getEventId());

			if (!result) {
				System.out.println("Cancel failed");
				return;
			}

			System.out.println("Event cancelled successfully");
		}

	}
	
	/* ===================== DATA RETRIVAL METHODS ===================== */

	/**
     * Retrieves all events created by a specific organizer.
     *
     * @param organizerId the ID of the organizer
     * @return list of events created by the organizer
     */
    public List<Event> getOrganizerEvents(int organizerId) {
        return organizerService.getOrganizerEvents(organizerId);
    }

    /**
     * Retrieves a specific event owned by the organizer.
     *
     * @param organizerId the ID of the organizer
     * @param eventId the ID of the event
     * @return event if found and authorized, otherwise null
     */
    public Event getOrganizerEventById(int organizerId, int eventId) {
        return organizerService.getOrganizerEventById(organizerId, eventId);
    }

    /**
     * Retrieves all available event categories.
     *
     * @return list of categories
     */
    public List<Category> getAllCategory() {
        return eventService.getAllCategory();
    }

    /**
     * Retrieves all available venues.
     *
     * @return list of venues
     */
    public List<Venue> getAllVenues() {
        return eventService.getActiveVenues();
    }

    /**
     * Retrieves formatted venue address.
     *
     * @param venueId the ID of the venue
     * @return formatted address string
     */
    public String getVenueAddress(int venueId) {
        return eventService.getVenueAddress(venueId);
    }

    /**
     * Checks venue availability for a given time range.
     *
     * @param venueId the venue ID
     * @param startTime event start date and time
     * @param endTime event end date and time
     * @return true if venue is available
     */
    public boolean isVenueAvailable(int venueId, LocalDateTime startTime, LocalDateTime endTime) {
        return eventService.isVenueAvailable(venueId, startTime, endTime);
    }

    /**
     * Retrieves venue details by ID.
     *
     * @param venueId the ID of the venue
     * @return venue object
     */
    public Venue getVenueById(int venueId) {
        return eventService.getVenueById(venueId);
    }
    
    
    /**
     * Updates editable event details.
     * Venue change is intentionally restricted.
     *
     * @param eventId the event ID
     * @param title updated title
     * @param description updated description
     * @param categoryId updated category ID
     * @param venueId existing venue ID
     * @return true if update succeeds
     */
    public boolean updateEventDetails(int eventId, String title, String description,
                                      int categoryId, int venueId) {
        return organizerService.updateEventDetails(
            eventId,
            title,
            description,
            categoryId,
            venueId
        );
    }

    /**
     * Updates event capacity.
     *
     * @param eventId the event ID
     * @param capacity new capacity
     * @return true if update succeeds
     */
    public boolean updateEventCapacity(int eventId, int capacity) {
        return organizerService.updateEventCapacity(eventId, capacity);
    }


}
