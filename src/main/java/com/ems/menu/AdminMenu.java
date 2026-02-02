package com.ems.menu;

import com.ems.actions.AdminCategoryManagementAction;
import com.ems.actions.AdminEventManagementAction;
import com.ems.actions.AdminNotificationManagementAction;
import com.ems.actions.AdminOfferManagementAction;
import com.ems.actions.AdminReportAction;
import com.ems.actions.AdminTicketManagementAction;
import com.ems.actions.AdminUserManagementAction;
import com.ems.actions.AdminVenueManagementAction;
import com.ems.actions.NotificationAction;
import com.ems.actions.SystemLogAction;
import com.ems.enums.NotificationType;
import com.ems.model.User;
import com.ems.util.InputValidationUtil;
import com.ems.util.ScannerUtil;

/*
 * Handles administrator related console interactions.
 *
 * Responsibilities:
 * - Display admin menus and navigation flows
 * - Collect and validate user input
 * - Delegate administrative operations to services
 */
public class AdminMenu extends BaseMenu {

	private final AdminUserManagementAction userManagementAction;
	private final AdminEventManagementAction eventManagementAction;
	private final AdminCategoryManagementAction categoryManagementAction;
	private final AdminVenueManagementAction venueManagementAction;
	private final AdminNotificationManagementAction notificationManagementAction;
	private final AdminReportAction reportAction;
	private final AdminOfferManagementAction offerManagementAction;
	private final AdminTicketManagementAction ticketManagementAction;
	private final NotificationAction notificationAction;
	private final SystemLogAction systemLogAction;

	public AdminMenu(User user) {
		super(user);
		this.userManagementAction = new AdminUserManagementAction();
		this.eventManagementAction = new AdminEventManagementAction();
		this.categoryManagementAction = new AdminCategoryManagementAction();
		this.venueManagementAction = new AdminVenueManagementAction();
		this.notificationManagementAction = new AdminNotificationManagementAction();
		this.reportAction = new AdminReportAction();
		this.offerManagementAction = new AdminOfferManagementAction();
		this.ticketManagementAction = new AdminTicketManagementAction();
		this.notificationAction = new NotificationAction();
		this.systemLogAction = new SystemLogAction();
	}

	public void start() {
		while (true) {
			eventManagementAction.markCompletedEvents();
			System.out.println("\nAdmin Menu\n" + "1. User Management\n" + "2. Event Management\n"
					+ "3. Category Management\n" + "4. Venue Management\n" + "5. Ticket & Registration Management\n"
					+ "6. Payment & Revenue Management\n" + "7. Offer & Promotion Management\n"
					+ "8. Reports & Analytics\n" + "9. Notifications\n" + "10. Feedback Moderation\n"
					+ "11. Role Management\n" + "12. View system logs\n" + "13. Logout\n>" );

			int choice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");

			switch (choice) {
			case 1:
				userManagementMenu();
				break;
			case 2:
				eventManagementMenu();
				break;
			case 3:
				categoryManagementMenu();
				break;
			case 4:
				venueManagementMenu();
				break;
			case 5:
				ticketRegistrationManagementMenu();
				break;
			case 6:
				paymentRevenueManagementMenu();
				break;
			case 7:
				offerPromotionManagementMenu();
				break;
			case 8:
				reportsMenu();
				break;
			case 9:
				notificationMenu();
				break;
			case 10:
				feedbackModerationMenu();
				break;
			case 11:
				roleManagementMenu();
				break;
			case 12:
				systemLogAction.printAllLogs();
				break;
			case 13:
				eventManagementAction.markCompletedEvents();
				if (confirmLogout()) {
					System.out.println("Logging out...");
					return;
				}
				break;
			default:
				System.out.println("Invalid option. Please select a valid menu number.");
				break;
			}
		}
	}

