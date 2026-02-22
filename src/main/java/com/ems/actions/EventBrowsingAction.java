package com.ems.actions;

import java.util.List;

import com.ems.model.Event;
import com.ems.model.Ticket;
import com.ems.service.EventService;
import com.ems.util.ApplicationUtil;
import com.ems.util.MenuHelper;
import com.ems.util.PaginationUtil;

/*
 * Handles read-only event browsing operations.
 * Used for listing events, viewing details, and ticket options.
 */
public class EventBrowsingAction {

    
    private final EventService eventService;
    
    public EventBrowsingAction() {
        this.eventService = ApplicationUtil.eventService();
    }

    /**
     * Retrieves all events that are currently available.
     *
     * @return list of available events
     */
    public List<Event> getAllAvailableEvents() {
        return eventService.listAvailableEvents();
    }

    /**
     * Retrieves ticket types for a given event.
     *
     * @param eventId the ID of the event
     * @return list of ticket types for the event
     */
    public List<Ticket> getTicketsForEvent(int eventId) {
        return eventService.getTicketTypes(eventId);
    }

    /**
     * Displays a summarized list of all available events.
     */
    public void printAllAvailableEvents() {
        List<Event> filteredEvents = getAllAvailableEvents();

        if (filteredEvents == null || filteredEvents.isEmpty()) {
            System.out.println("There are no available events!");
            return;
        }

        PaginationUtil.paginate(filteredEvents, MenuHelper::printEventSummaries);
    }

    /**
     * Displays detailed information for a selected event.
     * Allows the user to choose an event from the available list.
     */
    public void viewEventDetails() {
        List<Event> events = getAllAvailableEvents();

        if (events == null || events.isEmpty()) {
            System.out.println("No events available at the moment.");
            return;
        }

        PaginationUtil.paginate(events, MenuHelper::printEventSummaries);

        int choice = MenuHelper.selectFromList(
            events.size(),
            "Select an event number"
        );

        Event selectedEvent = events.get(choice - 1);
        MenuHelper.printEventDetails(selectedEvent);
    }

    /**
     * Displays ticket options for a selected event.
     * Allows the user to view available ticket types.
     */
    public void viewTicketOptions() {
        List<Event> events = getAllAvailableEvents();

        if (events == null || events.isEmpty()) {
            System.out.println("No events available at the moment.");
            return;
        }

        PaginationUtil.paginate(events, MenuHelper::printEventSummaries);

        int choice = MenuHelper.selectFromList(
            events.size(),
            "Select an event number"
        );

        Event selectedEvent = events.get(choice - 1);
        int eventId = selectedEvent.getEventId();

        List<Ticket> tickets = getTicketsForEvent(eventId);

        if (tickets == null || tickets.isEmpty()) {
            System.out.println("No ticket types available for this event.");
            return;
        }

        MenuHelper.printTicketSummaries(tickets);
    }
}
