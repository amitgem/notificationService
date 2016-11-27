package net.event.collection;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.event.EventRef;
import net.event.store.EventStore;



/**
 * REST service for creating and updating the Events
 * 
 * @author Amit Singh
 *
 */
@Path("/event")
public class RESTEventProducer {

	private EventStore store;
	
	/**
	 * REST method to create a new Event
	 * 
	 * @param eventType
	 * @param source
	 * @param message
	 * @param destination
	 * @param expireInMinutes
	 * @return
	 */
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public Response addEvent(@FormParam("type") String eventType, @FormParam("src") String source,
			@FormParam("msg") String message, @FormParam("dest") String destination,
			@FormParam("life") long expireInMinutes) {

		//TODO Finish the method
		// Create event object and save it to store
		
		return Response.ok("EventId="+store.addNewEvent(null), "text/plain").build();
	}

	/**
	 * REST method to disable/enable an event
	 * 
	 * @param eventId
	 * @param disable
	 * @return
	 */
	@PUT
	@Produces(MediaType.TEXT_PLAIN)
	public Response updateEvent(@FormParam("eventId") long eventId, @FormParam("disable") boolean disable) {

		//TODO Finish the method
		// find event in store and update the status
		store.updateEventStatus(eventId, disable);
		
		return Response.ok("Event disabled", "text/plain").build();
	}

}
