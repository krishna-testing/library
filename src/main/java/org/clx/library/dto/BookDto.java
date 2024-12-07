package org.clx.library.dto;

import lombok.Data;
import org.clx.library.model.Author;
import org.clx.library.model.Card;
import org.clx.library.model.Genre;
import org.clx.library.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class BookDto {

        private int id;
        private String name;
        private Genre genre;
        private LocalDateTime createdAt;
        Author author;
        Card card;
        private boolean available;

        private List<Transaction> transactions;


        public BookDto(Object o, String bookTitle, String authorName, String genre) {
        }
}
