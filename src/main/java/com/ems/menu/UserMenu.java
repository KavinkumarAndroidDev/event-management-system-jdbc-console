package com.ems.menu;

import com.ems.actions.EventBrowsingAction;
import com.ems.actions.EventRegistrationAction;
import com.ems.actions.EventSearchAction;
import com.ems.actions.FeedbackAction;
import com.ems.actions.NotificationAction;
import com.ems.actions.UserAction;
import com.ems.actions.UserRegistrationAction;
import com.ems.model.User;
import com.ems.util.InputValidationUtil;
import com.ems.util.ScannerUtil;

/*
 * Handles all user-facing menu navigation.
 * Delegates actual work to action classes.
 */
public class UserMenu extends BaseMenu {

	private final NotificationAction notificationAction;
	private final EventBrowsingAction eventBrowsingAction;
	private final EventRegistrationAction eventRegistrationAction;
	private final UserRegistrationAction userRegistrationAction;
	private final EventSearchAction eventSearchAction;
	private final FeedbackAction feedbackAction;
	private final UserAction userAction;

	public UserMenu(User user) {
		super(user);
		this.notificationAction = new NotificationAction();
		this.eventBrowsingAction = new EventBrowsingAction();
		this.eventRegistrationAction = new EventRegistrationAction();
		this.userRegistrationAction = new UserRegistrationAction();
		this.eventSearchAction = new EventSearchAction();
		this.feedbackAction = new FeedbackAction();
		this.userAction = new UserAction();
	}

    /**
     * Starts the main user menu loop.
     * Displays unread notifications once on entry.
     */
    public void start() {
        notificationAction.displayUnreadNotifications(loggedInUser.getUserId());

        while (true) {

        	System.out.println(
        		    "\nUser menu\n" +
        		    "1 Browse events\n" +
        		    "2 Search and filter events\n" +
        		    "3 My registrations\n" +
        		    "4 Notifications\n" +
        		    "5 Feedback\n" + 
        		    "6 Update profile\n" +
        		    "7 Logout\n\n" +
        		    "Choice:"
        		);

            int choice = InputValidationUtil.readInt(
                ScannerUtil.getScanner(),
                ""
            );

            switch (choice) {
            case 1:
                browseEventsMenu();
                break;
            case 2:
                searchEvents();
                break;
            case 3:
                registrationMenu();
                break;
            case 4:
                notificationAction.displayAllNotifications(
                    loggedInUser.getUserId()
                );
                break;
            case 5:
                feedbackMenu();
                break;
            case 6:
            	userAction.updateProfile(loggedInUser);
				break;
            case 7:
                if (confirmLogout()) {
                    System.out.println("Logging out...");
                    return;
                }
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /* ===================== MENUS ===================== */

    /**
     * Displays the browse events menu.
     * Allows users to view events, event details, ticket options,
     * and perform event registration.
     */
    private void browseEventsMenu() {
        while (true) {
            System.out.println(
            	    "\nBrowse events\n" +
            	    "1 View all events\n" +
            	    "2 View event details\n" +
            	    "3 View ticket options\n" +
            	    "4 Register for event\n" +
            	    "5 Back\n\n" +
            	    "Choice:"
            	);


            int choice = InputValidationUtil.readInt(
                ScannerUtil.getScanner(),
                ""
            );

            switch (choice) {
            case 1:
                eventBrowsingAction.printAllAvailableEvents();
                break;
            case 2:
                eventBrowsingAction.viewEventDetails();
                break;
            case 3:
                eventBrowsingAction.viewTicketOptions();
                break;
            case 4:
                eventRegistrationAction.registerForAvailableEvent(
                    loggedInUser.getUserId()
                );
                break;
            case 5:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the search and filter events menu.
     */
    public void searchEvents() {
        while (true) {
            System.out.println(
            	    "\nSearch and filter events\n" +
            	    "1 Search by category\n" +
            	    "2 Search by date\n" +
            	    "3 Search by date range\n" +
            	    "4 Search by city\n" +
            	    "5 Filter by price\n" +
            	    "6 View all events\n" +
            	    "7 Back\n\n" +
            	    "Choice:"
            	);


            int filterChoice = InputValidationUtil.readInt(
                ScannerUtil.getScanner(),
                ""
            );

            switch (filterChoice) {
            case 1:
                eventSearchAction.handleSearchByCategory();
                break;
            case 2:
                eventSearchAction.handleSearchByDate();
                break;
            case 3:
                eventSearchAction.handleSearchByDateRange();
                break;
            case 4:
                eventSearchAction.handleSearchByCity();
                break;
            case 5:
                eventSearchAction.handleFilterByPrice();
                break;
            case 6:
                eventBrowsingAction.printAllAvailableEvents();
                break;
            case 7:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the user registration management menu.
     * Allows users to view and cancel registrations.
     */
    private void registrationMenu() {
        while (true) {
        	System.out.println(
        		    "\nMy registrations\n" +
        		    "1 Upcoming events\n" +
        		    "2 Past events\n" +
        		    "3 Booking details\n" +
        		    "4 Cancel registration\n" +
        		    "5 Back\n\n" +
        		    "Choice:"
        		);

            int choice = InputValidationUtil.readInt(
                ScannerUtil.getScanner(),
                ""
            );

            switch (choice) {
            case 1:
                userRegistrationAction.listUpcomingEvents(
                    loggedInUser.getUserId()
                );
                break;
            case 2:
                userRegistrationAction.listPastEvents(
                    loggedInUser.getUserId()
                );
                break;
            case 3:
                userRegistrationAction.viewBookingDetails(
                    loggedInUser.getUserId()
                );
                break;
            case 4:
                eventRegistrationAction.cancelRegistration(
                    loggedInUser.getUserId()
                );
                break;
            case 5:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the feedback menu.
     * Allows users to submit ratings for past events.
     */
    private void feedbackMenu() {
        while (true) {
        	System.out.println(
        		    "\nFeedback\n" +
        		    "1 Submit rating\n" +
        		    "2 Back\n\n" +
        		    "Choice:"
        		);
            int choice = InputValidationUtil.readInt(
                ScannerUtil.getScanner(),
                ""
            );
            switch (choice) {
            case 1:
                feedbackAction.submitRating(
                    loggedInUser.getUserId()
                );
                break;
            case 2:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Prompts the user for logout confirmation.
     *
     * @return true if logout is confirmed
     */
    private boolean confirmLogout() {
        char choice = InputValidationUtil.readChar(
            ScannerUtil.getScanner(),
            "Are you sure you want to logout? (Y/N): "
        );
        return Character.toUpperCase(choice) == 'Y';
    }
}