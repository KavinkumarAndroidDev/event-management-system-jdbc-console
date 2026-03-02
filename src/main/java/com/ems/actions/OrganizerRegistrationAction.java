package com.ems.actions;

import java.util.Comparator;
import java.util.List;

import com.ems.model.Event;
import com.ems.model.EventRegistrationReport;
import com.ems.service.OrganizerService;
import com.ems.util.AdminMenuHelper;
import com.ems.util.ApplicationUtil;
import com.ems.util.InputValidationUtil;
import com.ems.util.MenuHelper;
import com.ems.util.PaginationUtil;
import com.ems.util.ScannerUtil;
import com.ems.exception.DataAccessException;

/**
 * Action class for organizer registration management operations.
 * Delegates business logic to appropriate services.
 */
public class OrganizerRegistrationAction {

	private final OrganizerService organizerService;
	
	public OrganizerRegistrationAction() {
		this.organizerService = ApplicationUtil.organizerService();
	}

	/**
	 * Views the total number of registrations for a specific event.
	 * 
	 * @param organizerId the ID of the organizer
	 * @return the count of registrations
	 */
	public void viewEventRegistrations(int organizerId) {
		try {
			List<Event> events = organizerService.getOrganizerEvents(organizerId);
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
			List<EventRegistrationReport> reports = organizerService
					.getEventWiseRegistrations(selectedEvent.getEventId());
			if (reports.isEmpty()) {
				System.out.println("No registrations found for this event");
				return;
			}
			reports.sort(Comparator.comparing(EventRegistrationReport::getRegistrationDate).reversed());
			PaginationUtil.paginate(reports, AdminMenuHelper::printEventRegistrationReport);
		} catch (DataAccessException e) {
			System.out.println("Error viewing registrations: " + e.getMessage());
		}
	}
}
