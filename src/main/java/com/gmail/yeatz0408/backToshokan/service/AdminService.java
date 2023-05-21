package com.gmail.yeatz0408.backToshokan.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gmail.yeatz0408.backToshokan.dao.BookRepository;
import com.gmail.yeatz0408.backToshokan.dao.CheckoutRepository;
import com.gmail.yeatz0408.backToshokan.dao.ReviewRepository;
import com.gmail.yeatz0408.backToshokan.entity.Book;
import com.gmail.yeatz0408.backToshokan.requestmodels.AddBookRequest;


@Service
@Transactional
public class AdminService {

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    private CheckoutRepository checkoutRepo;

    public void postBook(AddBookRequest addBookRequest) {
        Book book = new Book();
        book.setTitle(addBookRequest.getTitle());
        book.setAuthor(addBookRequest.getAuthor());
        book.setDescription(addBookRequest.getDescription());
        book.setCopies(addBookRequest.getCopies());
        book.setCopiesAvailable(addBookRequest.getCopies());
        book.setCategory(addBookRequest.getCategory());
        book.setImg(addBookRequest.getImg());
        bookRepo.save(book);

    }

    public void deleteBook(Long bookId) throws Exception {
        Optional<Book> book = bookRepo.findById(bookId);

        if (!book.isPresent()) {
            throw new Exception("book not found");
        }

        bookRepo.delete(book.get());
        checkoutRepo.deleteAllByBookId(bookId);
        reviewRepo.deleteAllByBookId(bookId);

    }

    public void increaseBookQuantity(Long bookId) throws Exception {
        Optional<Book> book = bookRepo.findById(bookId);

        if (!book.isPresent()) {
            throw new Exception("Book not found");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);
        book.get().setCopies(book.get().getCopies() + 1);

        bookRepo.save(book.get());
    }

    public void decreaseBookQuantity(Long bookId) throws Exception {
        Optional<Book> book = bookRepo.findById(bookId);

        if (!book.isPresent() || book.get().getCopiesAvailable() <= 0 || book.get().getCopies() <= 0) {
            throw new Exception("Book not found or quantity locked");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);
        book.get().setCopies(book.get().getCopies() - 1);

        bookRepo.save(book.get());
    }
    
}
