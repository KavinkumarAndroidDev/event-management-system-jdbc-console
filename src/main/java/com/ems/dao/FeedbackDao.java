package com.ems.dao;

import com.ems.exception.DataAccessException;

public interface FeedbackDao {

    /**
     * Submits rating and feedback for a completed event
     *
     * @param eventId
     * @param userId
     * @param rating
     * @param comments
     * @return true if feedback was saved successfully
     * @throws DataAccessException
     */
    boolean submitRating(int eventId, int userId, int rating, String comments)
            throws DataAccessException;

	boolean isRatingAlreadySubmitted(int eventId, int userId) throws DataAccessException;
}