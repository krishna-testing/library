package org.clx.library.repositories;

import jakarta.transaction.Transactional;
import org.clx.library.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Transactional
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    @Modifying
    @Query("update Author a set a.name=:#{#new_author.name}," +
            "a.email=:#{#new_author.email}," +
            "a.age=:#{#new_author.age}," +
            "a.country=:#{#new_author.country} where a.id=:#{#new_author.id}")
    int updateAuthorDetails(@Param("new_author") Author newAuthor);
}
