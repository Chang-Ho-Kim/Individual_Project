package nz.ac.aut.comp713.library.service;

import nz.ac.aut.comp713.library.domain.Book;
import nz.ac.aut.comp713.library.domain.Loan;
import nz.ac.aut.comp713.library.domain.Member;
import nz.ac.aut.comp713.library.repository.BookRepository;
import nz.ac.aut.comp713.library.repository.LoanRepository;
import nz.ac.aut.comp713.library.repository.MemberRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class LoanService {

    @Inject
    private LoanRepository loanRepository;

    @Inject
    private BookRepository bookRepository;

    @Inject
    private MemberRepository memberRepository;

    public List<Loan> getLoans() {
        return loanRepository.findAll();
    }

    public Loan borrowBook(Long memberId, Long bookId) {

        Member member = memberRepository.findById(memberId);

        if (member == null) {
            throw new IllegalArgumentException("Member does not exist");
        }

        Book book = bookRepository.findById(bookId);

        if (book == null) {
            throw new IllegalArgumentException("Book does not exist");
        }

        if (!book.isAvailable()) {
            throw new IllegalArgumentException("Book is not available");
        }

        if (loanRepository.hasActiveLoan(bookId)) {
            throw new IllegalArgumentException("Book is already borrowed");
        }

        Loan loan = loanRepository.create(memberId, bookId);
        bookRepository.setAvailability(bookId, false);
        return loan;
    }

    public void returnBook(Long loanId) {

        Loan loan = loanRepository.findActiveLoan(loanId);

        if (loan == null) {
            throw new IllegalArgumentException("Active loan does not exist");
        }

        loanRepository.returnLoan(loanId);
        bookRepository.setAvailability(loan.getBookId(), true);
    }

    public List<Loan> getLoansForMember(Long memberId) {

        Member member = memberRepository.findById(memberId);

        if (member == null) {
            throw new IllegalArgumentException("Member does not exist");
        }

        return loanRepository.findByMemberId(memberId);
    }
}