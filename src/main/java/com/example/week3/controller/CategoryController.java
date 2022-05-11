package com.example.week3.controller;


import com.example.week3.model.Book;
import com.example.week3.model.Category;
import com.example.week3.service.book.IBookService;
import com.example.week3.service.category.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/category")
@CrossOrigin("*")
public class CategoryController {

    @Autowired
    private IBookService bookService;

    @Autowired
    private ICategoryService categoryService;

    @GetMapping
    public ResponseEntity<Iterable<Category>> allCategory(){
        return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Category> saveCate(@RequestBody Category category){
        categoryService.save(category);
        return new ResponseEntity<>(category,HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Category> deleteCate(@PathVariable Long id){
        Optional<Category> categoryOptional = categoryService.findById(id);
        if (!categoryOptional.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        categoryService.remove(id);
        return new ResponseEntity<>(categoryOptional.get(), HttpStatus.NO_CONTENT);
    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<Category> editCate(@PathVariable Long id, @RequestBody Category category){
        Optional<Category> categoryOptional = categoryService.findById(id);
        if (!categoryOptional.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        category.setId(id);
        categoryService.save(category);
        return new ResponseEntity<>(category, HttpStatus.ACCEPTED);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Book> findId(@PathVariable Long id) {
//        categoryService.findById(id).get();
//        return new ResponseEntity<>( HttpStatus.OK);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> findById(@PathVariable Long id){
        Category category = categoryService.findById(id).get();
        return new ResponseEntity<>(category,HttpStatus.OK);
    }
}