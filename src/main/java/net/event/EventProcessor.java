package net.event;

public class EventProcessor implements Runnable {
	private final Event event;

	EventProcessor(Event event) {
		this.event = event;
	}

	public void run() {
		// Read and process the events
		// 1. From Subscription Management finds the target users and their
		// 		notification channels
		// 2. From User Session Management finds the active Notification
		// 		channels for the users and the user data for sending the notification
		// 3. Formats the notification for the channels
		// 4. Pushes the notification in the delivery channel’s queue
		// 5. Updates the Event log in EventStore with any failures in delivery

	}
}
