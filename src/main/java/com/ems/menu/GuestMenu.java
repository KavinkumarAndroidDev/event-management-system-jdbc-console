package com.ems.menu;

import java.util.List;

import com.ems.actions.EventSelectionAction;
import com.ems.actions.TicketAction;
import com.ems.actions.UserAction;
import com.ems.enums.UserRole;
import com.ems.model.Event;
import com.ems.model.Ticket;
import com.ems.util.InputValidationUtil;
import com.ems.util.MenuHelper;
import com.ems.util.ScannerUtil;

/*
 * Handles guest user console interactions.
 *
 * Responsibilities:
 * - Display guest accessible menus
 * - Allow browsing events and ticket information
 * - Guide guests through account registration
 */
public class GuestMenu extends BaseMenu {
	UserAction userAction = new UserAction();
    EventSelectionAction eventAction = new EventSelectionAction();
    TicketAction ticketAction = new TicketAction();

    public GuestMenu() {
        super(null);
    }
	public void start() {
		System.out.println("Guest access provides limited features.\n");
		while(true) {
			System.out.println(
				    "\nGuest menu\n" +
				    "1 Browse events\n" +
				    "2 Register account\n" +
				    "3 Exit guest mode\n\n" +
				    "Choice:"
				);
			int input = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");
			switch(input) {
			case 1:
                browseEventsMenu();
                break;
			case 2:
				createAccount(UserRole.ATTENDEE);
				break;
			case 3:
				System.out.println("Returning to main menu.\n");
			    return;   
			default:
				System.out.println("Please select a valid option from the menu.");
				break;
			}
			
		}
	}
	
	private void createAccount(UserRole role) {
        userAction.createAccount(role);
    }
	
	private void browseEventsMenu() {

		while (true) {
			System.out.println(
				    "\nBrowse events\n" +
				    "1 View all events\n" +
				    "2 View event details\n" +
				    "3 View ticket options\n" +
				    "4 Back\n\n" +
				    "Choice:"
				);

			int choice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");

			switch (choice) {
			case 1:
				printAllAvailableEvents();
				break;
			case 2:
				viewEventDetails();
				break;
			case 3:
				viewTicketOptions();
				break;
			case 4:
				return;
			default:
				System.out.println("Invalid choice. Please try again.");
			}
		}
	}
	private void printAllAvailableEvents() {
		List<Event> filteredEvents = eventAction.getAvailableEvents();
		if(filteredEvents.isEmpty()) {
			System.out.println("No events available at the moment.\n");
			return;
		}
		MenuHelper.printEventSummaries(filteredEvents);
	}
	
	private void viewEventDetails() {

	    List<Event> events = eventAction.getAvailableEvents();
	    if (events.isEmpty()) {
	        System.out.println("No events	 available at the moment.\n");
	        return;
	    }

	    MenuHelper.printEventSummaries(events);

	    int choice = InputValidationUtil.readInt(
	        ScannerUtil.getScanner(),
	        "Select an event (1-" + events.size() + "): "
	    );

	    Event selectedEvent = eventAction.getEventByIndex(events, choice);
	    if (selectedEvent == null) {
	        System.out.println("Invalid selection.");
	        return;
	    }

	    MenuHelper.printEventDetails(selectedEvent);
	}
	
	private void viewTicketOptions() {
	
	    List<Event> events = eventAction.getAvailableEvents();
	    if (events.isEmpty()) {
	        System.out.println("No events available at the moment.\n");
	        return;
	    }
	
	    MenuHelper.printEventSummaries(events);
	
	    int choice = InputValidationUtil.readInt(
	        ScannerUtil.getScanner(),
	        "Select an event (1-" + events.size() + "): "
	    );
	
	    Event selectedEvent = eventAction.getEventByIndex(events, choice);
	    if (selectedEvent == null) {
	        System.out.println("Invalid selection.");
	        return;
	    }
	
	    List<Ticket> tickets =
	        ticketAction.getTicketsForEvent(selectedEvent.getEventId());
	
	    if (tickets.isEmpty()) {
	    	System.out.println("No ticket options available.\n");
	        return;
	    }
	
	    MenuHelper.printTicketSummaries(tickets);
	}



}