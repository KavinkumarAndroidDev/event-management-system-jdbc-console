package com.ems.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ems.dao.EventDao;
import com.ems.dao.RegistrationDao;
import com.ems.dao.TicketDao;
import com.ems.enums.EventStatus;
import com.ems.enums.NotificationType;
import com.ems.exception.DataAccessException;
import com.ems.model.Event;
import com.ems.model.EventRegistrationReport;
import com.ems.model.EventRevenueReport;
import com.ems.model.OrganizerEventSummary;
import com.ems.model.Ticket;
import com.ems.service.NotificationService;
import com.ems.service.OrganizerService;
import com.ems.service.SystemLogService;
import com.ems.util.DateTimeUtil;

/*
 * Handles organizer related business operations.
 *
 * Responsibilities:
 * - Create, update, publish, and cancel events
 * - Manage event schedules, capacity, and tickets
 * - Access registration, sales, and revenue data
 * - Send event related notifications to attendees
 */
public class OrganizerServiceImpl implements OrganizerService {

	private final EventDao eventDao;
	private final TicketDao ticketDao;
	private final RegistrationDao registrationDao;
	private final NotificationService notificationService;
	private final SystemLogService systemLogService;

	public OrganizerServiceImpl(EventDao eventDao, TicketDao ticketDao, RegistrationDao registrationDao,
			NotificationService notificationService, SystemLogService systemLogService) {
		this.eventDao = eventDao;
		this.ticketDao = ticketDao;
		this.registrationDao = registrationDao;
		this.notificationService = notificationService;
		this.systemLogService = systemLogService;
	}

	/*
	 * Creates a new event in DRAFT state.
	 *
	 * Rule: - Newly created events are always saved as DRAFT
	 */
	public int createEvent(Event event) {
		event.setStatus(EventStatus.DRAFT.name());
		try {

			int eventId = eventDao.createEvent(event);

			systemLogService.log(
					event.getOrganizerId(),
					"CREATE",
					"EVENT",
					eventId,
					"Event created in DRAFT state");

			return eventId;

		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
		}
		return 0;
	}

