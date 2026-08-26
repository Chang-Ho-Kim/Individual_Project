package nz.ac.aut.comp713.library.domain;

import java.time.LocalDateTime;

public class Loan {

    private Long id;
    private Long memberId;
    private Long bookId;
    private LocalDateTime borrowedAt;
    private LocalDateTime returnedAt;

    public Loan() {
    }

    public Loan(Long id, Long memberId, Long bookId,
                LocalDateTime borrowedAt, LocalDateTime returnedAt) {
        this.id = id;
        this.memberId = memberId;
        this.bookId = bookId;
        this.borrowedAt = borrowedAt;
        this.returnedAt = returnedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getBorrowedAt() {
        return borrowedAt;
    }

    public void setBorrowedAt(LocalDateTime borrowedAt) {
        this.borrowedAt = borrowedAt;
    }

    public LocalDateTime getReturnedAt() {
        return returnedAt;
    }

    public void setReturnedAt(LocalDateTime returnedAt) {
        this.returnedAt = returnedAt;
    }
}