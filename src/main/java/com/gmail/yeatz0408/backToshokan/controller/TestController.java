package com.gmail.yeatz0408.backToshokan.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gmail.yeatz0408.backToshokan.dao.BookRepository;
import com.gmail.yeatz0408.backToshokan.dao.HistoryRepository;
import com.gmail.yeatz0408.backToshokan.entity.Book;
import com.gmail.yeatz0408.backToshokan.requestmodels.AddBookRequest;
import com.gmail.yeatz0408.backToshokan.service.AdminService;
import com.gmail.yeatz0408.backToshokan.service.BookService;

@CrossOrigin("http://yeatz0408.github.io/fithon-front")
@RestController
public class TestController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private HistoryRepository historyRepo;

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private BookService bookService;

    @PostMapping("/addBook")
    public void postBook(@RequestBody AddBookRequest addBookRequest) throws Exception {
        adminService.postBook(addBookRequest);
    }

    @GetMapping("/topbooks")
    public List<Book> topbooks() {

        bookService.topBooks();

        return bookService.topBooks();
    }

    
}
