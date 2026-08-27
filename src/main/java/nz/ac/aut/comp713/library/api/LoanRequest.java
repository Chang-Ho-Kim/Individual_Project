package nz.ac.aut.comp713.library.api;

public class LoanRequest {

    private Long bookId;

    public LoanRequest() {
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}