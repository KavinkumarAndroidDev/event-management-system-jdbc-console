package com.ems.dao;

import java.util.List;

import com.ems.exception.DataAccessException;
import com.ems.model.EventRegistrationReport;
import com.ems.model.Registration;
import com.ems.model.RegistrationTicket;

public interface RegistrationDao {

        /**
         * Retrieves confirmed registrations for a specific event
         *
         * @param eventId
         * @return list of event registration reports
         * @throws DataAccessException
         */
        List<EventRegistrationReport> getEventWiseRegistrations(int eventId)
                        throws DataAccessException;

        /**
         * Returns total number of registrations for an event
         *
         * @param eventId
         * @return registration count
         * @throws DataAccessException
         */
        int getEventRegistrationCount(int eventId)
                        throws DataAccessException;

        /**
         * Returns all user ids registered for an event
         *
         * @param eventId
         * @return list of user ids
         * @throws DataAccessException
         */
        List<Integer> getRegisteredUserIdsByEvent(int eventId)
                        throws DataAccessException;

        /**
         * Retrieves registration details by id
         *
         * @param registrationId
         * @return registration details or null if not found
         * @throws DataAccessException
         */
        Registration getById(int registrationId)
                        throws DataAccessException;

        /**
         * Updates registration status
         *
         * @param registrationId
         * @param status
         * @throws DataAccessException
         */
        void updateStatus(int registrationId, String status)
                        throws DataAccessException;

        /**
         * Retrieves ticket details associated with a registration
         *
         * @param registrationId
         * @return list of registration tickets
         * @throws DataAccessException
         */
        List<RegistrationTicket> getRegistrationTickets(int registrationId)
                        throws DataAccessException;
}
