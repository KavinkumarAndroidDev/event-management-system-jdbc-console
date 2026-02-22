package com.ems.menu;

import java.util.List;

import com.ems.actions.OrganizerEventManagementAction;
import com.ems.actions.OrganizerOfferManagementAction;
import com.ems.actions.OrganizerTicketManagementAction;
import com.ems.actions.UserAction;
import com.ems.actions.OrganizerRegistrationAction;
import com.ems.actions.OrganizerReportAction;
import com.ems.actions.NotificationAction;
import com.ems.enums.EventStatus;
import com.ems.model.Event;
import com.ems.model.User;
import com.ems.util.DateTimeUtil;
import com.ems.util.InputValidationUtil;
import com.ems.util.MenuHelper;
import com.ems.util.PaginationUtil;
import com.ems.util.ScannerUtil;

/*
 * Handles all organizer facing menu navigation.
 * Delegates actual business logic to action classes.
 */
public class OrganizerMenu extends BaseMenu {

	/* ===================== ACTION DEPENDENCIES ===================== */
	private final UserAction userAction = new UserAction();
	private final OrganizerEventManagementAction eventManagementAction;
	private final OrganizerTicketManagementAction ticketManagementAction;
	private final OrganizerRegistrationAction registrationAction;
	private final OrganizerReportAction reportAction;
	private final NotificationAction notificationAction;
	private final OrganizerOfferManagementAction offerManagementAction;

	public OrganizerMenu(User user) {
		super(user);
		this.eventManagementAction = new OrganizerEventManagementAction();
		this.ticketManagementAction = new OrganizerTicketManagementAction();
		this.registrationAction = new OrganizerRegistrationAction();
		this.reportAction = new OrganizerReportAction();
		this.notificationAction = new NotificationAction();
		this.offerManagementAction = new OrganizerOfferManagementAction();
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
				    "5 Notifications\n"+
				    "6 Offer management\n" +
				    "7 Update profile\n" +
				    "8 Logout\n\n" +
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
				offerManagementMenu();
				break;
			case 7:
				userAction.updateProfile(loggedInUser);
				break;
			case 8:
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
				    "2 Back\n\n" +
				    "Choice:"
				);


			int choice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");

			switch (choice) {

			case 1: {
				registrationAction.viewEventRegistrations(loggedInUser.getUserId());
				break;

			}
			case 2:
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
				    "1 Revenue summary\n" +
				    "2 My events summary\n" +
				    "3 Back\n\n" +
				    "Choice:"
				);


			int choice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");

			switch (choice) {
			case 1: {
			    reportAction.getRevenueSummary(loggedInUser.getUserId());
			    break;
			}
			case 2:{
		        reportAction.getEventSummary(loggedInUser.getUserId());
		        break;
			}
			case 3:
				return;
			default:
				System.out.println("Invalid choice. Please try again.");
			}
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
						&& e.getStartDateTime().isAfter(DateTimeUtil.nowUtc())
					)
					.toList();

				PaginationUtil.paginate(filteredEvents, MenuHelper::printEventSummaries);

				int eventChoice =
					MenuHelper.selectFromList(filteredEvents.size(), "Select an event");
				Event selectedEvent = filteredEvents.get(eventChoice - 1);

				String msg =
					InputValidationUtil.readString(
						ScannerUtil.getScanner(),
						"Enter the message to be send to all registered users:\n"
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
	
	/* ===================== OFFER MANAGEMENT ===================== */
	private void offerManagementMenu() {

		while (true) {
			System.out.println("\nOffer management\n" + "1 Create offer\n" + "2 View my offers\n" + "3 Activate offer\n"
					+ "4 Deactivate offer\n" + "5 Back\n" + "Choice:");

			int choice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");

			switch (choice) {
				case 1: {
					offerManagementAction.createOffer(loggedInUser.getUserId());
					break;
				}
				case 2: {
				    offerManagementAction.viewAllOffers(loggedInUser.getUserId());
				    break;
				}
				case 3: {
				    offerManagementAction.activateOffer(loggedInUser.getUserId());
				    break;
				}
				case 4: {
				    offerManagementAction.deactivateOffer(loggedInUser.getUserId());
				    break;
				}
				case 5:{
					return;
				}default:
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
