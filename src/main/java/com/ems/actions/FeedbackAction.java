package com.ems.actions;

import java.util.List;

import com.ems.model.UserEventRegistration;
import com.ems.service.EventService;
import com.ems.util.ApplicationUtil;
import com.ems.util.InputValidationUtil;
import com.ems.util.MenuHelper;
import com.ems.util.ScannerUtil;

/**
 * Action class for feedback operations.
 * Delegates business logic to EventService.
 */
public class FeedbackAction {
    
    private final EventService eventService;

    public FeedbackAction() {
        this.eventService = ApplicationUtil.eventService();
    }

    /**
     * Allows a user to submit a rating and optional feedback
     * for an event they have previously attended.
     *
     * @param userId the ID of the user submitting feedback
     */
    public void submitRating(int userId) {
        List<UserEventRegistration> past = eventService.viewPastEvents(userId);

        if (past == null || past.isEmpty()) {
            System.out.println("No past events to rate.");
            return;
        }

        MenuHelper.printEventsList(past);

        int choice = MenuHelper.selectFromList(
            past.size(),
            "Select an event to rate"
        );

        int eventId = past.get(choice - 1).getEventId();

        int rating;
        do {
            rating = InputValidationUtil.readInt(
                ScannerUtil.getScanner(),
                "Rate the event (1-5): "
            );

            if (rating < 1 || rating > 5) {
                System.out.println("Please enter a rating between 1 and 5.");
            }
        } while (rating < 1 || rating > 5);

        String comments = InputValidationUtil.readString(
            ScannerUtil.getScanner(),
            "Enter feedback (optional, press Enter to skip):\n"
        );

        comments = (comments == null || comments.trim().isEmpty())
            ? null
            : comments.trim();

        boolean isSuccess = eventService.submitRating(userId, eventId, rating, comments);
        if(isSuccess) {
        	System.out.println("Thank you for your feedback!");
        }else {
        	System.out.println("Failed to submit your rating!");
        }
    }
}