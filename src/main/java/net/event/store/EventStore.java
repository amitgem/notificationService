package net.event.store;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.event.Event;
import net.event.EventRef;
import net.event.EventState;
import net.notification.Notification;
import net.notification.dispatch.NotificationStore;

/**
 * Coordinates event lifecycle changes and keeps publication ownership in the
 * event side of the system.
 */
public class EventStore {
	private static final EventStoreDAO DAO = new EventStoreDAO();
	private static final NotificationStore NOTIFICATION_STORE = new NotificationStore();
	private static final BlockingQueue<Long> EVENT_Q = new LinkedBlockingQueue<Long>();
	private static final Object LIFECYCLE_LOCK = new Object();

	public long addNewEvent(Event event) {
		long eventId = DAO.addEvent(event);
		EVENT_Q.offer(Long.valueOf(eventId));
		DAO.addEventLog(eventId, "Event accepted for asynchronous publication.");
		return eventId;
	}

	public EventRef updateEventStatus(long eventId, boolean disable) {
		synchronized (LIFECYCLE_LOCK) {
			EventRef ref = DAO.getEventRef(eventId);
			if (ref == null) {
				return null;
			}

			if (disable) {
				if (ref.getState() == EventState.NEW && !NOTIFICATION_STORE.hasNotifications(eventId)) {
					EventRef updated = DAO.setEventState(eventId, EventState.DISABLED);
					DAO.addEventLog(eventId, "Event disabled before publication started.");
					return updated;
				}
				DAO.addEventLog(eventId, "Disable rejected because publication already started.");
				return DAO.getEventRef(eventId);
			}

			if (ref.getState() == EventState.DISABLED && !NOTIFICATION_STORE.hasNotifications(eventId)) {
				EventRef updated = DAO.setEventState(eventId, EventState.NEW);
				EVENT_Q.offer(Long.valueOf(eventId));
				DAO.addEventLog(eventId, "Event re-enabled and queued again for publication.");
				return updated;
			}

			DAO.addEventLog(eventId, "Enable rejected because the event is no longer updateable.");
			return DAO.getEventRef(eventId);
		}
	}

	public EventRef getEventReport(long eventId) {
		return DAO.getEventRef(eventId);
	}

	/**
	 * Returns the next event that is still eligible for publication.
	 * 
	 * @return next eligible event
	 * @throws InterruptedException when the worker is interrupted
	 */
	public Event readEvent() throws InterruptedException {
		for (;;) {
			Long eventId = EVENT_Q.take();
			EventRef ref = DAO.getEventRef(eventId.longValue());
			if (ref != null && ref.getState() == EventState.NEW) {
				return DAO.getEvent(eventId.longValue());
			}
		}
	}

	public Event startPublication(long eventId) {
		synchronized (LIFECYCLE_LOCK) {
			if (!DAO.startPublication(eventId)) {
				return null;
			}
			DAO.addEventLog(eventId, "Publication claimed by notification processing.");
			return DAO.getEvent(eventId);
		}
	}

	public Notification addNotification(Notification notification) {
		synchronized (LIFECYCLE_LOCK) {
			Notification savedNotification = NOTIFICATION_STORE.addNotification(notification);
			DAO.incrementPublishedChannelCount(notification.getSourceEventId());
			DAO.addEventLog(notification.getSourceEventId(),
					"Notification persisted for channel " + notification.getMedium() + ".");
			return savedNotification;
		}
	}

	public EventRef completePublication(long eventId) {
		synchronized (LIFECYCLE_LOCK) {
			EventRef updated = DAO.setEventState(eventId, EventState.ARCHIVED);
			if (updated != null) {
				DAO.addEventLog(eventId, "Notification publishing finished.");
			}
			return updated;
		}
	}

	public void addEventLog(long eventId, String logEntry) {
		DAO.addEventLog(eventId, logEntry);
	}
}
