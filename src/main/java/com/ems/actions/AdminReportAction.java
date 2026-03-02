package com.ems.actions;

import java.util.List;
import com.ems.exception.DataAccessException;

import com.ems.enums.UserRole;
import com.ems.model.Event;
import com.ems.model.EventRegistrationReport;
import com.ems.model.EventRevenueReport;
import com.ems.model.OrganizerEventSummary;
import com.ems.model.User;
import com.ems.service.AdminService;
import com.ems.service.EventService;
import com.ems.service.OrganizerService;
import com.ems.util.AdminMenuHelper;
import com.ems.util.ApplicationUtil;
import com.ems.util.MenuHelper;
import com.ems.util.PaginationUtil;

public class AdminReportAction {

    private final AdminService adminService;
    private final EventService eventService;
    private final OrganizerService organizerService;

    public AdminReportAction() {
        this.adminService = ApplicationUtil.adminService();
        this.eventService = ApplicationUtil.eventService();
        this.organizerService = ApplicationUtil.organizerService();
    }

    public void viewEventWiseRegistrations() {
        try {
            List<Event> events = eventService.getAllEvents();
            if (events.isEmpty()) {
                System.out.println("No events available at the moment.");
                return;
            }

            PaginationUtil.paginate(events, AdminMenuHelper::printAllEventsWithStatus);

            int selectedChoice = MenuHelper.selectFromList(events.size(), "Select an event");
            Event selectedEvent = events.get(selectedChoice - 1);

            List<EventRegistrationReport> reports = getEventWiseRegistrations(selectedEvent.getEventId());
            PaginationUtil.paginate(reports, AdminMenuHelper::printEventRegistrationReport);
        } catch (DataAccessException e) {
            System.out.println("Error viewing event registrations: " + e.getMessage());
        }
    }

    public void viewOrganizerReport() {
        try {
            List<User> users = adminService.getUsersList(UserRole.ORGANIZER.name());
            if (users.isEmpty()) {
                System.out.println("No organizers found.");
                return;
            }

            PaginationUtil.paginate(users, MenuHelper::displayUsers);

            int organizerChoice = MenuHelper.selectFromList(users.size(), "Select an organizer");
            List<OrganizerEventSummary> summary = getOrganizerEventSummary(users.get(organizerChoice - 1).getUserId());

            if (summary.isEmpty()) {
                System.out.println("No events conducted by this organizer.");
                return;
            }

            PaginationUtil.paginate(summary, AdminMenuHelper::printOrganizerEventSummary);
        } catch (DataAccessException e) {
            System.out.println("Error viewing organizer report: " + e.getMessage());
        }
    }

    public void viewRevenueReport() {
        try {
            List<EventRevenueReport> reports = getRevenueReport();
            AdminMenuHelper.printEventRevenueReport(reports);
        } catch (DataAccessException e) {
            System.out.println("Error viewing revenue report: " + e.getMessage());
        }
    }

    // -----------------------------------------------------------------------
    // Data access helpers
    // -----------------------------------------------------------------------

    public List<EventRegistrationReport> getEventWiseRegistrations(int eventId) throws DataAccessException {
        return adminService.getEventWiseRegistrations(eventId);
    }

    public List<OrganizerEventSummary> getOrganizerEventSummary(int organizerId) throws DataAccessException {
        return organizerService.getOrganizerEventSummary(organizerId);
    }

    public List<EventRevenueReport> getRevenueReport() throws DataAccessException {
        return adminService.getRevenueReport();
    }
}