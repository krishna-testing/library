package org.clx.library.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.clx.library.model.Book;

import java.util.ArrayList;
import java.util.List;
@Data
public class AuthorDto {
    private int id;
    private String name;
    private String email;
    private int age;
    private String country;
    private List<Book> booksWritten;
    private List<Book> savedBook = new ArrayList<>();
}
