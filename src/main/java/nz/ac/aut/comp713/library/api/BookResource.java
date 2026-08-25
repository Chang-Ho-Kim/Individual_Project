package nz.ac.aut.comp713.library.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/books")
public class BookResource {

    @GET
    @Produces("application/json")
    public String getBooks() {
        return "[{\"[TEST] id\":1,\"title\":\"The Hobbit\",\"author\":\"J.R.R. Tolkien\",\"available\":true}]";
    }
}