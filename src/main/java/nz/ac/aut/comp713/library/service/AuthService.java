package nz.ac.aut.comp713.library.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import nz.ac.aut.comp713.library.repository.AuthRepository;
import nz.ac.aut.comp713.library.repository.AuthUser;
import nz.ac.aut.comp713.library.security.TokenStore;

@ApplicationScoped
public class AuthService {

    @Inject
    private AuthRepository authRepository;

    @Inject
    private TokenStore tokenStore;

    public AuthUser authenticate(
            String email,
            String password) {

        AuthUser user = authRepository.findByEmail(email);

        if (user == null) {
            return null;
        }

        if (!user.getPassword().equals(password)) {
            return null;
        }

        return user;
    }

    public String createToken(Long memberId) {
        return tokenStore.createToken(memberId);
    }

    public Long getMemberId(String token) {
        return tokenStore.getMemberId(token);
    }
}