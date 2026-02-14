package com.ems.service;

public interface NotificationService {

	// send notifications
	void sendSystemWideNotification(String message, String notificationType);

	void sendEventNotification(int eventId, String message, String type);

	// read notifications
	void displayUnreadNotifications(int userId);
	
	//
	void sendPersonalNotification(int userId, String message, String type);

	void displayAllNotifications(int userId);
}
