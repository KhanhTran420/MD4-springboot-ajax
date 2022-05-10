package com.example.week3.repository;

import com.example.week3.model.Category;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ICategoryRepository extends PagingAndSortingRepository<Category,Long> {
}
