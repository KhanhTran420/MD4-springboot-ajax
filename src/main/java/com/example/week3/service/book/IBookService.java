package com.example.week3.service.book;

import com.example.week3.model.Book;
import com.example.week3.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBookService extends IGeneralService<Book> {
    Page<Book> findAll(Pageable pageable);
    Page<Book> findAllByFirstNameContaining(String firstName, Pageable pageable);
}
