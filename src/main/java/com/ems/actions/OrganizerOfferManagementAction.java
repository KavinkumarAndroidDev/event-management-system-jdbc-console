package com.ems.actions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import com.ems.exception.DataAccessException;

import com.ems.model.Event;
import com.ems.model.Offer;
import com.ems.service.EventService;
import com.ems.service.OfferService;
import com.ems.service.OrganizerService;
import com.ems.util.AdminMenuHelper;
import com.ems.util.ApplicationUtil;
import com.ems.util.DateTimeUtil;
import com.ems.util.InputValidationUtil;
import com.ems.util.MenuHelper;
import com.ems.util.PaginationUtil;
import com.ems.util.ScannerUtil;

public class OrganizerOfferManagementAction {

    private final OfferService offerService;
    private final EventService eventService;
    private final OrganizerService organizerService;

    public OrganizerOfferManagementAction() {
        this.offerService = ApplicationUtil.offerService();
        this.eventService = ApplicationUtil.eventService();
        this.organizerService = ApplicationUtil.organizerService();
    }

    public List<Offer> getAllOffers(int userId) throws DataAccessException {

        List<Event> myEvents = organizerService.getOrganizerEvents(userId);

        Set<Integer> eventIds = myEvents.stream()
                .map(Event::getEventId)
                .collect(Collectors.toSet());

        return offerService.getAllOffers()
                .stream()
                .filter(o -> eventIds.contains(o.getEventId()))
                .collect(Collectors.toList());
    }

    public boolean createOffer(int eventId, String code, int discount,
            LocalDateTime from, LocalDateTime to) throws DataAccessException {

        return offerService.createOffer(eventId, code, discount, from, to);
    }

    public void toggleOfferStatus(int offerId, LocalDateTime newValidTo) throws DataAccessException {
        offerService.toggleOfferStatus(offerId, newValidTo);
    }

    public Map<String, Integer> getOfferUsageReport(int userId) throws DataAccessException {

        List<Offer> myOffers = getAllOffers(userId);

        Set<String> myCodes = myOffers.stream()
                .map(Offer::getCode)
                .collect(Collectors.toSet());

        return offerService.getOfferUsageReport()
                .entrySet()
                .stream()
                .filter(e -> myCodes.contains(e.getKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue));
    }

    public void viewAllOffers(int userId) {
        try {
            List<Offer> offers = getAllOffers(userId);

            if (offers.isEmpty()) {
                System.out.println("No offers found.");
            } else {
                PaginationUtil.paginate(offers, AdminMenuHelper::printOffers);
            }
        } catch (DataAccessException e) {
            System.out.println("Error viewing offers: " + e.getMessage());
        }
    }

    public void viewOfferUsageReport(int userId) {
        try {
            Map<String, Integer> report = getOfferUsageReport(userId);

            if (report.isEmpty()) {
                System.out.println("No usage data found.");
                return;
            }

            AdminMenuHelper.printOfferUsageReport(report);
        } catch (DataAccessException e) {
            System.out.println("Error viewing usage report: " + e.getMessage());
        }
    }

    public void activateOffer(int userId) {
        try {
            List<Offer> offers = getAllOffers(userId);

            if (offers.isEmpty()) {
                System.out.println("No offers found.");
                return;
            }

            List<Offer> filtered = AdminMenuHelper.filterExpiredOffers(offers);

            if (filtered.isEmpty()) {
                System.out.println("No expired offers available.");
                return;
            }

            PaginationUtil.paginate(filtered, AdminMenuHelper::printOffers);

            int choice = InputValidationUtil.readInt(
                    ScannerUtil.getScanner(),
                    "Select offer (1-" + filtered.size() + "): ");

            while (choice < 1 || choice > filtered.size()) {
                choice = InputValidationUtil.readInt(
                        ScannerUtil.getScanner(),
                        "Enter valid choice: ");
            }

            Offer selectedOffer = filtered.get(choice - 1);

            Event event = eventService.getEventById(selectedOffer.getEventId());

            if (event == null || event.getOrganizerId() != userId) {
                System.out.println("Unauthorized action.");
                return;
            }

            String dateInput = InputValidationUtil.readString(
                    ScannerUtil.getScanner(),
                    "Activate until (dd-MM-yyyy HH:mm): ");

            LocalDateTime newValidTo = DateTimeUtil.parseLocalDateTime(dateInput);

            if (newValidTo == null ||
                    DateTimeUtil.toUtcInstant(newValidTo)
                            .isAfter(event.getStartDateTime())) {

                System.out.println("Invalid validity date.");
                return;
            }

            toggleOfferStatus(selectedOffer.getOfferId(), newValidTo);

            System.out.println("Offer activated successfully.");
        } catch (DataAccessException e) {
            System.out.println("Error activating offer: " + e.getMessage());
        }
    }

