package net.event.store;

import net.event.Event;
import net.event.EventRef;

public class EventStore {

	private EventStoreDAO dao;

	public long addNewEvent(Event event) {

		return dao.addEvent(event);
	}

	public EventRef updateEventStatus(long eventId, boolean disable) {
		// Check the event status. Disable if it is not delivered completely (archived)
		// else do nothing and return the status
		return null; // TODO
	}

	public EventRef getEventReport(long eventId) {
		return null; // TODO
	}
	
	public Event getHighestPriorityEvent(){
		
		
		return null;
	}
	
	public void addEventLog(long eventId, String logEntry){
		// TODO add event log to DB
		
	}
	

}
