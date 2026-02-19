package com.ems.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ems.dao.SystemLogDao;
import com.ems.exception.DataAccessException;
import com.ems.model.SystemLog;
import com.ems.util.DBConnectionUtil;
import com.ems.util.DateTimeUtil;
/*
 * Handles database operations related to system audit logs.
 *
 * Responsibilities:
 * - Persist system wide audit logs
 * - Retrieve logs for monitoring and diagnostics
 */
public class SystemLogDaoImpl implements SystemLogDao {

	@Override
	public void log(
	        Integer userId,
	        String action,
	        String entity,
	        Integer entityId,
	        String message)
	        throws DataAccessException {

	    // Allows nullable user and entity references for system generated logs
	    String sql =
	        "insert into system_logs " +
	        "(user_id, action, entity, entity_id, message, created_at) " +
	        "values (?, ?, ?, ?, ?, utc_timestamp())";


		try (Connection con = DBConnectionUtil.getConnection();
		     PreparedStatement ps = con.prepareStatement(sql)) {

			if (userId != null) {
				ps.setInt(1, userId);
			} else {
				ps.setNull(1, java.sql.Types.INTEGER);
			}

			ps.setString(2, action);
			ps.setString(3, entity);

			if (entityId != null) {
				ps.setInt(4, entityId);
			} else {
				ps.setNull(4, java.sql.Types.INTEGER);
			}

			ps.setString(5, message);

			ps.executeUpdate();

		} catch (SQLException e) {
			throw new DataAccessException(
				"Error while inserting system log"
			);
		}
	}

	@Override
	public List<SystemLog> findAll() throws DataAccessException {

	    // Returns logs ordered by most recent for admin views
	    String sql = "SELECT log_id, user_id, action, entity, entity_id, message, created_at "
	               + "FROM system_logs "
	               + "ORDER BY created_at DESC";

	    List<SystemLog> logs = new ArrayList<>();
	
	    try (Connection con = DBConnectionUtil.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            SystemLog log = new SystemLog();
	            log.setLogId(rs.getInt("log_id"));
	            log.setAction(rs.getString("action"));
	            log.setEntity(rs.getString("entity"));
	
	            log.setUserId(rs.getObject("user_id", Integer.class));
	            log.setEntityId(rs.getObject("entity_id", Integer.class));
	
	            log.setMessage(rs.getString("message"));
	
	            log.setCreatedAt(DateTimeUtil.fromTimestamp(rs.getTimestamp("created_at")));
	            logs.add(log);
	        }
	    } catch (SQLException e) {
	        throw new DataAccessException("Error while fetching system log", e);
	    }
	
	    return logs;
	}

}
