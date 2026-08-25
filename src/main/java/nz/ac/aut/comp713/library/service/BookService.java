package nz.ac.aut.comp713.library.service;

import nz.ac.aut.comp713.library.domain.Book;
import nz.ac.aut.comp713.library.repository.BookRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class BookService {

    @Inject
    private BookRepository bookRepository;

    public List<Book> getBooks() {
        return bookRepository.findAll();
    }
}