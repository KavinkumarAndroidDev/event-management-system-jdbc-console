package com.ems.actions;

import java.util.List;

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
import com.ems.util.InputValidationUtil;
import com.ems.util.MenuHelper;
import com.ems.util.ScannerUtil;

public class AdminReportAction {
    private final AdminService adminService;
    private final EventService eventService;
    private final OrganizerService organizerService;
    
    public AdminReportAction() {
        this.adminService = ApplicationUtil.adminService();
        this.eventService = ApplicationUtil.eventService();
        this.organizerService = ApplicationUtil.organizerService();
    }
    
    
    public void viewEventWiseRegistrations(){
    	List<Event> events = eventService.getAllEvents();
    	if(events.isEmpty()) {
    		System.out.println("No events available at the moment");
    		return;
    	}
    	AdminMenuHelper.printAllEventsWithStatus(events);
    	int selectedChoice = MenuHelper.selectFromList(events.size(), "Select an event");
    	Event selectedEvent = events.get(selectedChoice - 1);
		if (selectedEvent == null)
			return;

		List<EventRegistrationReport> reports = getEventWiseRegistrations(selectedEvent.getEventId());
		AdminMenuHelper.printEventRegistrationReport(reports);
    }
    
    public void viewOrganizerReport() {
    	List<User> user = adminService.getUsersList(UserRole.ORGANIZER.toString());
		MenuHelper.displayUsers(user);
		int organizerChoice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "Enter the valid choice (1 - " + user.size() +"): ");
		while(organizerChoice < 1 || organizerChoice > user.size()) {
			organizerChoice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "Enter the valid choice (1 - " + user.size() +"): ");
		}
		List<OrganizerEventSummary> list =
	            getOrganizerEventSummary(user.get(organizerChoice - 1).getUserId());

	    if (list.isEmpty()) {
	        System.out.println("No event conducted by the organizer!");
	        return;
	    }

	    AdminMenuHelper.printOrganizerEventSummary(list);
    }
    
    public List<EventRegistrationReport>  getEventWiseRegistrations(int eventId) {
        return adminService.getEventWiseRegistrations(eventId);
    }

    public List<OrganizerEventSummary> getOrganizerEventSummary(int organizerId) {
        return organizerService.getOrganizerEventSummary(organizerId);
    }

    public List<EventRevenueReport> getRevenueReport() {
        return adminService.getRevenueReport();
    }


    public void viewRevenueReport() {

        List<EventRevenueReport> reports = getRevenueReport();

        AdminMenuHelper.printEventRevenueReport(reports);
    }

}