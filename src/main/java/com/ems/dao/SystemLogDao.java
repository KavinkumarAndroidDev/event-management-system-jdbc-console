package com.ems.dao;

import java.util.List;

import com.ems.exception.DataAccessException;
import com.ems.model.SystemLog;

public interface SystemLogDao {

    /**
     * Persists a system audit log entry
     *
     * @param userId optional user who triggered the action
     * @param action performed action
     * @param entity affected entity name
     * @param entityId optional affected entity id
     * @param message descriptive log message
     * @throws DataAccessException
     */
    void log(
        Integer userId,
        String action,
        String entity,
        Integer entityId,
        String message
    ) throws DataAccessException;

    /**
     * Retrieves all system logs ordered by most recent
     *
     * @return list of system logs
     * @throws DataAccessException
     */
    List<SystemLog> findAll() throws DataAccessException;
}
