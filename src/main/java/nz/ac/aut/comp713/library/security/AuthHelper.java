package nz.ac.aut.comp713.library.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.HttpHeaders;

@ApplicationScoped
public class AuthHelper {

    @Inject
    private TokenStore tokenStore;

    public Long getAuthenticatedMemberId(HttpHeaders headers) {

        String authorization =
                headers.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authorization == null ||
            !authorization.startsWith("Bearer ")) {

            throw new UnauthorizedException(
                    "Authentication required"
            );
        }

        String token =
                authorization.substring("Bearer ".length()).trim();

        if (token.isEmpty()) {
            throw new UnauthorizedException(
                    "Authentication required"
            );
        }

        Long memberId = tokenStore.getMemberId(token);

        if (memberId == null) {
            throw new UnauthorizedException(
                    "Invalid or expired token"
            );
        }

        return memberId;
    }

    public static class UnauthorizedException
            extends RuntimeException {

        public UnauthorizedException(String message) {
            super(message);
        }
    }
}