package com.example.week3.repository;


import com.example.week3.model.Book;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IBookRepository extends PagingAndSortingRepository<Book,Long> {
}
