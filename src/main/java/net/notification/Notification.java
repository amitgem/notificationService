package net.notification;

public class Notification {
	private long notificationId;
	private long sourceEventId;
	private NotificationMedium medium;
	private String message;
	private NotificationState state;
	private NotificationDestination dest;

	public long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(long notificationId) {
		this.notificationId = notificationId;
	}

	public long getSourceEventId() {
		return sourceEventId;
	}

	public void setSourceEventId(long sourceEventId) {
		this.sourceEventId = sourceEventId;
	}

	public NotificationMedium getMedium() {
		return medium;
	}

	public void setMedium(NotificationMedium medium) {
		this.medium = medium;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public NotificationState getState() {
		return state;
	}

	public void setState(NotificationState state) {
		this.state = state;
	}

	public NotificationDestination getDest() {
		return dest;
	}

	public void setDest(NotificationDestination dest) {
		this.dest = dest;
	}
}
