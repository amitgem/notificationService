package net.event;

public class Event {
	private String eventType;
	private String source;
	private Priority priority;
	private String message;
	private String destination;
	private long expireInMinutes;
	
	
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Priority getPriority() {
		return priority;
	}
	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public long getExpireInMinutes() {
		return expireInMinutes;
	}
	public void setExpireInMinutes(long expireInMinutes) {
		this.expireInMinutes = expireInMinutes;
	}
	
}
