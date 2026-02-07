package com.ems.service;

import java.util.List;

import com.ems.model.SystemLog;

public interface SystemLogService {

	void log(
		Integer userId,
		String action,
		String entity,
		Integer entityId,
		String message
	);

	List<SystemLog> getAllLogs();
}
