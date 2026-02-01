package com.ems.actions;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.ems.enums.EventStatus;
import com.ems.model.Event;
import com.ems.model.Ticket;
import com.ems.service.OrganizerService;
import com.ems.util.ApplicationUtil;
import com.ems.util.InputValidationUtil;
import com.ems.util.MenuHelper;
import com.ems.util.ScannerUtil;

/**
 * Action class for organizer ticket management operations.
 * Delegates business logic to appropriate services.
 */
public class OrganizerTicketManagementAction {

    private final OrganizerService organizerService;

    public OrganizerTicketManagementAction() {
        this.organizerService = ApplicationUtil.organizerService();
    }

    /**
     * Creates a new ticket for an event.
     * 
     * @param ticket the ticket object to create
     */
    public void createTicket(Ticket ticket) {
        organizerService.createTicket(ticket);
    }
    
    
    
    /**
     * Updates the ticket price of the event
     * 
     * @param userId the organizer ID
     */
	public void updateTicketPrice(int userId) {

		List<Event> events = organizerService.getOrganizerEvents(userId);

		if (events.isEmpty()) {
			System.out.println("No events found.");
			return;
		}

		List<Event> validEvents = events.stream()
				.filter(e -> EventStatus.DRAFT.toString().equals(e.getStatus())
						|| EventStatus.PUBLISHED.toString().equals(e.getStatus()))
				.sorted(Comparator.comparing(Event::getStartDateTime)).collect(Collectors.toList());

		if (validEvents.isEmpty()) {
			System.out.println("No events available");
			return;
		}

		MenuHelper.printEventSummaries(validEvents);

		int eventChoice = InputValidationUtil.readInt(ScannerUtil.getScanner(),
				"Select an event number (1-" + validEvents.size() + "): ");

		while (eventChoice < 1 || eventChoice > validEvents.size()) {
			eventChoice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "Please enter a valid number from the list.: ");
		}

		Event selectedEvent = validEvents.get(eventChoice - 1);

		List<Ticket> tickets = organizerService.viewTicketAvailability(selectedEvent.getEventId());

		if (tickets.isEmpty()) {
			System.out.println("No tickets available for this event");
			return;
		}

		Ticket selectedTicket = selectTicketFromList(tickets, false);
		if (selectedTicket == null) {
		    return;
		}

		double newPrice = InputValidationUtil.readDouble(ScannerUtil.getScanner(), "Enter new price: ");

		while (newPrice <= 0) {
			newPrice = InputValidationUtil.readDouble(ScannerUtil.getScanner(), "Enter a valid price: ");
		}

		boolean result = organizerService.updateTicketPrice(selectedTicket.getTicketId(), newPrice);

