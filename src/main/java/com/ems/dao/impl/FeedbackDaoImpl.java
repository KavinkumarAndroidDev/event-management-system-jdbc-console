package com.ems.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ems.dao.FeedbackDao;
import com.ems.exception.DataAccessException;
import com.ems.util.DBConnectionUtil;

/*
 * Handles database operations related to event feedback.
 *
 * Responsibilities:
 * - Validate user eligibility for feedback submission
 * - Persist ratings and comments for completed events
 */
public class FeedbackDaoImpl implements FeedbackDao {
	
	@Override
	public boolean submitRating(int eventId, int userId, int rating, String comments)
	        throws DataAccessException {

	    // Ensures feedback is allowed only for confirmed registrations of completed events
	    String sql =
	        "select count(*) from events e " +
	        "join registrations r on e.event_id = r.event_id " +
	        "where r.user_id = ? " +
	        "and e.event_id = ? " +
	        "and e.status = 'COMPLETED' " +
	        "and r.status = 'CONFIRMED'";

		try (Connection con = DBConnectionUtil.getConnection();
			PreparedStatement ps = con.prepareStatement(sql)) {
			
		    ps.setInt(1, userId);
		    ps.setInt(2, eventId);
		    ResultSet rs = ps.executeQuery();
		    
		    if (rs.next() && rs.getInt(1) > 0) {
		    	// Persists feedback only after eligibility check passes
		    	String insertReview =
		    	    "insert into feedback(event_id, user_id, rating, comments, submitted_at) " +
		    	    "values(?, ?, ?, ?, utc_timestamp())";

		        try (PreparedStatement ps1 = con.prepareStatement(insertReview)) {
			        ps1.setInt(1, eventId);
			        ps1.setInt(2, userId);
			        ps1.setInt(3, rating);
			        ps1.setString(4, comments);
			        
			        int affectedRows = ps1.executeUpdate();
			        
			        if (affectedRows > 0) {
			            return true;  
			        } else {
			            return false;  
			        }
		        }
		    } else {
		        return false;  
		    }
		    
		} catch (SQLException e) {
			throw new DataAccessException("Error while submitting rating");
		}
	}
}