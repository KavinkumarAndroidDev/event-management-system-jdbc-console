package com.ems.actions;

import java.util.List;

import com.ems.enums.EventStatus;
import com.ems.exception.DataAccessException;
import com.ems.model.Event;
import com.ems.model.Ticket;
import com.ems.service.AdminService;
import com.ems.service.EventService;
import com.ems.util.AdminMenuHelper;
import com.ems.util.ApplicationUtil;
import com.ems.util.InputValidationUtil;
import com.ems.util.MenuHelper;
import com.ems.util.PaginationUtil;
import com.ems.util.ScannerUtil;

public class AdminEventManagementAction {
	private final AdminService adminService;
	private final EventService eventService;

	public AdminEventManagementAction() {
		this.adminService = ApplicationUtil.adminService();
		this.eventService = ApplicationUtil.eventService();
	}

	public void listAllEvents() {
		try {
			List<Event> events = getAllEvents();
			if (events.isEmpty()) {
				System.out.println("No events available at the moment.");
			} else {
				PaginationUtil.paginate(events, AdminMenuHelper::printAllEventsWithStatus);
			}
		} catch (DataAccessException e) {
			System.out.println("Error listing events: " + e.getMessage());
		}
	}

	public void printEventDetails() {
		try {
			Event selectedEvent = selectAnyEvent();
			if (selectedEvent == null)
				return;

			MenuHelper.printEventDetails(selectedEvent);
		} catch (DataAccessException e) {
			System.out.println("Error printing event details: " + e.getMessage());
		}
	}

	public void listTicketsForEvent() {
		try {
			List<Event> events = getAllEvents();
			List<Event> filteredEvents = events.stream()
					.filter(e -> EventStatus.PUBLISHED.equals(e.getStatus())).toList();
			if (filteredEvents.isEmpty()) {
				System.out.println("No published events available at the moment.");
				return;
			}

			PaginationUtil.paginate(filteredEvents, AdminMenuHelper::printAllEventsWithStatus);

			int choice = InputValidationUtil.readInt(ScannerUtil.getScanner(),
					"Select an event (1-" + filteredEvents.size() + "): ");

			while (choice < 1 || choice > filteredEvents.size()) {
				choice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "Enter a valid choice: ");
			}

			Event event = filteredEvents.get(choice - 1);

			List<Ticket> tickets = eventService.getTicketTypes(event.getEventId());

			if (tickets.isEmpty()) {
				System.out.println("No ticket options are available for this event.");
				return;
			}

			AdminMenuHelper.printTicketDetails(tickets);
		} catch (DataAccessException e) {
			System.out.println("Error listing tickets: " + e.getMessage());
		}
	}

	public List<Event> getAllEvents() throws DataAccessException {
		return eventService.getAllEvents();
	}

	public Event getEventById(int eventId) throws DataAccessException {
		return eventService.getEventById(eventId);
	}

	public List<Event> getEventsAwaitingApproval() throws DataAccessException {
		return eventService.listEventsYetToApprove();
	}

	public void approveEvent(int adminId) {
		try {
			List<Event> events = getEventsAwaitingApproval();

			if (events == null || events.isEmpty()) {
				System.out.println("There are no events waiting for approval.");
				return;
			}

			PaginationUtil.paginate(events, MenuHelper::printEventSummaries);

			int eventChoice = InputValidationUtil.readInt(ScannerUtil.getScanner(),
					"Select an event to approve (1-" + events.size() + "): ");

			while (eventChoice < 1 || eventChoice > events.size()) {
				eventChoice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "Enter a valid choice: ");
			}

			Event selectedEvent = events.get(eventChoice - 1);
			char approveChoice = InputValidationUtil.readChar(ScannerUtil.getScanner(),
					"Approve this event? (Y/N)\n");
			if (approveChoice == 'Y' || approveChoice == 'y') {
				boolean isApproved = adminService.approveEvents(adminId, selectedEvent.getEventId());
				if (isApproved) {
					System.out.println("Event approved successfully and the organizer has been notified.");
				} else {
					System.out.println("Event approval failed.!");
				}
			} else {
				System.out.println("Action cancelled by user.");
			}
		} catch (DataAccessException e) {
			System.out.println("Error approving event: " + e.getMessage());
		}
	}

	public List<Event> getAvailableAndDraftEvents() throws DataAccessException {
		return eventService.listAvailableAndDraftEvents();
	}

	public void cancelEvent() {
		try {
			List<Event> events = getAvailableAndDraftEvents();

			if (events.isEmpty()) {
				System.out.println("No events available to cancel");
				return;
			}

			PaginationUtil.paginate(events, MenuHelper::printEventSummaries);

			int eventChoice = InputValidationUtil.readInt(ScannerUtil.getScanner(),
					"Select an event to cancel (1-" + events.size() + "): ");

			while (eventChoice < 1 || eventChoice > events.size()) {
				eventChoice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "Enter a valid choice: ");
			}

			Event selectedEvent = events.get(eventChoice - 1);

			char cancelChoice = InputValidationUtil.readChar(ScannerUtil.getScanner(),
					"Cancel this event? (Y/N)\n");
			if (cancelChoice == 'Y' || cancelChoice == 'y') {
				adminService.cancelEvent(selectedEvent.getEventId());

				System.out.println("Event cancelled successfully.");
			} else {
				System.out.println("Action cancelled by user.");
			}
		} catch (DataAccessException e) {
			System.out.println("Error cancelling event: " + e.getMessage());
		}
	}

	public void markCompletedEvents() {
		try {
			adminService.markCompletedEvents();
		} catch (DataAccessException e) {
			System.out.println("Error marking completed events: " + e.getMessage());
		}
	}

	public int getAvailableTickets(int eventId) throws DataAccessException {
		return eventService.getAvailableTickets(eventId);
	}

	public Event selectAnyEvent() throws DataAccessException {

		List<Event> events = getAllEvents();

		if (events.isEmpty()) {
			System.out.println("No events available at the moment.");
			return null;
		}

		PaginationUtil.paginate(events, AdminMenuHelper::printAllEventsWithStatus);

		int choice = InputValidationUtil.readInt(ScannerUtil.getScanner(),
				"Select an event (1-" + events.size() + "): ");

		while (choice < 1 || choice > events.size()) {
			choice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "Enter a valid choice: ");
		}

		return events.get(choice - 1);
	}

	public void listAvailableEvents() {
		try {
			List<Event> events = eventService.listAvailableEvents();
			if (events.isEmpty()) {
				System.out.println("No events available at the moment.");
			} else {
				PaginationUtil.paginate(events, AdminMenuHelper::printAllEventsWithStatus);
			}
		} catch (DataAccessException e) {
			System.out.println("Error listing available events: " + e.getMessage());
		}
	}

}
