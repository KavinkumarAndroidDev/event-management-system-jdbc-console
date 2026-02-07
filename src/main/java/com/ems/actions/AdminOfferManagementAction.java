package com.ems.actions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.ems.model.Event;
import com.ems.model.Offer;
import com.ems.service.EventService;
import com.ems.service.OfferService;
import com.ems.util.AdminMenuHelper;
import com.ems.util.ApplicationUtil;
import com.ems.util.DateTimeUtil;
import com.ems.util.InputValidationUtil;
import com.ems.util.MenuHelper;
import com.ems.util.ScannerUtil;

public class AdminOfferManagementAction {
    private final OfferService offerService;
    private final EventService eventService;

    public AdminOfferManagementAction() {
        this.offerService = ApplicationUtil.offerService();
        this.eventService = ApplicationUtil.eventService();
    }

    public List<Offer> getAllOffers() {
        return offerService.getAllOffers();
    }

    public boolean createOffer(int eventId, String code, int discount, LocalDateTime from, LocalDateTime to) {
        return offerService.createOffer(eventId, code, discount, from, to);
    }

    public void toggleOfferStatus(int offerId, LocalDateTime newValidTo) {
        offerService.toggleOfferStatus(offerId, newValidTo);
    }

    public Map<String, Integer> getOfferUsageReport() {
        return offerService.getOfferUsageReport();
    }
    
    public void viewAllOffers() {
    	List<Offer> offers = getAllOffers();
		if (offers.isEmpty()) {
			System.out.println("No offers found.");
		} else {
			AdminMenuHelper.printOffers(offers);
		}
    }
    
    
    public void viewOfferUsageReport(){
    	Map<String, Integer> report = getOfferUsageReport();
		AdminMenuHelper.printOfferUsageReport(report);
    }
	public void changeOfferStatus() {

		System.out.println("\n1. Activate offer\n" + "2. Deactivate offer\n" + "3. Back\n" + ">");

		int option = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");

		if (option == 3) {
			return;
		}

		List<Offer> offers = getAllOffers();

		if (offers.isEmpty()) {
			System.out.println("No offers found.");
			return;
		}

		List<Offer> filtered;
		if (option == 1) {
			filtered = AdminMenuHelper.filterExpiredOffers(offers);
		} else if (option == 2) {
			filtered = AdminMenuHelper.filterActiveOffers(offers);
		} else {
			System.out.println("Invalid option. Please select a valid menu number.");
			return;
		}

		if (filtered.isEmpty()) {
			System.out.println("No applicable offers found");
			return;
		}

		AdminMenuHelper.printOffers(filtered);

		int choice = InputValidationUtil.readInt(ScannerUtil.getScanner(),
				"Select offer (1-" + filtered.size() + "): ");

		while (choice < 1 || choice > filtered.size()) {
			choice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "Enter a valid choice: ");
		}

		Offer selectedOffer = filtered.get(choice - 1);

		LocalDateTime newValidTo;

		if (option == 1) {
			String dateInput = InputValidationUtil.readString(ScannerUtil.getScanner(),"Activate until (dd-MM-yyyy HH:mm): ");
			newValidTo = DateTimeUtil.parseLocalDateTime(dateInput);
		} else {
			newValidTo = LocalDateTime.now();
		}
		Event event = eventService.getEventById(selectedOffer.getEventId());
		if (event == null) {
			System.out.println("No event found for the offer!");
			return;
		}
		if (newValidTo.isAfter(event.getStartDateTime())) {
			System.out.println("Offer validity must end before the event starts.");
			return;
		}

		char updateChoice = InputValidationUtil.readChar(ScannerUtil.getScanner(),
				"Are you sure you want to update offer status (Y/N)\n");
		if (updateChoice == 'Y' || updateChoice == 'y') {
			toggleOfferStatus(selectedOffer.getOfferId(), newValidTo);

			System.out.println(option == 1 ? "Offer activated successfully." : "Offer deactivated successfully.");
		} else {
			System.out.println("Process aborted!");
		}

	}
	
	
	public void createOffer() {

		List<Event> events = eventService.listAvailableEvents();

		if (events.isEmpty()) {
			System.out.println("No events available");
			return;
		}

		MenuHelper.printEventSummaries(events);

		int eChoice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "Select event (1-" + events.size() + "): ");

		while (eChoice < 1 || eChoice > events.size()) {
			eChoice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "Enter a valid choice: ");
		}

		Event event = events.get(eChoice - 1);

		String code = InputValidationUtil.readNonEmptyString(ScannerUtil.getScanner(), "Enter the offer code: ");

		int discount = InputValidationUtil.readInt(ScannerUtil.getScanner(), "Enter the discount percentage: ");
		while (discount < 0 || discount > 100) {
			discount = InputValidationUtil.readInt(ScannerUtil.getScanner(), "Enter the discount percentage (1 - 100): ");
		}
		/*
		 * Offer start date must: - Not be in the past - Not exceed the event start time
		 * 
		 * This ensures offers are only active before the event begins.
		 */
		LocalDateTime from = null;

		while (from == null) {
		    String input = InputValidationUtil.readString(
		            ScannerUtil.getScanner(),
		            "Enter the valid from (dd-MM-yyyy HH:mm): "
		    );

		    from = DateTimeUtil.parseLocalDateTime(input);

		    if (from == null
		            || from.isBefore(LocalDateTime.now())
		            || from.isAfter(event.getStartDateTime())) {

		        System.out.println("Invalid 'from' date time. Please try again.");
		        from = null;
		    }
		}


		/*
		 * Offer end date must: - Not be in the past - Be after the offer start date -
		 * Not exceed the event start time
		 * 
		 * This prevents invalid or overlapping offer periods.
		 */
		LocalDateTime to = null;

		while (to == null) {
		    String input = InputValidationUtil.readString(
		            ScannerUtil.getScanner(),
		            "Enter the valid to (dd-MM-yyyy HH:mm): "
		    );

		    to = DateTimeUtil.parseLocalDateTime(input);

		    if (to == null
		            || to.isBefore(LocalDateTime.now())
		            || to.isBefore(from)
		            || to.isAfter(event.getStartDateTime())) {

		        System.out.println("Invalid 'to' date time. Please try again.");
		        to = null;
		    }
		}

		boolean isCreated = createOffer(event.getEventId(), code, discount, from, to);

		if(isCreated) {
			System.out.println("Offer created successfully. Offer: " + code);
		}else {
			System.out.println("Offer creation failed");
		}
	}
}