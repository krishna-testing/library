package org.clx.library.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.clx.library.model.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    private int id;
    private String name;
    private Genre genre;

    private boolean available;

    @JsonIgnore
    private List<TransactionDto> transactions;

    public Book bookDtoToBook() {
        Book book = new Book();
        book.setId(id);
        book.setName(name);
        book.setGenre(genre);
        book.setAvailable(available);
        return book;
    }

    public static BookDto bookToBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setName(book.getName());
        bookDto.setGenre(book.getGenre());
        return bookDto;
    }
}
