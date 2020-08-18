package com.insurance.solutions.app.services;

import com.insurance.solutions.app.exceptions.ResourceNotFoundException;
import com.insurance.solutions.app.models.Book;
import com.insurance.solutions.app.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public ResponseEntity<?> deleteBook(Long bookId) {
        bookRepository.deleteById(bookId);
        return ResponseEntity.ok().build();
    }

    public Book updateBook(Long bookId, Book book) {
        Book oldBook = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found."));
        Book newBook = new Book(book.getName(), book.getDate(), book.getPublisher(), book.getAuthor(), book.getPagesQuantity());
        newBook.setId(oldBook.getId());
        return bookRepository.save(newBook);
    }

    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found."));
    }

    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        bookRepository.findAll().forEach(books::add);
        return books;
    }

}
