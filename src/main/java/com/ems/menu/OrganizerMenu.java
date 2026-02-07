package com.ems.menu;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.ems.actions.OrganizerEventManagementAction;
import com.ems.actions.OrganizerTicketManagementAction;
import com.ems.actions.OrganizerRegistrationAction;
import com.ems.actions.OrganizerReportAction;
import com.ems.actions.NotificationAction;
import com.ems.enums.EventStatus;
import com.ems.model.Event;
import com.ems.model.OrganizerEventSummary;
import com.ems.model.User;
import com.ems.util.InputValidationUtil;
import com.ems.util.MenuHelper;
import com.ems.util.ScannerUtil;

/*
 * Handles all organizer facing menu navigation.
 * Delegates actual business logic to action classes.
 */
public class OrganizerMenu extends BaseMenu {

	/* ===================== ACTION DEPENDENCIES ===================== */

	private final OrganizerEventManagementAction eventManagementAction;
	private final OrganizerTicketManagementAction ticketManagementAction;
	private final OrganizerRegistrationAction registrationAction;
	private final OrganizerReportAction reportAction;
	private final NotificationAction notificationAction;

	public OrganizerMenu(User user) {
		super(user);
		this.eventManagementAction = new OrganizerEventManagementAction();
		this.ticketManagementAction = new OrganizerTicketManagementAction();
		this.registrationAction = new OrganizerRegistrationAction();
		this.reportAction = new OrganizerReportAction();
		this.notificationAction = new NotificationAction();
	}

	/* ===================== ENTRY POINT ===================== */

	public void start() {

		notificationAction.displayUnreadNotifications(loggedInUser.getUserId());

		while (true) {
			System.out.println(
				    "\nOrganizer menu\n" +
				    "1 Event management\n" +
				    "2 Ticket management\n" +
				    "3 Registrations\n" +
				    "4 Reports\n" +
				    "5 Notifications\n" +
				    "6 Logout\n\n" +
				    "Choice:"
				);


			int choice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");

			switch (choice) {
			case 1:
				eventManagementMenu();
				break;
			case 2:
				ticketManagementMenu();
				break;
			case 3:
				registrationMenu();
				break;
			case 4:
				reportMenu();
				break;
			case 5:
				notificationMenu();
				break;
			case 6:
				if (confirmLogout()) {
					return;
				}
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
			}
		}
	}

	/* ===================== EVENT MANAGEMENT ===================== */

	private void eventManagementMenu() {

		while (true) {
			System.out.println(
				    "\nEvent management\n" +
				    "1 Create new event\n" +
				    "2 View my events\n" +
				    "3 Update event details\n" +
				    "4 Update event capacity\n" +
				    "5 Publish event\n" +
				    "6 Cancel event\n" +
				    "7 Back\n\n" +
				    "Choice:"
				);


			int choice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");

			switch (choice) {
			case 1:
				eventManagementAction.createEvent(loggedInUser.getUserId());
				break;
			case 2:
				eventManagementAction.viewMyEventDetails(loggedInUser.getUserId());
				break;
			case 3:
				eventManagementAction.updateEventDetails(loggedInUser.getUserId());
				break;
			case 4:
				eventManagementAction.updateEventCapacity(loggedInUser.getUserId());
				break;
			case 5:
				eventManagementAction.publishEvent(loggedInUser.getUserId());
				break;
			case 6:
				eventManagementAction.cancelEvent(loggedInUser.getUserId());
				break;
			case 7:
				return;
			default:
				System.out.println("Invalid choice. Please try again.");
			}
		}
	}

	/* ===================== TICKET MANAGEMENT ===================== */

	private void ticketManagementMenu() {

		while (true) {
			System.out.println(
				    "\nTicket management\n" +
				    "1 Update ticket price\n" +
				    "2 Update ticket quantity\n" +
				    "3 View ticket availability\n" +
				    "4 Back\n\n" +
				    "Choice:"
				);

			int choice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");

			switch (choice) {
			case 1:
				ticketManagementAction.updateTicketPrice(loggedInUser.getUserId());
				break;
			case 2:
				ticketManagementAction.updateTicketQuantity(loggedInUser.getUserId());
				break;
			case 3:
				ticketManagementAction.viewTicketAvailability(loggedInUser.getUserId());
				break;
			case 4:
				return;
			default:
				System.out.println("Invalid choice. Please try again.");
			}
		}
	}

	/* ===================== REGISTRATIONS & ATTENDEES ===================== */