		System.out.println(result ? "Ticket price updated successfully" : "Unable to update ticket. Please try again.\n");
	}
	
	/**
	 * update the ticket quantity of the event
	 * Only works if the event capacity is more than the cummulated ticket count of event
	 * 
	 * @param userId the organizer ID
	 */
	public void updateTicketQuantity(int userId) {

		List<Event> events = organizerService.getOrganizerEvents(userId);

		if (events.isEmpty()) {
			System.out.println("No events found.");
			return;
		}

		List<Event> validEvents = events.stream()
				.filter(e -> EventStatus.PUBLISHED.toString().equals(e.getStatus()))
				.sorted(Comparator.comparing(Event::getStartDateTime)).collect(Collectors.toList());

		if (validEvents.isEmpty()) {
			System.out.println("No events available");
			return;
		}

		MenuHelper.printEventSummaries(validEvents);
		
		int eventChoice = MenuHelper.selectFromList(validEvents.size(), "Select an event number");
		Event selectedEvent = validEvents.get(eventChoice - 1);
		int capacity = selectedEvent.getCapacity();

		List<Ticket> tickets = organizerService.viewTicketAvailability(selectedEvent.getEventId());

		int totalTickets = tickets.stream().mapToInt(Ticket::getTotalQuantity).sum();

		if (totalTickets == capacity) {
			System.out.println("Ticket quantity already matches event capacity");
			return;
		}

		int remaining = capacity - totalTickets;

		System.out.println("\nEvent Capacity: " + capacity + "\nCurrent Ticket Quantity: " + totalTickets
				+ "\nRemaining Capacity: " + remaining);

		System.out.println("\n1. Update existing ticket\n" + "2. Add new ticket\n\n>");

		int option = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");

		if (option == 2) {
			createTicketForEvent(selectedEvent.getEventId(), remaining);
			return;
		}

		if (option != 1) {
			System.out.println("Invalid option. Please select from the menu.");
			return;
		}

		if (tickets.isEmpty()) {
			System.out.println("No tickets available to update");
			return;
		}

		Ticket selectedTicket = selectTicketFromList(tickets, false);
		if (selectedTicket == null) {
		    return;
		}

		int maxAllowed = selectedTicket.getTotalQuantity() + remaining;

		int newQty = InputValidationUtil.readInt(
		        ScannerUtil.getScanner(),
		        "Enter new quantity (max " + maxAllowed + "): ");

		while (newQty <= 0 || newQty > maxAllowed) {
		    newQty = InputValidationUtil.readInt(ScannerUtil.getScanner(), "Enter valid quantity: ");
		}

		boolean result = organizerService.updateTicketQuantity(selectedTicket.getTicketId(), newQty);
		System.out.println(result ? "Ticket quantity updated successfully" : "Update failed");

	}

	/**
	 * Helps to see how much ticket has been sold on the particular event 
	 * 
	 * @param userId the organizer ID
	 */
	public void viewTicketAvailability(int userId) {

		List<Event> events = organizerService.getOrganizerEvents(userId);

		if (events.isEmpty()) {
			System.out.println("No events found.");
			return;
		}

		List<Event> validEvents = events.stream()
				.filter(e -> EventStatus.DRAFT.toString().equals(e.getStatus())
						|| EventStatus.PUBLISHED.toString().equals(e.getStatus()))
				.sorted(Comparator.comparing(Event::getStartDateTime)).collect(Collectors.toList());

		if (validEvents.isEmpty()) {
			System.out.println("No events available");
			return;
		}

		MenuHelper.printEventSummaries(validEvents);

		int eventChoice = MenuHelper.selectFromList(validEvents.size(), "Select a event");

		Event selectedEvent = validEvents.get(eventChoice - 1);

		List<Ticket> tickets = organizerService.viewTicketAvailability(selectedEvent.getEventId());
		selectTicketFromList(tickets, true);

	}
	
	
	/* ===================== HELPER FUNCTIONS ===================== */
	

	private void createTicketForEvent(int eventId, int remainingCapacity) {

		Ticket ticket = new Ticket();
		ticket.setEventId(eventId);

		ticket.setTicketType(InputValidationUtil.readNonEmptyString(ScannerUtil.getScanner(), "Ticket Type: "));

		ticket.setPrice(InputValidationUtil.readDouble(ScannerUtil.getScanner(), "Ticket Price (₹): "));

		int qty = InputValidationUtil.readInt(ScannerUtil.getScanner(),
				"Ticket Quantity (max " + remainingCapacity + "): ");

		while (qty <= 0 || qty > remainingCapacity) {
			qty = InputValidationUtil.readInt(ScannerUtil.getScanner(), "Enter valid quantity: ");
		}

		ticket.setTotalQuantity(qty);

		createTicket(ticket);

		System.out.println("Ticket added successfully");
	}
	
	private Ticket selectTicketFromList(List<Ticket> tickets, boolean showAvailability) {

	    if (tickets == null || tickets.isEmpty()) {
	        System.out.println("No tickets available");
	        return null;
	    }

	    int index = 1;
	    for (Ticket t : tickets) {
	        if (showAvailability) {
	            System.out.println(index + ". " + t.getTicketType()
	                    + " | Total: " + t.getTotalQuantity()
	                    + " | Available: " + t.getAvailableQuantity()
	                    + " | Price: " + t.getPrice());
	        } else {
	            System.out.println(index + ". " + t.getTicketType()
	                    + " | Price: " + t.getPrice()
	                    + " | Quantity: " + t.getTotalQuantity());
	        }
	        index++;
	    }

	    int choice = MenuHelper.selectFromList(tickets.size(), "Select a ticket");
	    return tickets.get(choice - 1);
	}


}
