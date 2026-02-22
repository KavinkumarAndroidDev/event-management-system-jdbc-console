package com.ems.service;

import java.time.LocalDateTime;
import java.util.List;

import com.ems.model.Event;
import com.ems.model.EventRegistrationReport;
import com.ems.model.EventRevenueReport;
import com.ems.model.OrganizerEventSummary;
import com.ems.model.Ticket;

public interface OrganizerService {

    // event creation & management
    int createEvent(Event event);

    boolean updateEventDetails(int eventId, String title, String description, int categoryId, int venueId);

    boolean updateEventSchedule(int eventId, LocalDateTime start, LocalDateTime end);

    boolean updateEventCapacity(int eventId, int capacity);

    boolean publishEvent(int eventId);

    boolean cancelEvent(int eventId);

    
    // ticket management
    boolean createTicket(Ticket ticket);

    boolean updateTicketPrice(int ticketId, double price);

    boolean updateTicketQuantity(int ticketId, int quantity);

    List<Ticket> viewTicketAvailability(int eventId);

    
    // registrations & reports
    int viewEventRegistrations(int eventId);

    List<EventRevenueReport> getRevenueReport(int organizerId);
    
    List<OrganizerEventSummary> getOrganizerEventSummary(int organizerId);

    
    // notifications
    void sendEventUpdate(int eventId, String message);

    void sendScheduleChange(int eventId, String message);

    
    // organizer data
    List<Event> getOrganizerEvents(int organizerId);
    
    Event getOrganizerEventById(int organizerId, int eventId);

	void sendCancellationRequest(Event selectedEvent, String message);
	
	List<EventRegistrationReport> getEventWiseRegistrations(int eventId);

}
