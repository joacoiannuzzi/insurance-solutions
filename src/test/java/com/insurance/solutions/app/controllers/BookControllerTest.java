package com.insurance.solutions.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.solutions.app.exceptions.ResourceNotFoundException;
import com.insurance.solutions.app.models.Book;
import com.insurance.solutions.app.services.BookService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BookControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookService bookService;

    // Date => 2020-02-20 20:20:20 (UTC-3)
    private final Date date = new Date(1582240820000L);

    private String toJson(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

    @Test
    public void createBook() throws Exception {
        Book newBook = new Book("The Unit Test", date, "Publisher", "Me", 20);

        /*
        Compruebo que el status sea 201 (Created)
        */
        MvcResult response = mockMvc
                .perform(
                        post("/books/create")
                                .contentType(APPLICATION_JSON)
                                .content(toJson(newBook))
                )
                .andExpect(status().isCreated())
                .andReturn();

        Long id = objectMapper.readValue(response.getResponse().getContentAsString(), Book.class).getId();
        newBook.setId(id);

        /*
        Compruebo que el book exista en la db y que sea el mismo
        */
        Assert.assertEquals(toJson(newBook), toJson(bookService.getBookById(id)));
        System.out.println("HERE!!!! " + toJson(bookService.getBooks()));
    }

    @Test
    public void getBook() throws  Exception {
        Book newBook = bookService.createBook(new Book("The Unit Test", date, "Publisher", "Me", 20));

        /*
        Compruebo que el status sea 200 (Ok)
        */
        MvcResult response = mockMvc
                .perform(get("/books/get/" + newBook.getId()))
                .andExpect(status().isOk())
                .andReturn();

        /*
        Compruebo que el book sea el mismo que se creo en la db
        */
        Assert.assertEquals(toJson(newBook), response.getResponse().getContentAsString());

        long mockID = 100L;

        /*
        Compruebo que no exista un libro con ese mockID en la db
        */
        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(mockID));
        Assert.assertEquals("Book not found.", exception.getMessage());

        /*
        Compruebo que el status sea 404 (Not found)
        */
        mockMvc
                .perform(get("/books/get/" + mockID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getBooks() throws  Exception {
        List<Book> bookList = bookService.getBooks();

        /*
        Compruebo que el status sea 200 (Ok) y que concuerden
        los libros que hay en la db y en la response
        */
        mockMvc
                .perform(get("/books/get-all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(toJson(bookList)));

        bookService.createBook(new Book("The Unit Test", date, "Publisher", "Me", 20));
        bookList = bookService.getBooks();

        /*
        Compruebo que el status sea 200 (Ok) y que concuerden
        los libros que hay en la db y en la response
        */
        mockMvc
                .perform(get("/books/get-all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(toJson(bookList)));
    }

    @Test
    public void updateBooks() throws Exception {
        Book aBook = new Book("The Unit Test", date, "Publisher", "Me", 20);
        Book bookUpdated = new Book("The Unit Test Updated", date, "Austral Editorial", "Me 2.0", 40);
        Long id = bookService.createBook(aBook).getId();

        /*
        Compruebo que los datos book concuerden antes de hacer los cambios
        */
        Assert.assertEquals(toJson(aBook), toJson(bookService.getBookById(id)));

        /*
        Compruebo que el status sea 200 (Ok)
        */
        mockMvc
                .perform(
                        put("/books/update/" + id)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(bookUpdated))
                )
                .andExpect(status().isOk());

        /*
        Compruebo que los datos book concuerden
        */
        bookUpdated.setId(id);
        Assert.assertEquals(toJson(bookUpdated), toJson(bookService.getBookById(id)));

        long mockID = 100L;

        /*
        Compruebo que no exista un libro con ese mockID en la db
        */
        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> bookService.updateBook(mockID, bookUpdated));
        Assert.assertEquals("Book not found.", exception.getMessage());

        /*
        Compruebo que el status sea 404 (Not found)
        */
        mockMvc
                .perform(
                        put("/books/update/" + mockID)
                                .contentType(APPLICATION_JSON)
                                .content(toJson(bookUpdated))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteBook() throws Exception {
        Book aBook = new Book("The Unit Test", date, "Publisher", "Me", 20);
        Long id = bookService.createBook(aBook).getId();

        /*
        Compruebo que exista el book
        */
        Assert.assertEquals(toJson(aBook), toJson(bookService.getBookById(id)));

        /*
        Compruebo que el status sea 200 (Ok)
        */
        mockMvc
                .perform(delete("/books/delete/" + id))
                .andExpect(status().isOk());

        /*
        Compruebo que no exista el libro con ese ID en la db
        */
        Exception exception = Assert.assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(id));
        Assert.assertEquals("Book not found.", exception.getMessage());

        /*
        Compruebo que el status sea 404 (Not found)
        */
        mockMvc
                .perform(delete("/books/delete/" + id))
                .andExpect(status().isNotFound());
    }
}
