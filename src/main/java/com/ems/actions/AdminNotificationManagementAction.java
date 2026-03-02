package com.ems.actions;

import java.util.List;

import com.ems.enums.NotificationType;
import com.ems.enums.UserRole;
import com.ems.model.User;
import com.ems.service.AdminService;
import com.ems.util.ApplicationUtil;
import com.ems.util.InputValidationUtil;
import com.ems.util.MenuHelper;
import com.ems.util.PaginationUtil;
import com.ems.util.ScannerUtil;

public class AdminNotificationManagementAction {
    private final AdminService adminService;

    public AdminNotificationManagementAction() {
        this.adminService = ApplicationUtil.adminService();
    }

    public void sendSystemWideNotification(NotificationType type) {
		String message = InputValidationUtil.readNonEmptyString(ScannerUtil.getScanner(), "Enter message to send: ");
        adminService.sendSystemWideNotification(message, type);
    }

    private void sendNotificationByRole(String message, NotificationType type, UserRole role) {
        adminService.sendNotificationByRole(message, type, role);
    }

    private void sendNotificationToUser(String message, NotificationType type, int userId) {
        adminService.sendNotificationToUser(message, type, userId);
    }
    
    public void sendNotificationByRole() {

		System.out.println("\nSelect user role\n" + "1. Attendee\n" + "2. Organizer\n>");

		int roleChoice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");

		UserRole role;

		if (roleChoice == 1) {
			role = UserRole.ATTENDEE;
		} else if (roleChoice == 2) {
			role = UserRole.ORGANIZER;
		} else {
			System.out.println("Invalid role selected. Please try again.");
			return;
		}

		System.out.println("\nSelect notification type\n" + "1. SYSTEM\n" + "2. EVENT\n>");

		int typeChoice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");

		NotificationType type;

		if (typeChoice == 1) {
			type = NotificationType.SYSTEM;
		} else if (typeChoice == 2) {
			type = NotificationType.EVENT;
		} else {
			System.out.println("Invalid notification type selected.");
			return;
		}

		String message = InputValidationUtil.readNonEmptyString(ScannerUtil.getScanner(), "Enter message: ");

		sendNotificationByRole(message, type, role);

		System.out.println("Notification sent successfully.");
	}
    
	public void sendNotificationToSpecificUser() {

		List<User> users = adminService.getAllUsers();

		if (users.isEmpty()) {
			System.out.println("No users available");
			return;
		}

		PaginationUtil.paginate(users, MenuHelper::displayUsers);

		int choice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "Select a user (1-" + users.size() + "): ");

		while (choice < 1 || choice > users.size()) {
			choice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "Enter a valid choice: ");
		}

		User selectedUser = users.get(choice - 1);

		System.out.println("\nSelect notification type\n" + "1. SYSTEM\n" + "2. EVENT\n" + "3. PAYMENT\n>");

		int typeChoice = InputValidationUtil.readInt(ScannerUtil.getScanner(), "");

		NotificationType type;

		if (typeChoice == 1) {
			type = NotificationType.SYSTEM;
		} else if (typeChoice == 2) {
			type = NotificationType.EVENT;
		} else if (typeChoice == 3) {
			type = NotificationType.PAYMENT;
		} else {
			System.out.println("Invalid notification type selected.");
			return;
		}

		String message = InputValidationUtil.readNonEmptyString(ScannerUtil.getScanner(), "Enter message: ");

		sendNotificationToUser(message, type, selectedUser.getUserId());

		System.out.println("Notification sent successfully.");
	}


}
