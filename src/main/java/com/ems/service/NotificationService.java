package com.ems.service;

import com.ems.enums.NotificationType;

public interface NotificationService {

	// send notifications
	void sendSystemWideNotification(String message, NotificationType notificationType);

	void sendEventNotification(int eventId, String message, NotificationType type);

	// read notifications
	void displayUnreadNotifications(int userId);

	//
	void sendPersonalNotification(int userId, String message, NotificationType type);

	void displayAllNotifications(int userId);
}
