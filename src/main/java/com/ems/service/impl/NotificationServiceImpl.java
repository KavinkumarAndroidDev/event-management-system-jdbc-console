package com.ems.service.impl;

import java.util.Comparator;
import java.util.List;

import com.ems.dao.NotificationDao;
import com.ems.dao.RegistrationDao;
import com.ems.enums.NotificationType;
import com.ems.exception.DataAccessException;
import com.ems.model.Notification;
import com.ems.service.NotificationService;
import com.ems.service.SystemLogService;
import com.ems.util.MenuHelper;
import com.ems.util.PaginationUtil;

/*
 * Handles notification related business operations.
 *
 * Responsibilities:
 * - Send notifications to users and groups
 * - Display user notifications
 * - Maintain read and unread notification states
 */
public class NotificationServiceImpl implements NotificationService {

    private final NotificationDao notificationDao;
    private final RegistrationDao registrationDao;
    private final SystemLogService systemLogService;

    /*
     * Initializes notification service with required data access dependencies.
     */
    public NotificationServiceImpl(NotificationDao notificationDao,
            RegistrationDao registrationDao, SystemLogService systemLogService) {
        this.notificationDao = notificationDao;
        this.registrationDao = registrationDao;
        this.systemLogService = systemLogService;
    }

    /*
     * Sends a notification to all users in the system.
     * Used for system-level or promotional announcements.
     */
    @Override
    public void sendSystemWideNotification(String message, NotificationType notificationType)
            throws DataAccessException {
        notificationDao.sendSystemWideNotification(message, notificationType);
        systemLogService.log(null, "SEND_NOTIFICATION", "SYSTEM", null,
                "System-wide notification sent");
        System.out.println("The message has been sent to all users.");
    }

    /*
     * Displays unread notifications for a user.
     * Notifications are automatically marked as read after display.
     */
    @Override
    public void displayUnreadNotifications(int userId) throws DataAccessException {
        List<Notification> notifications = notificationDao.getUnreadNotifications(userId);

        if (!notifications.isEmpty()) {
            System.out.println("\nYou have unread notifications:");
            notifications.sort(Comparator.comparing(Notification::getCreatedAt).reversed());

            PaginationUtil.paginate(notifications,
                    (page, i) -> MenuHelper.displayNotifications(page));

            // Mark all as read after viewing
            for (Notification n : notifications) {
                notificationDao.markAsRead(n.getNotificationId());
            }
        }
    }

    /*
     * Displays all notifications for a user.
     * All notifications are marked as read once displayed.
     */
    @Override
    public void displayAllNotifications(int userId) throws DataAccessException {
        List<Notification> notifications = notificationDao.getAllNotifications(userId);

        PaginationUtil.paginate(notifications,
                (page, i) -> MenuHelper.displayNotifications(page));

        if (!notifications.isEmpty()) {
            notificationDao.markAllAsRead(userId);
        }
    }

    /*
     * Sends a notification to all users registered for a specific event.
     * Used for event updates or schedule changes.
     */
    @Override
    public void sendEventNotification(int eventId, String message, NotificationType type) throws DataAccessException {
        List<Integer> userIds = registrationDao.getRegisteredUserIdsByEvent(eventId);
        for (Integer userId : userIds) {
            notificationDao.sendNotification(userId, message, type);
        }
        systemLogService.log(null, "SEND_NOTIFICATION", "EVENT", eventId,
                "Event notification sent to registered users");
    }

    @Override
    public void sendPersonalNotification(int userId, String message, NotificationType type) throws DataAccessException {
        notificationDao.sendNotification(userId, message, type);
    }
}