	/*
	 * Updates basic event information.
	 *
	 * Used when organizer edits title, description, category, or venue.
	 */
	public boolean updateEventDetails(int eventId, String title, String description, int categoryId, int venueId) {
		try {
			boolean updated = eventDao.updateEventDetails(eventId, title, description, categoryId, venueId);

			if (updated) {
				systemLogService.log(
						null,
						"UPDATE",
						"EVENT",
						eventId,
						"Event details updated");
			}

			return updated;

		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	/*
	 * Updates the event start and end schedule.
	 *
	 * Used for rescheduling upcoming events.
	 */
	public boolean updateEventSchedule(int eventId, LocalDateTime start, LocalDateTime end) {
		try {
			boolean updated = eventDao.updateEventSchedule(
					eventId,
					DateTimeUtil.toUtcInstant(start),
					DateTimeUtil.toUtcInstant(end));

			if (updated) {
				systemLogService.log(
						null,
						"UPDATE",
						"EVENT",
						eventId,
						"Event schedule updated to " + start + " - " + end);
			}

			return updated;

		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	/*
	 * Updates the maximum allowed capacity for an event.
	 */
	public boolean updateEventCapacity(int eventId, int capacity) {
		try {
			boolean updated = eventDao.updateEventCapacity(eventId, capacity);

			if (updated) {
				systemLogService.log(
						null,
						"UPDATE",
						"EVENT",
						eventId,
						"Event capacity updated to " + capacity);
			}

			return updated;

		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	/*
	 * Publishes an event and makes it visible to users.
	 */
	public boolean publishEvent(int eventId) {
		try {
			boolean published = eventDao.updateEventStatus(eventId, EventStatus.PUBLISHED.name());

			if (published) {
				systemLogService.log(
						null,
						"PUBLISH",
						"EVENT",
						eventId,
						"Event published");
			}

			return published;

		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	/*
	 * Cancels an existing event.
	 *
	 * Used when an event cannot proceed as planned.
	 */
	public boolean cancelEvent(int eventId) {
		try {
			boolean cancelled = eventDao.updateEventStatus(eventId, EventStatus.CANCELLED.name());

			if (cancelled) {
				systemLogService.log(
						null,
						"CANCEL",
						"EVENT",
						eventId,
						"Event cancelled by organizer");
			}

			return cancelled;

		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	/*
	 * Creates a ticket type for an event.
	 *
	 * Rule: - Available quantity is initialized to total quantity
	 */
	public boolean createTicket(Ticket ticket) {
		try {
			ticket.setAvailableQuantity(ticket.getTotalQuantity());
			boolean created = ticketDao.createTicket(ticket);

			if (created) {
				systemLogService.log(
						null,
						"CREATE",
						"TICKET",
						ticket.getEventId(),
						"Ticket type created: " + ticket.getTicketType());
			}

			return created;
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	/*
	 * Updates the price of an existing ticket type.
	 */
	public boolean updateTicketPrice(int ticketId, double price) {
		try {
			boolean updated = ticketDao.updateTicketPrice(ticketId, price);

			if (updated) {
				systemLogService.log(
						null,
						"UPDATE",
						"TICKET",
						ticketId,
						"Ticket price updated to ₹" + price);
			}

			return updated;
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	/*
	 * Updates the total quantity of tickets available.
	 */
	public boolean updateTicketQuantity(int ticketId, int quantity) {
		try {
			boolean updated = ticketDao.updateTicketQuantity(ticketId, quantity);

			if (updated) {
				systemLogService.log(
						null,
						"UPDATE",
						"TICKET",
						ticketId,
						"Ticket quantity updated to " + quantity);
			}

			return updated;
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
		}
		return false;

	}

	/*
	 * Retrieves current ticket availability for an event.
	 */
	public List<Ticket> viewTicketAvailability(int eventId) {
		try {
			return ticketDao.getTicketsByEvent(eventId);
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
		}
		return new ArrayList<>();
	}

	/*
	 * Returns the total number of registrations for an event.
	 */
	public int viewEventRegistrations(int eventId) {
		try {
			return registrationDao.getEventRegistrationCount(eventId);
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
			return 0;
		}
	}

	/*
	 * Displays registration details for a specific event. Registrations are shown
	 * in reverse chronological order.
	 */
	@Override
	public List<EventRegistrationReport> getEventWiseRegistrations(int eventId) {
		try {
			List<EventRegistrationReport> reports = registrationDao.getEventWiseRegistrations(eventId);

			if (reports.isEmpty()) {
				return new ArrayList<>();
			}
			return reports;
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
		}
		return new ArrayList<>();
	}

	/*
	 * Retrieves all events created by a specific organizer.
	 */
	public List<Event> getOrganizerEvents(int organizerId) {
		try {
			return eventDao.getEventsByOrganizer(organizerId);
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
		}
		return new ArrayList<>();
	}

	/*
	 * Sends a general update notification to all event attendees.
	 */
	public void sendEventUpdate(int eventId, String message) {
		notificationService.sendEventNotification(eventId, message, NotificationType.EVENT);
	}

	/*
	 * Sends a schedule change notification to all event attendees.
	 */
	public void sendScheduleChange(int eventId, String message) {
		notificationService.sendEventNotification(eventId, message, NotificationType.EVENT);
	}

	public List<OrganizerEventSummary> getOrganizerEventSummary(int organizerId) {
		try {
			return eventDao.getEventSummaryByOrganizer(organizerId);
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
		}
		return new ArrayList<>();
	}

	@Override
	public Event getOrganizerEventById(int organizerId, int eventId) {
		try {
			Event event = eventDao.getEventById(eventId);

			if (event == null) {
				return null;
			}

			if (event.getOrganizerId() != organizerId) {
				return null;
			}

			return event;

		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public List<EventRevenueReport> getRevenueReport(int organizerId) {
		try {
			return eventDao.getEventWiseRevenueReportByOrganizer(organizerId);
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
		}
		return new ArrayList<>();
	}

	@Override
	public void sendCancellationRequest(Event event, String organizerMessage) {

		String notificationMessage = "CANCELLATION REQUEST\n\n" +
				"Event: " + event.getTitle() + "\n" +
				"Event ID: " + event.getEventId() + "\n" +
				"Start Time: " + DateTimeUtil.formatForDisplay(event.getStartDateTime()) + "\n\n" +
				"Requested by Organizer:\n" +
				event.getOrganizerId() + " (User ID: " + event.getOrganizerId() + ")\n\n" +
				"Organizer Message:\n" +
				organizerMessage;

		notificationService.sendPersonalNotification(
				event.getApprovedBy(),
				notificationMessage,
				NotificationType.EVENT);
	}
}
