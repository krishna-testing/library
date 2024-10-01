package org.clx.library.book.repo;

import org.clx.library.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    List<Book> findByTitleContaining(String title);
    List<Book> findByAuthorContaining(String author);
}
