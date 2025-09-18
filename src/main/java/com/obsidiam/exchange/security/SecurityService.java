package com.obsidiam.exchange.security;

import org.springframework.stereotype.Component;

@Component
public class SecurityService {

	private static final String BASE64_CREDENTIALS = "dXNlcjpwYXNzd29yZA==";

	private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ==";

	public String getToken(String credentials) {
		// TODO: create a token from the base64 credentials
		return TOKEN;
	}

	public boolean isAuthorized(String token) {
		// TODO: validate token
		if (token.substring(7).equals(TOKEN)) {
			return true;
		}
		throw new SecurityException();
	}
}
