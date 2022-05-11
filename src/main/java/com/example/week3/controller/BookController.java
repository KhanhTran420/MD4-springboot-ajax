package com.example.week3.controller;


import com.example.week3.model.Book;
import com.example.week3.model.BookForm;
import com.example.week3.model.Category;
import com.example.week3.service.book.IBookService;
import com.example.week3.service.category.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private IBookService bookService;
    @Autowired
    private ICategoryService categoryService;

    @ModelAttribute("categories")
    private Iterable<Category> categories() {
        return categoryService.findAll();
    }

    @Autowired
    Environment env;

    @ModelAttribute("categories")
    public Iterable<Category> listAllCategory() {
        return categoryService.findAll();
    }

    @GetMapping("/cate")
    public ResponseEntity<Iterable<Category>> showCate() {
        return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
    }

    //    @GetMapping
//    public ModelAndView showBooks(){
//        ModelAndView modelAndView = new ModelAndView("/list");
//        modelAndView.addObject("books",bookService.findAll());
//        return  modelAndView;
//    }
//    @GetMapping
//    public ResponseEntity<Iterable<Book>> listBooks() {
////        List<Book> bookList = (List<Book>) bookService.findAll();
//        return new ResponseEntity<>(bookService.findAll(), HttpStatus.OK);
//    }

    @GetMapping
    public ResponseEntity<Page<Book>> listBooks(@RequestParam(name="q") Optional<String> q, @PageableDefault(value = 3) Pageable pageable  ) {
//        List<Book> bookList = (List<Book>) bookService.findAll();
        Page<Book> books;
        if (!q.isPresent()) {
            books = bookService.findAll(pageable);
        } else {
            books = bookService.findAllByNameContaining(q.get(), pageable);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@ModelAttribute BookForm bookForm) {
        MultipartFile multipartFile = bookForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        String fileUpload = env.getProperty("upload.path");
        try {
            FileCopyUtils.copy(multipartFile.getBytes(), new File(fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Book book = new Book(bookForm.getName(), bookForm.getPrice(), bookForm.getAuthor(), fileName, bookForm.getCategory());

        bookService.save(book);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable Long id) {
        Optional<Book> bookOptional = bookService.findById(id);
        if (!bookOptional.isPresent()) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            bookService.remove(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Book> findBook(@PathVariable Long id) {
//
//        Optional<Book> bookOptional = bookService.findById(id);
//
//        return new ResponseEntity<>(bookOptional.get(), HttpStatus.OK);
//    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @ModelAttribute BookForm bookForm) {
        Optional<Book> bookOptional = bookService.findById(id);
        bookForm.setId(bookOptional.get().getId());
        MultipartFile multipartFile = bookForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        String fileUpload = env.getProperty("upload.path");
        Book existBook = new Book(id, bookForm.getName(), bookForm.getPrice(), bookForm.getAuthor(), fileName, bookForm.getCategory());
        try {
            FileCopyUtils.copy(multipartFile.getBytes(), new File(fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        bookService.save(existBook);
        return new ResponseEntity<>(existBook, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> findOne(@PathVariable Long id) {
        return new ResponseEntity<>(bookService.findById(id).get(), HttpStatus.OK);
    }

//    @GetMapping("/category/{id}")
//    public ResponseEntity<Page<Book>> findByCategory(@PathVariable Long id, @PageableDefault(value = 4) Pageable pageable) {
//        Page<Book> books = bookService.findByCategory(id, pageable);
//        return new ResponseEntity<>(books, HttpStatus.OK);
//    }
}