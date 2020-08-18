package com.insurance.solutions.app.controllers;

import com.insurance.solutions.app.models.Book;
import com.insurance.solutions.app.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@CrossOrigin
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/create")
    public Book createBook(@RequestBody Book book) {
        return bookService.createBook(book);
    }

    @DeleteMapping("/delete/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable Long bookId) {
        return bookService.deleteBook(bookId);
    }

    @PutMapping("/update/{bookId}")
    public Book updateBook(@PathVariable Long bookId, @RequestBody Book book) {
        return bookService.updateBook(bookId, book);
    }

    @GetMapping("get/{bookId}")
    public Book getBookById(@PathVariable Long bookId) {
        return bookService.getBookById(bookId);

    }

    @GetMapping("/get-all")
    public List<Book> getBooks() {
        return bookService.getBooks();
    }



}
