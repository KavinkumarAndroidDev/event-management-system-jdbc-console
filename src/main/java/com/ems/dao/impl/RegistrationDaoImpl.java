package com.ems.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ems.dao.RegistrationDao;
import com.ems.enums.PaymentStatus;
import com.ems.exception.DataAccessException;
import com.ems.model.EventRegistrationReport;
import com.ems.model.Registration;
import com.ems.model.RegistrationTicket;
import com.ems.util.DBConnectionUtil;
import com.ems.util.DateTimeUtil;

/*
 * Handles database operations related to event registrations.
 *
 * Responsibilities:
 * - Persist and update event registrations and ticket allocations
 * - Retrieve registration data for events and organizers
 * - Generate registration, sales, and revenue reports
 */
public class RegistrationDaoImpl implements RegistrationDao {

	
	@Override
	public List<EventRegistrationReport> getEventWiseRegistrations(int eventId)
	        throws DataAccessException {

	    // Returns only confirmed registrations for reporting
	    List<EventRegistrationReport> reports = new ArrayList<>();
	
	    String sql =
	        "select e.title as event_title, u.full_name, t.ticket_type, " +
	        "rt.quantity, r.registration_date " +
	        "from registrations r " +
	        "inner join users u on r.user_id = u.user_id " +
	        "inner join registration_tickets rt on r.registration_id = rt.registration_id " +
	        "inner join tickets t on rt.ticket_id = t.ticket_id " +
	        "inner join events e on r.event_id = e.event_id " +
	        "where r.event_id = ? " +
	        "  and r.status = 'CONFIRMED'";

	
	    try (Connection con = DBConnectionUtil.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	
	        ps.setInt(1, eventId);
	        ResultSet rs = ps.executeQuery();
	
	        while (rs.next()) {
	            EventRegistrationReport report = new EventRegistrationReport();
	            report.setEventTitle(rs.getString("event_title"));
	            report.setUserName(rs.getString("full_name"));
	            report.setTicketType(rs.getString("ticket_type"));
	            report.setQuantity(rs.getInt("quantity"));
	            report.setRegistrationDate(
	            	    DateTimeUtil.convertUtcToLocalDateTime(
	            	        rs.getTimestamp("registration_date").toInstant()
	            	    )
	            	);

	            

	
	            reports.add(report);
	        }
	        rs.close();
	
	    } catch (SQLException e) {
	        throw new DataAccessException(
	            "Error while fetching event wise registration"
	        );
	    }

	    return reports;
	}

	
	@Override
	public List<Integer> getRegisteredUserIdsByEvent(int eventId)
	        throws DataAccessException {

	    // Used for bulk notification or follow up actions

	    String sql = "select distinct user_id from registrations where event_id=?";
	    List<Integer> userIds = new ArrayList<>();

	    try (Connection con = DBConnectionUtil.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, eventId);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            userIds.add(rs.getInt("user_id"));
	        }

