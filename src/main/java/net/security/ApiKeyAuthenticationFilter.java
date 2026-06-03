package net.security;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

public class ApiKeyAuthenticationFilter implements ContainerRequestFilter {
	private static final String CLIENT_ID_HEADER = "X-Client-Id";
	private static final String API_KEY_HEADER = "X-API-Key";
	private static final String AUTH_SCHEME = "API-Key";

	private final AuthenticationService authenticationService;

	public ApiKeyAuthenticationFilter() {
		this(new ApiKeyAuthenticationService());
	}

	public ApiKeyAuthenticationFilter(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	public ContainerRequest filter(ContainerRequest request) {
		String clientId = request.getHeaderValue(CLIENT_ID_HEADER);
		String apiKey = request.getHeaderValue(API_KEY_HEADER);

		try {
			AuthenticatedClient client = authenticationService.authenticate(clientId, apiKey);
			boolean secure = "https".equalsIgnoreCase(request.getRequestUri().getScheme());
			request.setSecurityContext(new NotificationSecurityContext(client, AUTH_SCHEME, secure));
			return request;
		} catch (AuthenticationException e) {
			throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
					.entity("Authentication required")
					.type("text/plain")
					.build());
		}
	}
}
