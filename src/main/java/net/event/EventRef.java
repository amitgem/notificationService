package net.event;

import java.util.List;

public class EventRef {
	private int eventId;
	private EventState state;
	private List<EventLog> log;
	public int getEventId() {
		return eventId;
	}
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	public EventState getState() {
		return state;
	}
	public void setState(EventState state) {
		this.state = state;
	}
	public List<EventLog> getLog() {
		return log;
	}
	public void setLog(List<EventLog> log) {
		this.log = log;
	}
	
	
	
	
	
}
