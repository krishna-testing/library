package org.clx.library.bookabhay.repositories;

import org.clx.library.bookabhay.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    // Custom query to find books by a specific user's ID
    @Query("SELECT b FROM Book b WHERE b.user.id = :userId")
    List<Book> findBookByUserId(Integer userId);
}