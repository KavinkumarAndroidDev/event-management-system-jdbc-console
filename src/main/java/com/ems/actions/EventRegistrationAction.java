package com.ems.actions;

import java.util.List;

import com.ems.enums.PaymentMethod;
import com.ems.model.Event;
import com.ems.model.Ticket;
import com.ems.model.UserEventRegistration;
import com.ems.service.EventService;
import com.ems.util.ApplicationUtil;
import com.ems.util.InputValidationUtil;
import com.ems.util.MenuHelper;
import com.ems.util.ScannerUtil;

/*
 * Handles event registration and cancellation workflows.
 * Responsible for user interaction and validation only.
 */
public class EventRegistrationAction {


    private final EventService eventService;

    public EventRegistrationAction() {
        this.eventService = ApplicationUtil.eventService();
    }

    /**
     * Initiates registration flow for available events.
     *
     * @param userId the ID of the user
     */
    public void registerForAvailableEvent(int userId) {
        List<Event> events = eventService.listAvailableEvents();

        if (events == null || events.isEmpty()) {
            System.out.println("No events available for registration at the moment.");
            return;
        }

        performRegistration(userId, events);
    }

    /**
     * Performs the core event registration workflow.
     *
     * @param userId the ID of the user
     * @param events list of available events
     */
    private void performRegistration(int userId, List<Event> events) {
        MenuHelper.printEventSummaries(events);

        int eventChoice = MenuHelper.selectFromList(events.size(), "Select an event");

        Event selectedEvent = events.get(eventChoice - 1);
        int eventId = selectedEvent.getEventId();

        /**
         * Retrieves ticket types and validates availability.
         */
        List<Ticket> tickets = eventService.getTicketTypes(eventId);
        if (tickets == null || tickets.isEmpty()) {
            System.out.println("No ticket types available for this event.");
            return;
        }

        MenuHelper.printTicketSummaries(tickets);

        int ticketChoice = MenuHelper.selectFromList(tickets.size(), "Select a ticket type");

        Ticket selectedTicket = tickets.get(ticketChoice - 1);
        int ticketId = selectedTicket.getTicketId();

        /**
         * Reads ticket quantity with validation.
         */
        int quantity = readQuantity(selectedTicket.getAvailableQuantity());

        /**
         * Displays payment options and retrieves user selection.
         */
        PaymentMethod selectedMethod = selectPaymentMethod();
        if (selectedMethod == null) return;

        /**
         * Calculates and displays total amount.
         */
        double totalAmount = quantity * selectedTicket.getPrice();
        System.out.println("\nTotal amount: ₹" + String.format("%.2f", totalAmount));

        /**
         * Reads optional offer code.
         */
        String offerCode = InputValidationUtil.readString(
            ScannerUtil.getScanner(),
            "Enter offer code (press Enter to skip): "
        );

        if (offerCode == null || offerCode.trim().isEmpty()) {
            offerCode = "";
        }

        /**
         * Attempts event registration.
         */
        boolean success = eventService.registerForEvent(
            userId,
            eventId,
            ticketId,
            quantity,
            selectedTicket.getPrice(),
            selectedMethod,
            offerCode
        );

        if (success) {
            System.out.println("\nRegistration successful! Your tickets are confirmed.");
            System.out.println("Check 'My Registrations' for booking details.");
        } else {
            System.out.println("\nRegistration failed. Please try again or contact support.");
        }
    }

    /**
     * Cancels an existing event registration for the user.
     *
     * @param userId the ID of the user
     */
    public void cancelRegistration(int userId) {
        List<UserEventRegistration> upcoming = eventService.viewUpcomingEvents(userId);

        if (upcoming == null || upcoming.isEmpty()) {
            System.out.println("You have no upcoming events.");
            return;
        }

        MenuHelper.printEventsList(upcoming);

        int choice = MenuHelper.selectFromList(
            upcoming.size(),
            "Select a registration number"
        );

        UserEventRegistration registration = upcoming.get(choice - 1);

        char cancelChoice = InputValidationUtil.readChar(
            ScannerUtil.getScanner(),
            "Are you sure you want to cancel this registration? (Y/N): "
        );

        if (cancelChoice == 'Y' || cancelChoice == 'y') {
            boolean success = eventService.cancelRegistration(
                userId,
                registration.getRegistrationId()
            );

            if (success) {
                System.out.println("Registration cancelled successfully. Refund will be processed.");
            } else {
                System.out.println("Unable to cancel registration. Please contact support.");
            }
        } else {
            System.out.println("Cancellation aborted.");
        }
    }

    /* ===================== HELPER METHODS ===================== */

    /**
     * Reads ticket quantity with validation against available tickets.
     *
     * @param maxAvailable maximum available ticket quantity
     * @return validated ticket quantity
     */
    private int readQuantity(int maxAvailable) {
        if (maxAvailable <= 0) {
            System.out.println("No tickets available for this type.");
        }

        int quantity;

        while (true) {
            quantity = InputValidationUtil.readInt(
                ScannerUtil.getScanner(),
                "Enter number of tickets (1-" + maxAvailable + "): "
            );

            if (quantity >= 1 && quantity <= maxAvailable) {
                return quantity;
            }

            System.out.println("Please enter a quantity between 1 and " + maxAvailable + ".");
        }
    }

    /**
     * Displays available payment methods and retrieves user selection.
     *
     * @return selected payment method
     */
    private PaymentMethod selectPaymentMethod() {
        System.out.println("\nAvailable payment methods:");
        PaymentMethod[] methods = PaymentMethod.values();

        for (int i = 0; i < methods.length; i++) {
            System.out.println((i + 1) + ". " + methods[i].name().replace("_", " "));
        }

        int paymentChoice = MenuHelper.selectFromList(
            methods.length,
            "Select payment method"
        );

        return methods[paymentChoice - 1];
    }
}