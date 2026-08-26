package nz.ac.aut.comp713.library.api;

public class LoanRequest {

    private Long memberId;
    private Long bookId;

    public LoanRequest() {
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}