package net.security;

import java.security.MessageDigest;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ApiKeyAuthenticationService implements AuthenticationService {
	public static final String PRODUCER_ROLE = "EVENT_PRODUCER";
	private static final String API_KEYS_PROPERTY = "notification.api.keys";
	private static final String API_KEYS_ENV = "NOTIFICATION_API_KEYS";

	private final Map<String, ClientCredential> credentials;

	public ApiKeyAuthenticationService() {
		this(readConfiguredCredentials());
	}

	public ApiKeyAuthenticationService(String configuredCredentials) {
		this.credentials = Collections.unmodifiableMap(parseCredentials(configuredCredentials));
	}

	public AuthenticatedClient authenticate(String clientId, String apiKey) throws AuthenticationException {
		if (isBlank(clientId) || isBlank(apiKey)) {
			throw new AuthenticationException("Missing credentials");
		}

		ClientCredential credential = credentials.get(clientId);
		if (credential == null || !matches(apiKey, credential.apiKey)) {
			throw new AuthenticationException("Invalid credentials");
		}

		return new AuthenticatedClient(clientId, credential.roles);
	}

	private static String readConfiguredCredentials() {
		String propertyValue = System.getProperty(API_KEYS_PROPERTY);
		if (!isBlank(propertyValue)) {
			return propertyValue;
		}
		return System.getenv(API_KEYS_ENV);
	}

	private static Map<String, ClientCredential> parseCredentials(String configuredCredentials) {
		Map<String, ClientCredential> parsed = new HashMap<String, ClientCredential>();
		if (isBlank(configuredCredentials)) {
			return parsed;
		}

		String[] clientEntries = configuredCredentials.split(";");
		for (int i = 0; i < clientEntries.length; i++) {
			String entry = clientEntries[i].trim();
			if (entry.length() == 0) {
				continue;
			}

			String[] parts = entry.split(":", 3);
			if (parts.length < 2 || isBlank(parts[0]) || isBlank(parts[1])) {
				throw new IllegalArgumentException("Invalid API key entry. Expected clientId:apiKey[:role1,role2]");
			}

			Set<String> roles = parseRoles(parts.length == 3 ? parts[2] : null);
			parsed.put(parts[0].trim(), new ClientCredential(parts[1].trim(), roles));
		}
		return parsed;
	}

	private static Set<String> parseRoles(String roleList) {
		Set<String> roles = new HashSet<String>();
		if (isBlank(roleList)) {
			roles.add(PRODUCER_ROLE);
			return roles;
		}

		String[] configuredRoles = roleList.split(",");
		for (int i = 0; i < configuredRoles.length; i++) {
			String role = configuredRoles[i].trim();
			if (role.length() > 0) {
				roles.add(role);
			}
		}
		return roles;
	}

	private static boolean matches(String presented, String expected) {
		byte[] presentedBytes = presented.getBytes();
		byte[] expectedBytes = expected.getBytes();
		return MessageDigest.isEqual(presentedBytes, expectedBytes);
	}

	private static boolean isBlank(String value) {
		return value == null || value.trim().length() == 0;
	}

	private static final class ClientCredential {
		private final String apiKey;
		private final Set<String> roles;

		private ClientCredential(String apiKey, Set<String> roles) {
			this.apiKey = apiKey;
			this.roles = Collections.unmodifiableSet(new HashSet<String>(roles));
		}
	}
}
