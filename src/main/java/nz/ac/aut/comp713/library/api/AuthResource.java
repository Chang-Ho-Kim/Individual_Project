package nz.ac.aut.comp713.library.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import nz.ac.aut.comp713.library.repository.AuthUser;
import nz.ac.aut.comp713.library.service.AuthService;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    private AuthService authService;

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {

        try {

            AuthUser user = authService.authenticate(
                    request.getEmail(),
                    request.getPassword()
            );

            if (user == null) {

                return Response
                        .status(Response.Status.UNAUTHORIZED)
                        .entity("Invalid email or password")
                        .build();
            }

            String token = authService.createToken(
                    user.getId()
            );

            LoginResponse response = new LoginResponse(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    token
            );

            return Response
                    .ok(response)
                    .build();

        } catch (RuntimeException e) {

            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Database is currently unavailable")
                    .build();
        }
    }
}

