package net.event.store;

import java.util.concurrent.BlockingQueue;

import net.event.Event;
import net.event.EventRef;

/**
 * @author test
 *
 */
public class EventStore {

	private EventStoreDAO dao;
	private BlockingQueue<Event> eventQ;

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
	
	
	/**
	 * Returns the highest priority Explicitly locks the record in DB for editing.
	 * 
	 * @return
	 * @throws InterruptedException 
	 */
	public Event readEvent() throws InterruptedException{
		return eventQ.take();
	}
	
	public void addEventLog(long eventId, String logEntry){
		// TODO add event log to DB
		
	}
	

}
