package org.clx.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.clx.library.model.Book;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {
    private int id;
    private String name;
    private String email;
    private int age;
    private String country;
    private List<Book> booksWritten;
    private List<Book> savedBook = new ArrayList<>();
}
