package com.ems.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ems.dao.SystemLogDao;
import com.ems.exception.DataAccessException;
import com.ems.model.SystemLog;
import com.ems.service.SystemLogService;

public class SystemLogServiceImpl implements SystemLogService {

	private final SystemLogDao systemLogDao;

	public SystemLogServiceImpl(SystemLogDao systemLogDao) {
		this.systemLogDao = systemLogDao;
	}

	@Override
	public void log(
			Integer userId,
			String action,
			String entity,
			Integer entityId,
			String message) {

		try {
			systemLogDao.log(
				userId,
				action,
				entity,
				entityId,
				message
			);
		}catch(DataAccessException e) {
			System.out.println(e);
		}
	}
	
	@Override
	public List<SystemLog> getAllLogs() {
	    try {
	        return systemLogDao.findAll();
	    } catch (DataAccessException e) {
	    	System.out.println("Failed to load system logs.");
	    }
		return new ArrayList<>();
	}


}
