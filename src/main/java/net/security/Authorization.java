package net.security;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

public final class Authorization {
	private Authorization() {
	}

	public static AuthenticatedClient requireRole(SecurityContext securityContext, String role) {
		if (securityContext == null || securityContext.getUserPrincipal() == null) {
			throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
					.entity("Authentication required")
					.type("text/plain")
					.build());
		}

		if (!securityContext.isUserInRole(role)) {
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN)
					.entity("Insufficient permissions")
					.type("text/plain")
					.build());
		}

		return (AuthenticatedClient) securityContext.getUserPrincipal();
	}
}
