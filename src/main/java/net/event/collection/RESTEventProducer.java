package net.event.collection;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import net.event.Event;
import net.event.store.EventStore;
import net.security.ApiKeyAuthenticationService;
import net.security.AuthenticatedClient;
import net.security.Authorization;



/**
 * REST service for creating and updating the Events
 * 
 * @author Amit Singh
 *
 */
@Path("/event")
public class RESTEventProducer {

	private EventStore store;
	@Context
	private SecurityContext securityContext;
	
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
		AuthenticatedClient client = Authorization.requireRole(securityContext, ApiKeyAuthenticationService.PRODUCER_ROLE);

		Event event = new Event();
		event.setEventType(eventType);
		event.setSource(client.getName());
		event.setMessage(message);
		event.setDestination(destination);
		event.setExpireInMinutes(expireInMinutes);
		
		return Response.ok("EventId="+store.addNewEvent(event), "text/plain").build();
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
		Authorization.requireRole(securityContext, ApiKeyAuthenticationService.PRODUCER_ROLE);

		//TODO Finish the method
		// find event in store and update the status
		store.updateEventStatus(eventId, disable);
		
		return Response.ok("Event disabled", "text/plain").build();
	}

}
