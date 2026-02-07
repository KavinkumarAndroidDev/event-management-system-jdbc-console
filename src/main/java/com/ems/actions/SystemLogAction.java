package com.ems.actions;

import java.util.List;

import com.ems.model.SystemLog;
import com.ems.service.SystemLogService;
import com.ems.util.AdminMenuHelper;
import com.ems.util.ApplicationUtil;

public class SystemLogAction {
    private final SystemLogService systemLogService;

    public SystemLogAction() {
        this.systemLogService = ApplicationUtil.systemLogService();
    }

    public void printAllLogs() {
    	List<SystemLog> logs = systemLogService.getAllLogs();
    	AdminMenuHelper.printSystemLogs(logs);

    }
}