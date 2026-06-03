package net.security;

import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AuthenticatedClient implements Principal {
	private final String name;
	private final Set<String> roles;

	public AuthenticatedClient(String name, Set<String> roles) {
		this.name = name;
		this.roles = Collections.unmodifiableSet(new HashSet<String>(roles));
	}

	public String getName() {
		return name;
	}

	public boolean hasRole(String role) {
		return roles.contains(role);
	}

	public Set<String> getRoles() {
		return roles;
	}
}
