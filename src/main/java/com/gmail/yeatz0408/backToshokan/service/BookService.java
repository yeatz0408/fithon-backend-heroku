package com.gmail.yeatz0408.backToshokan.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gmail.yeatz0408.backToshokan.dao.BookRepository;
import com.gmail.yeatz0408.backToshokan.dao.CheckoutRepository;
import com.gmail.yeatz0408.backToshokan.dao.HistoryRepository;
import com.gmail.yeatz0408.backToshokan.entity.Book;
import com.gmail.yeatz0408.backToshokan.entity.Checkout;
import com.gmail.yeatz0408.backToshokan.entity.History;
import com.gmail.yeatz0408.backToshokan.responsemodels.ShelfCurrentLoansResponse;



@Service
@Transactional
public class BookService {

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private CheckoutRepository checkoutRepo;

    @Autowired
    private HistoryRepository historyRepo;

    // public BookService(BookRepository bookRepo, CheckoutRepository checkoutRepo) {
    //     this.bookRepo = bookRepo;
    //     this.checkoutRepo = checkoutRepo;
    // }

    public Book checkoutBook(String userEmail, Long bookId) throws Exception {
        
        Optional<Book> book = bookRepo.findById(bookId);

        Checkout validateCheckout = checkoutRepo.findByUserEmailAndBookId(userEmail, bookId);

        if (!book.isPresent() || validateCheckout != null || book.get().getCopiesAvailable() <= 0) {
            throw new Exception("本が存在しないかすでにチェックアウトされました。");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);
        bookRepo.save(book.get());

        Checkout checkout = new Checkout(
            userEmail,
            LocalDate.now().toString(),
            LocalDate.now().plusDays(7).toString(),
            book.get().getId()
        );

        checkoutRepo.save(checkout);

        return book.get();
    }

    public Boolean checkoutBookByUser(String userEmail, Long bookId) {
        Checkout validateCheckout = checkoutRepo.findByUserEmailAndBookId(userEmail, bookId);

        if (validateCheckout != null) {
            return true;
        } else {
            return false;
        }
    }

    public int currentLoansCount(String userEmail) {
        return checkoutRepo.findBooksByUserEmail(userEmail).size();
    }

    public List<ShelfCurrentLoansResponse> currentLoans(String userEmail) throws Exception {
        List<ShelfCurrentLoansResponse> shelfCurrentLoansResponses = new ArrayList<>();

        List<Checkout> checkoutList = checkoutRepo.findBooksByUserEmail(userEmail);
        List<Long> bookIdList = new ArrayList<>();

        for (Checkout i : checkoutList) {
            bookIdList.add(i.getBookId());
        }

        List<Book> books = bookRepo.findBooksByBookIds(bookIdList);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (Book book: books) {
            Optional<Checkout> checkout = checkoutList.stream()
                .filter(x -> x.getBookId() == book.getId()).findFirst();

                if (checkout.isPresent()) {
                    java.util.Date d1 = sdf.parse(checkout.get().getReturnDate());
                    java.util.Date d2 = sdf.parse(LocalDate.now().toString());

                    TimeUnit time = TimeUnit.DAYS;

                    long difference_In_Time = time.convert(d1.getTime() - d2.getTime(), TimeUnit.MILLISECONDS);

                    shelfCurrentLoansResponses.add(new ShelfCurrentLoansResponse(book, (int) difference_In_Time));
                }
        }

        return shelfCurrentLoansResponses;
    }

    public void returnBook (String userEmail, Long bookId) throws Exception {
        Optional<Book> book = bookRepo.findById(bookId);

        Checkout validateCheckout = checkoutRepo.findByUserEmailAndBookId(userEmail, bookId);

        if (!book.isPresent() || validateCheckout == null) {
            throw new Exception ("Book doesn't exist or not checked out by user");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);

        bookRepo.save(book.get());
        checkoutRepo.deleteById(validateCheckout.getId());

        History history = new History(
            userEmail,
            validateCheckout.getCheckoutDate(),
            LocalDate.now().toString(),
            book.get().getTitle(),
            book.get().getAuthor(),
            book.get().getDescription(),
            book.get().getImg()
        );

        System.out.println("3284932489329dsflakf;da;f" + history.getTitle() );

        historyRepo.save(history);
        
    }

    public void renewLoan (String userEmail, Long bookId) throws Exception {
        Checkout validateCheckout = checkoutRepo.findByUserEmailAndBookId(userEmail, bookId);

        if (validateCheckout == null) {
            throw new Exception("book does not exist or not checked out by user");
        }

        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");

        java.util.Date d1 = sdFormat.parse(validateCheckout.getReturnDate());
        java.util.Date d2 = sdFormat.parse(LocalDate.now().toString());

        if (d1.compareTo(d2) > 0 || d1.compareTo(d2) == 0) {
            validateCheckout.setReturnDate(LocalDate.now().plusDays(7).toString());
            checkoutRepo.save(validateCheckout);
        }
    }

    public List<Book> topBooks() {

        List<Book> books = new ArrayList<Book>();

        List<Object[]> objects = historyRepo.findTopBooks();

        for (int i = 0; i < 5; i++) {

            // System.out.println("++++++++++++++++++" + objects.get(i)[0]);

            String title = objects.get(i)[0].toString();

            System.out.println("+++++++++++" + title + "+++++++");

            Optional<Book> optional = bookRepo.findByTitle(title);

            Book book = null;

            if (optional.isPresent()) {
                book = optional.get();
            }

            if (book != null) {
                books.add(book);
            }
        }

        return books;

    }

    
}
