package nz.ac.aut.comp713.library.api;

import nz.ac.aut.comp713.library.domain.Loan;
import nz.ac.aut.comp713.library.security.AuthHelper;
import nz.ac.aut.comp713.library.service.LoanService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/loans")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoanResource {

    @Inject
    private LoanService loanService;

    @Inject
    private AuthHelper authHelper;

    @GET
    public Response getLoans(
            @Context HttpHeaders headers) {

        try {

            Long memberId =
                    authHelper.getAuthenticatedMemberId(headers);

            List<Loan> loans =
                    loanService.getLoansForMember(memberId);

            return Response
                    .ok(loans)
                    .build();

        } catch (AuthHelper.UnauthorizedException e) {

            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @POST
    public Response borrowBook(
            LoanRequest request,
            @Context HttpHeaders headers) {

        try {

            Long memberId =
                    authHelper.getAuthenticatedMemberId(headers);

            Loan loan = loanService.borrowBook(
                    memberId,
                    request.getBookId()
            );

            return Response
                    .status(Response.Status.CREATED)
                    .entity(loan)
                    .build();

        } catch (AuthHelper.UnauthorizedException e) {

            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity(e.getMessage())
                    .build();

        } catch (IllegalArgumentException e) {

            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}/return")
    public Response returnBook(
            @PathParam("id") Long loanId,
            @Context HttpHeaders headers) {

        try {

            Long memberId =
                    authHelper.getAuthenticatedMemberId(headers);

            loanService.returnBook(
                    loanId,
                    memberId
            );

            return Response
                    .ok()
                    .entity("Book returned successfully")
                    .build();

        } catch (AuthHelper.UnauthorizedException e) {

            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity(e.getMessage())
                    .build();

        } catch (IllegalArgumentException e) {

            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/member/{memberId}")
    public Response getLoansForMember(
            @PathParam("memberId") Long requestedMemberId,
            @Context HttpHeaders headers) {

        try {

            Long authenticatedMemberId =
                    authHelper.getAuthenticatedMemberId(headers);

            if (!authenticatedMemberId.equals(requestedMemberId)) {

                return Response
                        .status(Response.Status.FORBIDDEN)
                        .entity("You can only view your own loans")
                        .build();
            }

            List<Loan> loans =
                    loanService.getLoansForMember(
                            authenticatedMemberId
                    );

            return Response
                    .ok(loans)
                    .build();

        } catch (AuthHelper.UnauthorizedException e) {

            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity(e.getMessage())
                    .build();

        } catch (IllegalArgumentException e) {

            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }
}