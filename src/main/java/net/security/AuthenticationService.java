package net.security;

public interface AuthenticationService {
	AuthenticatedClient authenticate(String clientId, String apiKey) throws AuthenticationException;
}
