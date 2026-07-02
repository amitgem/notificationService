package net.event;

import net.event.store.EventStore;
import net.notification.Notification;
import net.notification.NotificationMedium;
import net.notification.NotificationState;

public class EventProcessor implements Runnable {
	private final EventStore store;
	private final Event event;

	EventProcessor(EventStore store, Event event) {
		this.store = store;
		this.event = event;
	}

	public void run() {
		Event claimedEvent = store.startPublication(event.getEventId());
		if (claimedEvent == null) {
			store.addEventLog(event.getEventId(),
					"Publication skipped because the event is no longer eligible for publishing.");
			return;
		}

		try {
			Notification notification = new Notification();
			notification.setSourceEventId(claimedEvent.getEventId());
			notification.setMedium(NotificationMedium.APP);
			notification.setMessage(claimedEvent.getMessage());
			notification.setState(NotificationState.QUEUED);
			store.addNotification(notification);
			store.completePublication(claimedEvent.getEventId());
		} catch (RuntimeException ex) {
			store.addEventLog(claimedEvent.getEventId(),
					"Notification publishing failed: " + ex.getMessage());
		}
	}
}
