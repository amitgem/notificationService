package net.event.store;

import net.event.Event;
import net.event.EventRef;

public class EventStore {

	private EventStoreDAO dao;

	public long addNewEvent(Event event) {

		return dao.addEvent(event);
	}

	public EventRef updateEventStatus(long eventId, boolean disable) {
		// Check if the event is not delivered completely (archived) then
		// disable else do nothing and return the status
		return null; // TODO
	}

	public EventRef getEventReport(long eventId) {
		return null; // TODO
	}

}