	private void userManagementMenu() {
		while (true) {
			System.out.println("\nUser Management\n" + "1. View all users\n" + "2. View organizers\n" + "3. View admins\n"
					+ "4. Activate user\n" + "5. Suspend user\n" + "6. Back\n" + ">");

			int choice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");

			switch (choice) {

			case 1: {
				userManagementAction.listUsersByRole("Attendee");
				break;
			}

			case 2: {
				userManagementAction.listUsersByRole("Organizer");
				break;
			}
			case 3: {
				userManagementAction.listUsersByRole("Admin");
				break;
			}
			case 4: {
				userManagementAction.changeUserStatus("ACTIVE");
				break;
			}

			case 5: {
				userManagementAction.changeUserStatus("SUSPENDED");
				break;
			}

			case 6: {
				return;
			}

			default: {
				System.out.println("Invalid option. Please select a valid menu number.");
			}
			}
		}
	}


	private void eventManagementMenu() {

		while (true) {
			System.out.println("\nEvent Management\n" + "1. View all events\n" + "2. View event details\n"
					+ "3. View ticket options\n" + "4. Approve event\n" + "5. Cancel event\n" + "6. Back\n>");

			int choice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");

			switch (choice) {

			case 1: {
				eventManagementAction.listAllEvents();
				break;
			}

			case 2: {
				eventManagementAction.printEventDetails();
				break;
			}

			case 3: {
				eventManagementAction.listTicketsForEvent();
				break;
			}

			case 4: {
				eventManagementAction.approveEvent(loggedInUser.getUserId());
				break;
			}

			case 5: {
				eventManagementAction.cancelEvent();
				break;
			}

			case 6: {
				return;
			}

			default: {
				System.out.println("Invalid option. Please select a valid menu number.");
			}
			}
		}
	}

	private void reportsMenu() {
		while (true) {
			System.out.println("\nReports & Analytics\n" + "1. Event-wise registrations\n"
					+ "2. Organizer-wise performance\n" + "3. Revenue report\n" + "4. Back\n" + ">");

			int choice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");

			switch (choice) {
			case 1:
				reportAction.viewEventWiseRegistrations();
				break;
			case 2:
				reportAction.viewOrganizerReport();
				break;
			case 3:
				reportAction.viewRevenueReport();
				break;
			case 4:
				return;
			default:
				System.out.println("Invalid option. Please select a valid menu number.");
				break;
			}
		}
	}

	private void notificationMenu() {

		while (true) {
			System.out.println("\nNotifications\n" + "1. Send system update (all users)\n"
					+ "2. Send promotional message (all users)\n" + "3. Send notification to user role\n"
					+ "4. Send notification to specific user\n" + "5. View my notifications\n" + "6. Back\n>");

			int choice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");

			switch (choice) {

			case 1: {

				notificationManagementAction.sendSystemWideNotification(NotificationType.SYSTEM);
				break;
			}

			case 2: {
				notificationManagementAction.sendSystemWideNotification(NotificationType.EVENT);
				break;
			}

			case 3: {
				notificationManagementAction.sendNotificationByRole();
				break;
			}

			case 4: {
				notificationManagementAction.sendNotificationToSpecificUser();
				break;
			}

			case 5: {
				notificationAction.displayAllNotifications(loggedInUser.getUserId());
				break;
			}

			case 6: {
				return;
			}

			default: {
				System.out.println("Invalid option. Please select a valid menu number.");
			}
			}
		}
	}

	private void categoryManagementMenu() {

		while (true) {
			System.out.println("\nCategory Management\n" + "1. View all categories\n" + "2. Add new category\n"
					+ "3. Update category\n" + "4. Delete category\n" + "5. Back\n>");

			int choice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");

			switch (choice) {

			case 1: {
				categoryManagementAction.listAllCategories();
				break;
			}

			case 2: {
				categoryManagementAction.addCategory();
				break;
			}

			case 3: {
				categoryManagementAction.updateCategory();
				break;
			}

			case 4: {
				categoryManagementAction.deleteCategory();
				break;
			}

			case 5: {
				return;
			}

			default: {
				System.out.println("Invalid option. Please select a valid menu number.");
			}
			}
		}
	}

	

