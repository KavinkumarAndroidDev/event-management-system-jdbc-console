package com.ems.service.impl;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ems.dao.*;
import com.ems.enums.NotificationType;
import com.ems.enums.PaymentMethod;
import com.ems.exception.DataAccessException;
import com.ems.model.BookingDetail;
import com.ems.model.Event;
import com.ems.model.Registration;
import com.ems.model.RegistrationTicket;
import com.ems.model.Ticket;
import com.ems.model.Category;
import com.ems.model.UserEventRegistration;
import com.ems.model.Venue;
import com.ems.service.EventService;
import com.ems.service.PaymentService;
import com.ems.service.SystemLogService;
import com.ems.util.DateTimeUtil;

public class EventServiceImpl implements EventService {

    private final EventDao eventDao;
    private final CategoryDao categoryDao;
    private final PaymentDao paymentDao;
    private final VenueDao venueDao;
    private final TicketDao ticketDao;
    private final PaymentService paymentService;
    private final FeedbackDao feedbackDao;
    private final RegistrationDao registrationDao;
    private final SystemLogService systemLogService;
    private final NotificationDao notificationDao;

    public EventServiceImpl(EventDao eventDao, CategoryDao categoryDao, VenueDao venueDao, TicketDao ticketDao,
            RegistrationDao registrationDao, PaymentDao paymentDao, PaymentService paymentService,
            FeedbackDao feedbackDao, SystemLogService systemLogService, NotificationDao notificationDao) {
        this.eventDao = eventDao;
        this.categoryDao = categoryDao;
        this.venueDao = venueDao;
        this.ticketDao = ticketDao;
        this.paymentService = paymentService;
        this.feedbackDao = feedbackDao;
        this.systemLogService = systemLogService;
        this.registrationDao = registrationDao;
        this.paymentDao = paymentDao;
        this.notificationDao = notificationDao;
    }

