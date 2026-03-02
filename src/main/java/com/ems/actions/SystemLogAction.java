package com.ems.actions;

import java.util.List;
import com.ems.model.SystemLog;
import com.ems.service.SystemLogService;
import com.ems.util.AdminMenuHelper;
import com.ems.util.ApplicationUtil;
import com.ems.util.PaginationUtil;

public class SystemLogAction {
    private final SystemLogService systemLogService;

    public SystemLogAction() {
        this.systemLogService = ApplicationUtil.systemLogService();
    }

    public void printAllLogs() {
        List<SystemLog> logs = systemLogService.getAllLogs();
        if (logs.isEmpty()) {
            System.out.println("No system logs found.");
        } else {
            PaginationUtil.paginate(logs, AdminMenuHelper::printSystemLogs);
        }
    }
}
