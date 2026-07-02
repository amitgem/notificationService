package net.event.collection;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import net.event.Event;
import net.event.EventRef;
import net.event.EventState;
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

	private final EventStore store = new EventStore();
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

		EventRef eventRef = store.updateEventStatus(eventId, disable);
		if (eventRef == null) {
			return Response.status(Status.NOT_FOUND).entity("Unknown eventId=" + eventId).type(MediaType.TEXT_PLAIN).build();
		}
		if (disable && eventRef.getState() != EventState.DISABLED) {
			return Response.status(Status.CONFLICT)
					.entity("EventId=" + eventId + " cannot be disabled after publication has started.")
					.type(MediaType.TEXT_PLAIN).build();
		}
		if (!disable && eventRef.getState() != EventState.NEW) {
			return Response.status(Status.CONFLICT)
					.entity("EventId=" + eventId + " cannot be re-enabled in state " + eventRef.getState() + ".")
					.type(MediaType.TEXT_PLAIN).build();
		}

		String statusMessage = disable ? "Event disabled" : "Event enabled";
		return Response.ok(statusMessage + " EventId=" + eventId, "text/plain").build();
	}

	@POST
	@Path("/{eventId}/start-publication")
	@Produces(MediaType.TEXT_PLAIN)
	public Response startPublication(@PathParam("eventId") long eventId) {
		Authorization.requireRole(securityContext, ApiKeyAuthenticationService.PRODUCER_ROLE);

		Event claimedEvent = store.startPublication(eventId);
		if (claimedEvent == null) {
			EventRef eventRef = store.getEventReport(eventId);
			if (eventRef == null) {
				return Response.status(Status.NOT_FOUND).entity("Unknown eventId=" + eventId).type(MediaType.TEXT_PLAIN)
						.build();
			}
			return Response.status(Status.CONFLICT)
					.entity("Publication cannot be claimed in state " + eventRef.getState() + ".")
					.type(MediaType.TEXT_PLAIN).build();
		}

		return Response.ok("Publication claimed for EventId=" + eventId, "text/plain").build();
	}
}
