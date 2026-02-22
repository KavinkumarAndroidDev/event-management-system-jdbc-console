package com.ems.actions;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.ems.model.Category;
import com.ems.model.Event;
import com.ems.model.Venue;
import com.ems.service.EventService;
import com.ems.util.ApplicationUtil;
import com.ems.util.DateTimeUtil;
import com.ems.util.InputValidationUtil;
import com.ems.util.MenuHelper;
import com.ems.util.PaginationUtil;
import com.ems.util.ScannerUtil;

/**
 * Action class for event search and filter operations.
 * Delegates all business logic to EventService.
 */
public class EventSearchAction {
    
    private final EventService eventService;
    
    public EventSearchAction() {
        this.eventService = ApplicationUtil.eventService();
    }
    
    /**
     * Handles searching events by category.
     * Displays available categories, validates user input,
     * and prints matching events.
     */
    public void handleSearchByCategory() {
        List<Category> categories = getAllCategories();

        if (categories.isEmpty()) {
            System.out.println("No categories available.");
            return;
        }

        MenuHelper.displayCategories(categories);

        int choice = InputValidationUtil.readInt(
            ScannerUtil.getScanner(), "Select category: "
        );

        if (choice < 1 || choice > categories.size()) {
            System.out.println("Invalid category selection.");
            return;
        }

        Category selected = categories.get(choice - 1);
        List<Event> events = searchByCategory(selected.getCategoryId());

        if (events.isEmpty()) {
            System.out.println(
                "No events found in category: " + selected.getName()
            );
            return;
        }

        PaginationUtil.paginate(events, MenuHelper::printEventSummaries);
    }

    /**
     * Handles searching events by a specific date.
     * Prompts user for date input and displays matching events.
     */
	public void handleSearchByDate() {
	    String input = InputValidationUtil.readNonEmptyString(ScannerUtil.getScanner(), "Enter date (dd-mm-yyyy): ");
	
	    LocalDate date = DateTimeUtil.parseLocalDate(input);
	    if (date == null) {
	        System.out.println("Invalid date format.");
	        return;
	    }
	
	    List<Event> events = searchByDate(date);
	
	    if (events.isEmpty()) {
	        System.out.println("No events found on " + date);
	        return;
	    }
	
	    PaginationUtil.paginate(events, MenuHelper::printEventSummaries);
	}


    /**
     * Handles searching events within a date range.
     * Validates date order before performing search.
     */
    public void handleSearchByDateRange() {
    	String startInput = InputValidationUtil.readNonEmptyString(ScannerUtil.getScanner(), "Enter start date (dd-mm-yyyy): ");
    	
	    LocalDate start = DateTimeUtil.parseLocalDate(startInput);
        if (start == null) return;
        
    	String endInput = InputValidationUtil.readNonEmptyString(ScannerUtil.getScanner(), "Enter end date (dd-mm-yyyy): ");
    	
	    LocalDate end = DateTimeUtil.parseLocalDate(endInput);
        if (end == null) return;

        if (start.isAfter(end)) {
            System.out.println("Start date cannot be after end date.");
            return;
        }

        List<Event> events = searchByDateRange(start, end);

        if (events.isEmpty()) {
            System.out.println("No events found in selected range.");
            return;
        }

        PaginationUtil.paginate(events, MenuHelper::printEventSummaries);
    }

    /**
     * Handles searching events by city.
     * Displays available cities and validates user selection.
     */
    public void handleSearchByCity() {
        List<Venue> venues = eventService.getAllVenues();

        if (venues == null || venues.isEmpty()) {
            System.out.println("No venues available.");
            return;
        }

        PaginationUtil.paginate(venues, MenuHelper::displayVenues);

        int choice = MenuHelper.selectFromList(
            venues.size(),
            "Select venue"
        );

        Venue selected = venues.get(choice - 1);

        List<Event> events = eventService.searchByCity(selected.getVenueId());

        if (events.isEmpty()) {
            System.out.println("No events found at " + selected.getName());
            return;
        }

        PaginationUtil.paginate(events, MenuHelper::printEventSummaries);
    }

    

    /**
     * Handles filtering events by price range.
     * Validates price limits before searching.
     */
    public void handleFilterByPrice() {
        double min = InputValidationUtil.readDouble(
            ScannerUtil.getScanner(), "Enter minimum price: "
        );
        double max = InputValidationUtil.readDouble(
            ScannerUtil.getScanner(), "Enter maximum price: "
        );

        if (min < 0 || min > max) {
            System.out.println("Invalid price range.");
            return;
        }

        List<Event> events = filterByPrice(min, max);

        if (events.isEmpty()) {
            System.out.println("No events found in price range.");
            return;
        }

        PaginationUtil.paginate(events, MenuHelper::printEventSummaries);
    }

    /* ===================== DATA RETRIEVAL METHODS ===================== */

    /**
     * Retrieves all available categories.
     *
     * @return list of categories, or empty list if none exist
     */
    public List<Category> getAllCategories() {
        return eventService.getAllCategory();
    }

    /**
     * Searches events by category ID.
     *
     * @param categoryId the category ID to search by
     * @return list of events in the category
     */
    public List<Event> searchByCategory(int categoryId) {
        return eventService.searchBycategory(categoryId);
    }

    /**
     * Searches events by specific date.
     *
     * @param date the date to search by
     * @return list of events on that date
     */
    public List<Event> searchByDate(LocalDate date) {
        return eventService.searchByDate(date);
    }

    /**
     * Searches events within a date range.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @return list of events in the date range
     */
    public List<Event> searchByDateRange(LocalDate startDate, LocalDate endDate) {
        return eventService.searchByDateRange(startDate, endDate);
    }

    /**
     * Retrieves all cities where events are available.
     *
     * @return map of city ID to city name
     */
    public Map<Integer, String> getAllCities() {
        return eventService.getAllCities();
    }

    /**
     * Searches events by city ID.
     *
     * @param cityId the city ID to search by
     * @return list of events in that city
     */
    public List<Event> searchByCity(int cityId) {
        return eventService.searchByCity(cityId);
    }

    /**
     * Filters events by price range.
     *
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return list of events in the price range
     */
    public List<Event> filterByPrice(double minPrice, double maxPrice) {
        return eventService.filterByPrice(minPrice, maxPrice);
    }
}