	        return userIds;

	    } catch (Exception e) {
	        throw new DataAccessException("Unable to fetch registered users", e);
	    }
	}

	
	
	//organizer functions:
	@Override
	public int getEventRegistrationCount(int eventId)
	        throws DataAccessException {

	    // Lightweight count query for capacity checks
       String sql = "select count(*) from registrations where event_id=? and status='CONFIRMED' ";
        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, eventId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception e) {
	        throw new DataAccessException("Unable to fetch registered count");
	    }
    }
	
	@Override
    public List<Map<String, Object>> getRegisteredUsers(int eventId) throws DataAccessException {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "select u.user_id,u.full_name,u.email from users u join registrations r on u.user_id=r.user_id where r.event_id=?";
        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, eventId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> m = new HashMap<>();
                m.put("userId", rs.getInt("user_id"));
                m.put("name", rs.getString("full_name"));
                m.put("email", rs.getString("email"));
                list.add(m);
            }
        } catch (Exception e) {
	        throw new DataAccessException("Unable to fetch registered users");
	    }
        return list;
    }
	
	@Override
	public List<Map<String, Object>> getOrganizerWiseRegistrations(int organizerId)
	        throws DataAccessException {
	    // Aggregates registrations per event for organizer dashboard

        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "select e.title,count(r.registration_id) total from events e left join registrations r \r\n"
        		+ "  on e.event_id = r.event_id "
        		+ " and r.status = 'CONFIRMED' "
        		+ " where e.organizer_id=? group by e.event_id";
        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, organizerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> m = new HashMap<>();
                m.put("event", rs.getString("title"));
                m.put("count", rs.getInt("total"));
                list.add(m);
            }
        } catch (Exception e) {
	        throw new DataAccessException("Unable to fetch organizer wise registrations");
	    }
        return list;
    }
	
	@Override
    public List<Map<String, Object>> getTicketSales(int organizerId) throws DataAccessException {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "select t.ticket_type,sum(rt.quantity) sold from tickets t join registration_tickets rt on t.ticket_id=rt.ticket_id join events e on e.event_id=t.event_id where e.organizer_id=? group by t.ticket_id";
        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, organizerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> m = new HashMap<>();
                m.put("ticketType", rs.getString("ticket_type"));
                m.put("sold", rs.getInt("sold"));
                list.add(m);
            }
        } catch (Exception e) {
	        throw new DataAccessException("Unable to fetch ticket sales");
	    }
        return list;
    }
	@Override
    public double getRevenueSummary(int organizerId) throws DataAccessException {
        String sql = "select sum(p.amount) from payments p join registrations "
        		+ "r on p.registration_id=r.registration_id join events e "
        		+ "on e.event_id=r.event_id where e.organizer_id=? and p.payment_status = ?";
        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, organizerId);
            ps.setString(2, PaymentStatus.SUCCESS.toString());
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getDouble(1) : 0;
        } catch (Exception e) {
	        throw new DataAccessException("Unable to fetch revenue summary");
	    }
    }

	@Override
	public Registration getById(int registrationId)
	        throws DataAccessException {

	    // Fetches core registration details without ticket breakdown

        String sql = "SELECT registration_id, user_id, event_id, registration_date, status "
                   + "FROM registrations "
                   + "WHERE registration_id = ?";

        try (Connection con = DBConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, registrationId);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                	throw new DataAccessException("Data not found");
                }

                return new Registration(
                    rs.getInt("registration_id"),
                    rs.getInt("user_id"),
                    rs.getInt("event_id"),
                    DateTimeUtil.convertUtcToLocalDateTime(
                    	    rs.getTimestamp("registration_date").toInstant()
                    	),
                    rs.getString("status")
                );
            }

        } catch (SQLException e) {
            throw new DataAccessException("Unable to fetch registration with id: " + registrationId);
        }
    }


	@Override
	public void updateStatus(int registrationId, String string) throws DataAccessException {
		String sql = "update registrations "
				+ "set status = ? "
				+ "where registration_id = ?";
		try (Connection con = DBConnectionUtil.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {

	            ps.setString(1, string);
	            ps.setInt(2, registrationId);
	            ps.executeUpdate();
	        } catch (Exception e) {
		        throw new DataAccessException("Unable to update registration status");
		    }
		
	}

	@Override
	public List<RegistrationTicket> getRegistrationTickets(int registrationId)
	        throws DataAccessException {

	    // Used during cancellation or refund processing

	    String sql = "select ticket_id, quantity " +
	                 "from registration_tickets " +
	                 "where registration_id = ?";

	    List<RegistrationTicket> tickets = new ArrayList<>();

	    try (Connection con = DBConnectionUtil.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, registrationId);

	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                RegistrationTicket rt = new RegistrationTicket();
	                rt.setTicketId(rs.getInt("ticket_id"));
	                rt.setQuantity(rs.getInt("quantity"));
	                tickets.add(rt);
	            }
	        }

	    } catch (SQLException e) {
	        throw new DataAccessException("Error fetching registration tickets");
	    }

	    return tickets;
	}



   
}