    @Override
    public List<Ticket> getTicketTypes(int eventId) {
        try {
            List<Ticket> tickets = ticketDao.getTicketTypes(eventId);
            return tickets != null ? tickets : new ArrayList<>();
        } catch (DataAccessException e) {
            systemLogService.log(0, "ERROR", "TICKET", eventId, "Failed to get ticket types: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Event> filterByPrice(double minPrice, double maxPrice) {
        if (minPrice < 0 || maxPrice < 0) {
            return new ArrayList<>();
        }
        
        if (minPrice > maxPrice) {
            return new ArrayList<>();
        }

        try {
            List<Event> allEvents = eventDao.listAvailableEvents();
            
            if (allEvents == null || allEvents.isEmpty()) {
                return new ArrayList<>();
            }

            return allEvents.stream().filter(event -> {
                try {
                    List<Ticket> tickets = ticketDao.getTicketTypes(event.getEventId());
                    if (tickets == null || tickets.isEmpty()) {
                        return false;
                    }
                    return tickets.stream()
                        .anyMatch(t -> t.getPrice() >= minPrice && t.getPrice() <= maxPrice);
                } catch (DataAccessException e) {
                    return false;
                }
            }).collect(Collectors.toList());

        } catch (DataAccessException e) {
            systemLogService.log(0, "ERROR", "EVENT", 0, "Price filter failed: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Event getEventById(int eventId) {
        try {
            return eventDao.getEventById(eventId);
        } catch (DataAccessException e) {
            systemLogService.log(0, "ERROR", "EVENT", eventId, "Failed to get event: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Event> searchByCity(int venueId) {
        try {
            List<Event> allEvents = eventDao.listAvailableEvents();
            
            if (allEvents == null) {
                return new ArrayList<>();
            }
            
            return allEvents.stream()
                .filter(e -> e.getVenueId() == venueId)
                .collect(Collectors.toList());
                
        } catch (DataAccessException e) {
            systemLogService.log(0, "ERROR", "EVENT", 0, "City search failed: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Event> searchByDate(LocalDate localDate) {
        if (localDate == null) {
            return new ArrayList<>();
        }

        try {
            List<Event> allEvents = eventDao.listAvailableEvents();
            
            if (allEvents == null) {
                return new ArrayList<>();
            }
            
            return allEvents.stream()
                .filter(e -> e.getStartDateTime() != null && 
                           e.getStartDateTime().toLocalDate().isEqual(localDate))
                .collect(Collectors.toList());

        } catch (DataAccessException e) {
            systemLogService.log(0, "ERROR", "EVENT", 0, "Date search failed: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Event> searchByDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return new ArrayList<>();
        }
        
        if (startDate.isAfter(endDate)) {
            return new ArrayList<>();
        }

        try {
            List<Event> allEvents = eventDao.listAvailableEvents();
            
            if (allEvents == null) {
                return new ArrayList<>();
            }

            return allEvents.stream().filter(e -> {
                if (e.getStartDateTime() == null) {
                    return false;
                }
                LocalDate eventDate = e.getStartDateTime().toLocalDate();
                return !eventDate.isBefore(startDate) && !eventDate.isAfter(endDate);
            }).collect(Collectors.toList());

        } catch (DataAccessException e) {
            systemLogService.log(0, "ERROR", "EVENT", 0, "Date range search failed: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Event> searchBycategory(int selectedCategoryId) {
        try {
            List<Event> allEvents = eventDao.listAvailableEvents();
            
            if (allEvents == null) {
                return new ArrayList<>();
            }
            
            return allEvents.stream()
                .filter(e -> e.getCategoryId() == selectedCategoryId)
                .collect(Collectors.toList());

        } catch (DataAccessException e) {
            systemLogService.log(0, "ERROR", "EVENT", 0, "Category search failed: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public boolean registerForEvent(
            int userId,
            int eventId,
            int ticketId,
            int quantity,
            double price,
            PaymentMethod paymentMethod,
            String offerCode) {
        
        String normalizedOfferCode = "";
        if (offerCode != null && !offerCode.trim().isEmpty()) {
            normalizedOfferCode = offerCode.trim().toUpperCase();
        }
        
        try {
            return paymentService.processRegistration(
                userId,
                eventId,
                ticketId,
                quantity,
                price,
                paymentMethod,
                normalizedOfferCode
            );
        } catch (Exception e) {
            systemLogService.log(
                userId,
                "REGISTRATION_FAILED",
                "EVENT",
                eventId,
                "Registration failed: " + e.getMessage()
            );
            return false;
        }
    }

    @Override
    public List<UserEventRegistration> viewUpcomingEvents(int userId) {
        try {
            List<UserEventRegistration> registrations = eventDao.getUserRegistrations(userId);
            
            if (registrations == null) {
                return new ArrayList<>();
            }

            return registrations.stream()
                .filter(r -> r.getStartDateTime() != null &&
                           r.getStartDateTime().isAfter(LocalDateTime.now()) &&
                           "CONFIRMED".equals(r.getRegistrationStatus()))
                .collect(Collectors.toList());

        } catch (DataAccessException e) {
            systemLogService.log(userId, "ERROR", "REGISTRATION", 0, 
                "Failed to get upcoming events: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<UserEventRegistration> viewPastEvents(int userId) {
        try {
            List<UserEventRegistration> registrations = eventDao.getUserRegistrations(userId);
            
            if (registrations == null) {
                return new ArrayList<>();
            }
            
            return registrations.stream()
                .filter(r -> r.getStartDateTime() != null &&
                           r.getStartDateTime().isBefore(LocalDateTime.now()) &&
                           "CONFIRMED".equals(r.getRegistrationStatus()))
                .collect(Collectors.toList());

        } catch (DataAccessException e) {
            systemLogService.log(userId, "ERROR", "REGISTRATION", 0,
                "Failed to get past events: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<BookingDetail> viewBookingDetails(int userId) {
        try {
            List<BookingDetail> bookings = eventDao.viewBookingDetails(userId);
            return bookings != null ? bookings : new ArrayList<>();
        } catch (DataAccessException e) {
            systemLogService.log(userId, "ERROR", "BOOKING", 0,
                "Failed to get booking details: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void submitRating(int userId, int eventId, int rating, String comments) {
        try {
            String normalizedComments = (comments == null || comments.trim().isEmpty()) 
                ? null 
                : comments.trim();

            feedbackDao.submitRating(eventId, userId, rating, normalizedComments);

            systemLogService.log(
                userId,
                "SUBMIT_FEEDBACK",
                "EVENT",
                eventId,
                "User submitted rating: " + rating
            );

        } catch (DataAccessException e) {
            systemLogService.log(userId, "ERROR", "FEEDBACK", eventId,
                "Failed to submit rating: " + e.getMessage());
        }
    }

    @Override
    public List<Event> getAllEvents() {
        try {
            List<Event> events = eventDao.listAllEvents();
            return events != null ? events : new ArrayList<>();
        } catch (DataAccessException e) {
            systemLogService.log(0, "ERROR", "EVENT", 0,
                "Failed to get all events: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Category getCategory(int categoryId) {
        try {
            return categoryDao.getCategory(categoryId);
        } catch (DataAccessException e) {
            systemLogService.log(0, "ERROR", "CATEGORY", categoryId,
                "Failed to get category: " + e.getMessage());
            return null;
        }
    }

    @Override
    public int getAvailableTickets(int eventId) {
        try {
            return ticketDao.getAvailableTickets(eventId);
        } catch (DataAccessException e) {
            systemLogService.log(0, "ERROR", "TICKET", eventId,
                "Failed to get available tickets: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public String getVenueName(int venueId) {
        try {
            String name = venueDao.getVenueName(venueId);
            return name != null ? name : "";
        } catch (DataAccessException e) {
            systemLogService.log(0, "ERROR", "VENUE", venueId,
                "Failed to get venue name: " + e.getMessage());
            return "";
        }
    }

    @Override
    public String getVenueAddress(int venueId) {
        try {
            String address = venueDao.getVenueAddress(venueId);
            return address != null ? address : "";
        } catch (DataAccessException e) {
            systemLogService.log(0, "ERROR", "VENUE", venueId,
                "Failed to get venue address: " + e.getMessage());
            return "";
        }
    }

    @Override
    public List<Category> getAllCategory() {
        try {
            List<Category> categories = categoryDao.getActiveCategories();
            return categories != null ? categories : new ArrayList<>();
        } catch (DataAccessException e) {
            systemLogService.log(0, "ERROR", "CATEGORY", 0,
                "Failed to get categories: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Map<Integer, String> getAllCities() {
        try {
            Map<Integer, String> cities = venueDao.getAllCities();
            
            if (cities == null) {
                return new LinkedHashMap<>();
            }
            
            if (!(cities instanceof LinkedHashMap)) {
                Map<Integer, String> sortedCities = new LinkedHashMap<>();
                cities.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue()) // Sort by city name
                    .forEachOrdered(e -> sortedCities.put(e.getKey(), e.getValue()));
                return sortedCities;
            }
            
            return cities;
            
        } catch (DataAccessException e) {
            systemLogService.log(0, "ERROR", "VENUE", 0,
                "Failed to get cities: " + e.getMessage());
            return new LinkedHashMap<>();
        }
    }

    @Override
    public List<Event> listAvailableEvents() {
        try {
            List<Event> events = eventDao.listAvailableEvents();
            return events != null ? events : new ArrayList<>();
        } catch (DataAccessException e) {
            systemLogService.log(0, "ERROR", "EVENT", 0,
                "Failed to list available events: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Venue> getActiveVenues() {
        try {
            List<Venue> venues = venueDao.getActiveVenues();
            return venues != null ? venues : new ArrayList<>();
        } catch (DataAccessException e) {
            systemLogService.log(0, "ERROR", "VENUE", 0,
                "Failed to get all venues: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<Venue> getAllVenues() {
        try {
            List<Venue> venues = venueDao.getAllVenues();
            return venues != null ? venues : new ArrayList<>();
        } catch (DataAccessException e) {
            systemLogService.log(0, "ERROR", "VENUE", 0,
                "Failed to get all venues: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    @Override
    public boolean isVenueAvailable(int venueId, LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            return false;
        }
        
        try {
            return venueDao.isVenueAvailable(
                venueId,
                Timestamp.from(DateTimeUtil.convertLocalDefaultToUtc(startTime)),
                Timestamp.from(DateTimeUtil.convertLocalDefaultToUtc(endTime))
            );
        } catch (DataAccessException e) {
            systemLogService.log(0, "ERROR", "VENUE", venueId,
                "Failed to check venue availability: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Venue getVenueById(int venueId) {
        try {
            return venueDao.getVenueById(venueId);
        } catch (DataAccessException e) {
            systemLogService.log(0, "ERROR", "VENUE", venueId,
                "Failed to get venue: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Event> listEventsYetToApprove() {
        try {
            List<Event> events = eventDao.listEventsYetToApprove();
            return events != null ? events : new ArrayList<>();
        } catch (DataAccessException e) {
            systemLogService.log(0, "ERROR", "EVENT", 0,
                "Failed to list pending events: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Event> listAvailableAndDraftEvents() {
        try {
            List<Event> events = eventDao.listAvailableAndDraftEvents();
            return events != null ? events : new ArrayList<>();
        } catch (DataAccessException e) {
            systemLogService.log(0, "ERROR", "EVENT", 0,
                "Failed to list available and draft events: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public boolean cancelRegistration(int userId, int registrationId) {
        try {
            Registration reg = registrationDao.getById(registrationId);
            
            if (reg == null) {
                systemLogService.log(userId, "CANCEL_FAILED", "REGISTRATION", registrationId,
                    "Registration not found");
                return false;
            }
            
            if (reg.getUserId() != userId) {
                systemLogService.log(userId, "CANCEL_FAILED", "REGISTRATION", registrationId,
                    "User does not own this registration");
                return false;
            }
            
            if (!"CONFIRMED".equals(reg.getStatus())) {
                systemLogService.log(userId, "CANCEL_FAILED", "REGISTRATION", registrationId,
                    "Registration status is: " + reg.getStatus());
                return false;
            }

            registrationDao.updateStatus(registrationId, "CANCELLED");

            List<RegistrationTicket> tickets = registrationDao.getRegistrationTickets(registrationId);
            
            if (tickets != null) {
                for (RegistrationTicket rt : tickets) {
                    ticketDao.updateAvailableQuantity(rt.getTicketId(), rt.getQuantity());
                }
            }

            paymentDao.updatePaymentStatus(registrationId);

            systemLogService.log(
                userId,
                "REGISTRATION_CANCELLED",
                "EVENT",
                reg.getEventId(),
                "Registration cancelled and refund initiated"
            );

            notificationDao.sendNotification(
                userId,
                "Your registration has been cancelled. Refund will be processed within 5-7 business days.",
                NotificationType.EVENT.name()
            );

            return true;
            
        } catch (DataAccessException e) {
            systemLogService.log(userId, "ERROR", "REGISTRATION", registrationId,
                "Cancellation failed: " + e.getMessage());
            return false;
        }
    }
}