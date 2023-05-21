package com.gmail.yeatz0408.backToshokan.responsemodels;

import com.gmail.yeatz0408.backToshokan.entity.Book;

import lombok.Data;

@Data
public class ShelfCurrentLoansResponse {

    private Book book;
    private int daysLeft;

    public ShelfCurrentLoansResponse(Book book, int daysLeft) {
        this.book = book;
        this.daysLeft = daysLeft;
    }
    
}