	private void venueManagementMenu() {

		while (true) {
			System.out.println(
					"\nVenue Management\n" + "1. View all venues\n" + "2. Add new venue\n" + "3. Update venue details\n"
							+ "4. Remove venue\n" + "5. View events at a venue\n" + "6. Back\n\n>");

			int choice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");

			switch (choice) {
				case 1: {
					venueManagementAction.listAllVenues();
					break;
				}
	
				case 2: {
					venueManagementAction.addVenue();
					break;
				}
				case 3: {
					venueManagementAction.updateVenue();
					break;
				}
	
				case 4: {
					venueManagementAction.removeVenue();
					break;
				}
	
				case 5: {
					venueManagementAction.listEventsByCity();
					break;
				}
	
				case 6: {
					return;
				}
	
				default: {
					System.out.println("Invalid option. Please select a valid menu number.");
				}
			}
		}
	}

	private void ticketRegistrationManagementMenu() {
		while (true) {
			System.out.println("\nTicket & Registration Management\n" + "1. View tickets by event\n"
					+ "2. View ticket availability summary\n" + "3. View registrations by event\n"
					+ "4. View registrations by user\n" + "5. Cancel a registration\n"
					+ "6. Restore cancelled registration\n" + "7. Back\n" + ">");

			int choice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");

			switch (choice) {

			case 1: {
				ticketManagementAction.viewTicketsByEvent();
				break;
			}

			case 2: {
				ticketManagementAction.viewTicketSummary();
				break;
			}

			case 3: {
				ticketManagementAction.viewEventRegistrations();
				break;
			}

			case 4:
				System.out.println("Feature coming soon");
				break;

			case 5:
				System.out.println("Cancellation flow will be added soon");
				break;

			case 6:
				System.out.println("Restore flow will be added soon");
				break;

			case 7:
				return;

			default:
				System.out.println("Invalid option. Please select a valid menu number.");
			}
		}
	}

	private void paymentRevenueManagementMenu() {
		while (true) {
			System.out.println("\nPayment & Revenue Management\n" + "1. View payments by event\n"
					+ "2. View payments by user\n" + "3. View failed payments\n" + "4. View payment summary\n"
					+ "5. Initiate refund\n" + "6. Back\n" + ">");

			int choice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");

			switch (choice) {
			case 6:
				return;

			default:
				System.out.println("This feature is under development and will be available soon.");
			}
		}
	}

	private void offerPromotionManagementMenu() {
		while (true) {
			System.out.println("\nOffer & Promotion Management\n" + "1. View all offers\n" + "2. Create new offer\n"
					+ "3. Activate or deactivate offer\n" + "4. View offer usage report\n" + "5. Back\n" + ">");

			int choice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");

			switch (choice) {
			case 1:
				offerManagementAction.viewAllOffers();
				break;
			case 2:
				offerManagementAction.createOffer();
				break;
			case 3:
				offerManagementAction.changeOfferStatus();
				break;

			case 4:
				offerManagementAction.viewOfferUsageReport();
				break;

			case 5:
				return;

			default:
				System.out.println("Invalid option. Please select a valid menu number.");
			}
		}
	}
	
	private void feedbackModerationMenu() {
		while (true) {
			System.out.println(
					"\nFeedback Moderation\n" + "1. View feedback by event\n" + "2. View feedback by organizer\n"
							+ "3. Delete feedback\n" + "4. Flag feedback as reviewed\n" + "5. Back\n" + ">");

			int choice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");

			switch (choice) {
			case 5:
				return;
			default:
				System.out.println("This feature is under development and will be available soon.");
			}
		}

	}

	private void roleManagementMenu() {
		while (true) {
			System.out.println("\nRole Management\n" + "1. View all roles\n" + "2. Create new role\n"
					+ "3. Assign role to user\n" + "4. Update role name\n" + "5. Delete role\n" + "6. Back\n" + ">");

			int choice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");

			switch (choice) {
			case 6:
				return;
			default:
				System.out.println("This feature is under development and will be available soon.");
			}
		}
	}

	private boolean confirmLogout() {
		char choice = InputValidationUtil.readChar(ScannerUtil.getScanner(), "Are you sure you want to log out? (Y/N): ");
		return Character.toUpperCase(choice) == 'Y';
	}
}