package nz.ac.aut.comp713.library.api;

import nz.ac.aut.comp713.library.domain.Book;
import nz.ac.aut.comp713.library.service.BookService;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.util.List;

@Path("/books")
@Produces("application/json")
public class BookResource {

    @Inject
    private BookService bookService;

    @GET
    public List<Book> getBooks() {
        return bookService.getBooks();
    }
}