	private void registrationMenu() {

		while (true) {
			System.out.println(
				    "\nRegistrations and attendees\n" +
				    "1 View event registrations\n" +
				    "2 View registered users\n" +
				    "3 Back\n\n" +
				    "Choice:"
				);


			int choice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");

			switch (choice) {

			case 1: {
				List<Event> events =
					eventManagementAction.getOrganizerEvents(loggedInUser.getUserId());

				if (events.isEmpty()) {
					System.out.println("No events available.");
					break;
				}

				MenuHelper.printEventSummaries(events);

				int eventChoice =
					MenuHelper.selectFromList(events.size(), "Select an event: ");
				Event selectedEvent = events.get(eventChoice - 1);

				int count =
					registrationAction.viewEventRegistrations(selectedEvent.getEventId());

				System.out.println("Total Registrations: " + count);
				break;
			}

			case 2: {
				List<Event> events =
					eventManagementAction.getOrganizerEvents(loggedInUser.getUserId());

				if (events.isEmpty()) {
					System.out.println("No events available.");
					break;
				}

				MenuHelper.printEventSummaries(events);

				int eventChoice =
					MenuHelper.selectFromList(events.size(), "Select an event: ");
				Event selectedEvent = events.get(eventChoice - 1);

				List<Map<String, Object>> users =
					registrationAction.viewRegisteredUsers(selectedEvent.getEventId());

				if (users != null && !users.isEmpty()) {
					System.out.println("Registered Users\n");
					users.forEach(
						u -> System.out.println(
							u.get("userId") + " | " + u.get("name") + " | " + u.get("email")
						)
					);
				}
				break;
			}

			case 3:
				return;

			default:
				System.out.println("Invalid choice. Please try again.");
			}
		}
	}

	/* ===================== REPORTS ===================== */

	private void reportMenu() {

		while (true) {
			System.out.println(
				    "\nReports\n" +
				    "1 Event registrations\n" +
				    "2 Ticket sales\n" +
				    "3 Revenue summary\n" +
				    "4 My events summary\n" +
				    "5 Back\n\n" +
				    "Choice:"
				);


			int choice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");

			switch (choice) {
			case 1:
				reportAction.getEventWiseRegistrations(loggedInUser.getUserId())
					.forEach(r ->
						System.out.println(
							"Event: " + r.get("event")
							+ " | Registrations: " + r.get("count")
						)
					);
				break;

			case 2:
				List<Map<String, Object>> list =
					reportAction.getTicketSales(loggedInUser.getUserId());

				if (list != null) {
					list.forEach(r ->
						System.out.println(
							"Ticket Type: " + r.get("ticketType")
							+ " | Tickets Sold: " + r.get("sold")
						)
					);
				} else {
					System.out.println("No ticket sales found.");
				}
				break;

			case 3:
				double revenue =
					reportAction.getRevenueSummary(loggedInUser.getUserId());
				System.out.println("Total revenue: ₹" + revenue);
				break;

			case 4:
				viewMyEventsSummary();
				break;

			case 5:
				return;

			default:
				System.out.println("Invalid choice. Please try again.");
			}
		}
	}

	/* ===================== EVENTS SUMMARY ===================== */

	private void viewMyEventsSummary() {

		List<OrganizerEventSummary> list =
			reportAction.getOrganizerEventSummary(loggedInUser.getUserId());

		if (list.isEmpty()) {
			System.out.println("You have not created any events yet.");
			return;
		}

		String currentStatus = "";

		for (OrganizerEventSummary s : list) {

			if (!s.getStatus().equals(currentStatus)) {
				currentStatus = s.getStatus();
				System.out.println("\n[" + currentStatus + "]");
			}

			System.out.println(
				s.getTitle()
				+ " | Tickets Booked: " + s.getBookedTickets()
				+ " out of " + s.getTotalTickets()
			);
		}
	}

	/* ===================== NOTIFICATIONS ===================== */

	private void notificationMenu() {

		while (true) {
			System.out.println(
				    "\nNotifications\n" +
				    "1 Send event update\n" +
				    "2 Send schedule change\n" +
				    "3 View notifications\n" +
				    "4 Back\n\n" +
				    "Choice:"
				);


			int choice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");

			switch (choice) {
			case 1:
			case 2:
				List<Event> events =
					eventManagementAction.getOrganizerEvents(loggedInUser.getUserId());

				List<Event> filteredEvents = events.stream()
					.filter(e ->
						e.getStatus().equals(EventStatus.PUBLISHED.toString())
						&& e.getStartDateTime().isAfter(LocalDateTime.now())
					)
					.toList();

				MenuHelper.printEventSummaries(filteredEvents);

				int eventChoice =
					MenuHelper.selectFromList(filteredEvents.size(), "Select an event");
				Event selectedEvent = events.get(eventChoice - 1);

				String msg =
					InputValidationUtil.readString(
						ScannerUtil.getScanner(),
						"Message:\n"
					);

				if (choice == 1) {
					notificationAction.sendEventUpdate(selectedEvent.getEventId(), msg);
				} else {
					notificationAction.sendScheduleChange(selectedEvent.getEventId(), msg);
				}
				break;

			case 3:
				notificationAction.displayAllNotifications(loggedInUser.getUserId());
				break;

			case 4:
				return;

			default:
				System.out.println("Invalid choice. Please try again.");
			}
		}
	}

	/* ===================== LOGOUT ===================== */

	private boolean confirmLogout() {
		char choice =
			InputValidationUtil.readChar(
				ScannerUtil.getScanner(),
				"Are you sure you want to logout? (Y/N): "
			);
		return Character.toUpperCase(choice) == 'Y';
	}
}
