package net.event.store;

import net.event.Event;
import net.event.EventRef;

public class EventStoreDAO {
	
	
	public long addEvent(Event event){
		// TODO Save the event object to DB
		return 0;
	}
	
	
	public EventRef updateEvent(long eventId, EventRef ref){
		// TODO Update event object in DB
		
		return ref;
	}	
	
}