    public void deactivateOffer(int userId) {
        try {
            List<Offer> offers = getAllOffers(userId);

            if (offers.isEmpty()) {
                System.out.println("No offers found.");
                return;
            }

            List<Offer> filtered = AdminMenuHelper.filterActiveOffers(offers);

            if (filtered.isEmpty()) {
                System.out.println("No active offers available.");
                return;
            }

            PaginationUtil.paginate(filtered, AdminMenuHelper::printOffers);

            int choice = InputValidationUtil.readInt(
                    ScannerUtil.getScanner(),
                    "Select offer (1-" + filtered.size() + "): ");

            while (choice < 1 || choice > filtered.size()) {
                choice = InputValidationUtil.readInt(
                        ScannerUtil.getScanner(),
                        "Enter valid choice: ");
            }

            Offer selectedOffer = filtered.get(choice - 1);

            Event event = eventService.getEventById(selectedOffer.getEventId());

            if (event == null || event.getOrganizerId() != userId) {
                System.out.println("Unauthorized action.");
                return;
            }

            LocalDateTime now = DateTimeUtil.toLocalDateTime(DateTimeUtil.nowUtc());

            toggleOfferStatus(selectedOffer.getOfferId(), now);

            System.out.println("Offer deactivated successfully.");
        } catch (DataAccessException e) {
            System.out.println("Error deactivating offer: " + e.getMessage());
        }
    }

    public void createOffer(int userId) {
        try {
            List<Event> events = organizerService.getOrganizerEvents(userId);

            if (events.isEmpty()) {
                System.out.println("No events available.");
                return;
            }

            PaginationUtil.paginate(events, MenuHelper::printEventSummaries);
            
            int eChoice = InputValidationUtil.readInt(
                    ScannerUtil.getScanner(),
                    "Select event (1-" + events.size() + "): ");

            while (eChoice < 1 || eChoice > events.size()) {
                eChoice = InputValidationUtil.readInt(
                        ScannerUtil.getScanner(),
                        "Enter a valid choice: ");
            }

            Event event = events.get(eChoice - 1);

            String code = InputValidationUtil.readNonEmptyString(
                    ScannerUtil.getScanner(),
                    "Enter the offer code: ");

            int discount = InputValidationUtil.readInt(
                    ScannerUtil.getScanner(),
                    "Enter the discount percentage: ");

            while (discount < 0 || discount > 100) {
                discount = InputValidationUtil.readInt(
                        ScannerUtil.getScanner(),
                        "Enter the discount percentage (1 - 100): ");
            }

            LocalDateTime from = null;

            while (from == null) {

                String input = InputValidationUtil.readString(
                        ScannerUtil.getScanner(),
                        "Enter the valid from (dd-MM-yyyy HH:mm): ");

                from = DateTimeUtil.parseLocalDateTime(input);

                if (from == null
                        || DateTimeUtil.toUtcInstant(from)
                                .isBefore(DateTimeUtil.nowUtc())
                        || DateTimeUtil.toUtcInstant(from)
                                .isAfter(event.getStartDateTime())) {

                    System.out.println("Invalid 'from' date time. Please try again.");
                    from = null;
                }
            }

            LocalDateTime to = null;

            while (to == null) {

                String input = InputValidationUtil.readString(
                        ScannerUtil.getScanner(),
                        "Enter the valid to (dd-MM-yyyy HH:mm): ");

                to = DateTimeUtil.parseLocalDateTime(input);

                if (to == null
                        || DateTimeUtil.toUtcInstant(to)
                                .isBefore(DateTimeUtil.nowUtc())
                        || to.isBefore(from)
                        || DateTimeUtil.toUtcInstant(to)
                                .isAfter(event.getStartDateTime())) {

                    System.out.println("Invalid 'to' date time. Please try again.");
                    to = null;
                }
            }

            boolean isCreated = createOffer(
                    event.getEventId(),
                    code,
                    discount,
                    from,
                    to);

            if (isCreated) {
                System.out.println("Offer created successfully. Offer: " + code);
            } else {
                System.out.println("Offer creation failed");
            }
        } catch (DataAccessException e) {
            System.out.println("Error creating offer: " + e.getMessage());
        }
    }
}