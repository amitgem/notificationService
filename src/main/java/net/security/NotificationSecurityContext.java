package net.security;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

public class NotificationSecurityContext implements SecurityContext {
	private final AuthenticatedClient client;
	private final String authenticationScheme;
	private final boolean secure;

	public NotificationSecurityContext(AuthenticatedClient client, String authenticationScheme, boolean secure) {
		this.client = client;
		this.authenticationScheme = authenticationScheme;
		this.secure = secure;
	}

	public Principal getUserPrincipal() {
		return client;
	}

	public boolean isUserInRole(String role) {
		return client.hasRole(role);
	}

	public boolean isSecure() {
		return secure;
	}

	public String getAuthenticationScheme() {
		return authenticationScheme;
	}
}
