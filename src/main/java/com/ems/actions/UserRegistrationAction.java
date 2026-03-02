package com.ems.actions;

import java.util.List;

import com.ems.model.BookingDetail;
import com.ems.model.UserEventRegistration;
import com.ems.service.EventService;
import com.ems.util.ApplicationUtil;
import com.ems.util.MenuHelper;
import com.ems.util.PaginationUtil;
import com.ems.exception.DataAccessException;

/*
 * Handles viewing of user registration history and booking details.
 */
public class UserRegistrationAction {

    private final EventService eventService;

    public UserRegistrationAction() {
        this.eventService = ApplicationUtil.eventService();
    }

    /**
     * Displays a list of upcoming events registered by the user.
     *
     * @param userId the ID of the user
     */
    public void listUpcomingEvents(int userId) {
        try {
            List<UserEventRegistration> upcoming = getUpcomingEvents(userId);

            if (upcoming == null || upcoming.isEmpty()) {
                System.out.println("\nYou have no upcoming events.");
                return;
            }

            // printEventsList is display-only here; lambda discards startIndex
            PaginationUtil.paginate(upcoming,
                    (page, i) -> MenuHelper.printEventsList(page));
        } catch (DataAccessException e) {
            System.out.println("Error listing upcoming events: " + e.getMessage());
        }
    }

    /**
     * Displays a list of past events registered by the user.
     *
     * @param userId the ID of the user
     */
    public void listPastEvents(int userId) {
        try {
            List<UserEventRegistration> past = getPastEvents(userId);

            if (past == null || past.isEmpty()) {
                System.out.println("\nYou have no past events.");
                return;
            }

            // printEventsList is display-only here; lambda discards startIndex
            PaginationUtil.paginate(past,
                    (page, i) -> MenuHelper.printEventsList(page));
        } catch (DataAccessException e) {
            System.out.println("Error listing past events: " + e.getMessage());
        }
    }

    /**
     * Displays booking details for all events registered by the user.
     *
     * @param userId the ID of the user
     */
    public void viewBookingDetails(int userId) {
        try {
            List<BookingDetail> bookingDetails = getBookingDetails(userId);

            if (bookingDetails == null || bookingDetails.isEmpty()) {
                System.out.println("You have no bookings yet.");
                return;
            }

            // printBookingDetails is display-only; lambda discards startIndex
            PaginationUtil.paginate(bookingDetails,
                    (page, i) -> MenuHelper.printBookingDetails(page));
        } catch (DataAccessException e) {
            System.out.println("Error viewing booking details: " + e.getMessage());
        }
    }

    /* ===================== DATA RETRIEVAL METHODS ===================== */

    /**
     * Retrieves booking details for a user.
     *
     * @param userId the ID of the user
     * @return list of booking details
     */
    public List<BookingDetail> getBookingDetails(int userId) throws DataAccessException {
        return eventService.viewBookingDetails(userId);
    }

    /**
     * Retrieves upcoming events registered by the user.
     *
     * @param userId the ID of the user
     * @return list of upcoming user event registrations
     */
    public List<UserEventRegistration> getUpcomingEvents(int userId) throws DataAccessException {
        return eventService.viewUpcomingEvents(userId);
    }

    /**
     * Retrieves past events registered by the user.
     *
     * @param userId the ID of the user
     * @return list of past user event registrations
     */
    public List<UserEventRegistration> getPastEvents(int userId) throws DataAccessException {
        return eventService.viewPastEvents(userId);
    }
}