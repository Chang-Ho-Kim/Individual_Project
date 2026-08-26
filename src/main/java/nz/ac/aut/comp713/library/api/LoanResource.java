package nz.ac.aut.comp713.library.api;

import nz.ac.aut.comp713.library.domain.Loan;
import nz.ac.aut.comp713.library.service.LoanService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/loans")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoanResource {

    @Inject
    private LoanService loanService;

    @GET
    public List<Loan> getLoans() {
        return loanService.getLoans();
    }

    @POST
    public Response borrowBook(LoanRequest request) {

        try {
            Loan loan = loanService.borrowBook(
                request.getMemberId(),
                request.getBookId()
            );

            return Response
                .status(Response.Status.CREATED)
                .entity(loan)
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
    public Response returnBook(@PathParam("id") Long loanId) {

        try {
            loanService.returnBook(loanId);

            return Response
                .ok()
                .entity("Book returned successfully")
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
            @PathParam("memberId") Long memberId) {

        try {

            List<Loan> loans = loanService.getLoansForMember(memberId);

            return Response
                .ok(loans)
                .build();

        } catch (IllegalArgumentException e) {

            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(e.getMessage())
                .build();
        }
    }
}