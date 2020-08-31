package com.insurance.solutions.app.repositories;

import com.insurance.solutions.app.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
}
