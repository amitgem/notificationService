package net.event;

import java.util.List;

public class EventRef {
	private long eventId;
	private EventState state;
	private List<EventLog> log;
	private int publishedChannelCount;

	public long getEventId() {
		return eventId;
	}
	public void setEventId(long eventId) {
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

	public int getPublishedChannelCount() {
		return publishedChannelCount;
	}

	public void setPublishedChannelCount(int publishedChannelCount) {
		this.publishedChannelCount = publishedChannelCount;
	}
}
