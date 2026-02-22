package com.ems.actions;

import java.util.Comparator;
import java.util.List;

import com.ems.model.Event;
import com.ems.model.EventRegistrationReport;
import com.ems.model.Ticket;
import com.ems.service.AdminService;
import com.ems.service.EventService;
import com.ems.util.AdminMenuHelper;
import com.ems.util.ApplicationUtil;
import com.ems.util.InputValidationUtil;
import com.ems.util.MenuHelper;
import com.ems.util.PaginationUtil;
import com.ems.util.ScannerUtil;

public class AdminTicketManagementAction {
    private final EventService eventService;
    private final AdminService adminService;

    public AdminTicketManagementAction() {
        this.eventService = ApplicationUtil.eventService();
        this.adminService = ApplicationUtil.adminService();
    }
    
    public void viewTicketsByEvent() {
    	List<Event> events = getAvailableEvents();
		if (events.isEmpty()) {
			System.out.println("No events available at the moment.");
			return;
		}
		PaginationUtil.paginate(events, MenuHelper::printEventSummaries);

		int eChoice = InputValidationUtil.readInt(ScannerUtil.getScanner(),
				"Select an event (1-" + events.size() + "): ");
		while (eChoice < 1 || eChoice > events.size()) {
			eChoice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "Enter a valid choice: ");
		}

		Event selectedEvent = events.get(eChoice - 1);
		List<Ticket> tickets = getTicketsForEvent(selectedEvent.getEventId());

		if (tickets.isEmpty()) {
			System.out.println("No tickets found for this event");
			return;
		}

		AdminMenuHelper.printTicketDetails(tickets);
    }


	public void viewTicketSummary() {
		List<Event> events = getAvailableEvents();
		if (events.isEmpty()) {
			System.out.println("No events available at the moment.");
			return;
		}

		PaginationUtil.paginate(events, MenuHelper::printEventSummaries);

		int eChoice = InputValidationUtil.readInt(ScannerUtil.getScanner(),
				"Select an event (1-" + events.size() + "): ");
		while (eChoice < 1 || eChoice > events.size()) {
			eChoice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "Enter a valid choice: ");
		}

		Event selectedEvent = events.get(eChoice - 1);
		List<Ticket> tickets = getTicketsForEvent(selectedEvent.getEventId());

		AdminMenuHelper.printTicketCapacitySummary(tickets);
		
	}
	
	

	public void viewEventRegistrations() {
		List<Event> events = getAvailableEvents();
		if (events.isEmpty()) {
			System.out.println("No events available at the moment.");
			return;
		}

		PaginationUtil.paginate(events, MenuHelper::printEventSummaries);

		int eChoice = InputValidationUtil.readInt(ScannerUtil.getScanner(),
				"Select an event (1-" + events.size() + "): ");
		while (eChoice < 1 || eChoice > events.size()) {
			eChoice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "Enter a valid choice: ");
		}

		Event selectedEvent = events.get(eChoice - 1);
		List<EventRegistrationReport> reports = getEventWiseRegistrations(selectedEvent.getEventId());
		if (reports.isEmpty()) {
			System.out.println("No registrations found for this event");
			return;
		}
		reports.sort(Comparator.comparing(EventRegistrationReport::getRegistrationDate).reversed());
		PaginationUtil.paginate(reports, AdminMenuHelper::printEventRegistrationReport);
		
		
	}
	
    public List<Event> getAvailableEvents() {
        return eventService.listAvailableEvents();
    }

    public List<Ticket> getTicketsForEvent(int eventId) {
        return eventService.getTicketTypes(eventId);
    }

    public List<EventRegistrationReport> getEventWiseRegistrations(int eventId) {
    	List<EventRegistrationReport> reports = adminService.getEventWiseRegistrations(eventId);
        
		return reports;
    }


}
