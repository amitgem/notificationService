package net.notification.dispatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import net.notification.Notification;
import net.notification.NotificationState;

public class NotificationStore {
	private final AtomicLong notificationIds = new AtomicLong();
	private final Map<Long, List<Notification>> notificationsByEventId = new ConcurrentHashMap<Long, List<Notification>>();

	public synchronized Notification addNotification(Notification notification) {
		long notificationId = notificationIds.incrementAndGet();
		notification.setNotificationId(notificationId);
		if (notification.getState() == null) {
			notification.setState(NotificationState.NEW);
		}

		List<Notification> notifications = notificationsByEventId.get(notification.getSourceEventId());
		if (notifications == null) {
			notifications = Collections.synchronizedList(new ArrayList<Notification>());
			notificationsByEventId.put(notification.getSourceEventId(), notifications);
		}
		notifications.add(copyNotification(notification));
		return copyNotification(notification);
	}

	public synchronized boolean hasNotifications(long eventId) {
		List<Notification> notifications = notificationsByEventId.get(eventId);
		return notifications != null && !notifications.isEmpty();
	}

	public synchronized List<Notification> getNotifications(long eventId) {
		List<Notification> notifications = notificationsByEventId.get(eventId);
		List<Notification> copies = new ArrayList<Notification>();
		if (notifications == null) {
			return copies;
		}
		synchronized (notifications) {
			for (int i = 0; i < notifications.size(); i++) {
				copies.add(copyNotification(notifications.get(i)));
			}
		}
		return copies;
	}

	private Notification copyNotification(Notification source) {
		Notification copy = new Notification();
		copy.setNotificationId(source.getNotificationId());
		copy.setSourceEventId(source.getSourceEventId());
		copy.setMedium(source.getMedium());
		copy.setMessage(source.getMessage());
		copy.setState(source.getState());
		copy.setDest(source.getDest());
		return copy;
	}
}
