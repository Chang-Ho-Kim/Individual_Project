package nz.ac.aut.comp713.library.security;

import jakarta.enterprise.context.ApplicationScoped;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class TokenStore {

    private final Map<String, Long> tokens =
            new ConcurrentHashMap<>();

    private final SecureRandom secureRandom =
            new SecureRandom();

    public String createToken(Long memberId) {

        byte[] bytes = new byte[32];

        secureRandom.nextBytes(bytes);

        String token = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);

        tokens.put(token, memberId);

        return token;
    }

    public Long getMemberId(String token) {
        return tokens.get(token);
    }

    public void removeToken(String token) {
        tokens.remove(token);
    }
}