package org.clx.library.dto;

import lombok.*;
import org.clx.library.model.Author;
import org.clx.library.model.Book;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {
    private int id;
    private String name;
    private String email;
    private int age;
    private String country;
    private List<BookDto> booksWritten;

    // Map Author entity to AuthorDto
    public static AuthorDto mapToDto(Author author) {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setName(author.getName());
        authorDto.setEmail(author.getEmail());
        authorDto.setAge(author.getAge());
        authorDto.setCountry(author.getCountry());
        return authorDto;
    }

    // Map AuthorDto to Author entity
    public static Author mapToEntity(AuthorDto authorDto) {
        Author author = new Author();
        author.setId(authorDto.getId());
        author.setName(authorDto.getName());
        author.setEmail(authorDto.getEmail());
        author.setAge(authorDto.getAge());
        author.setCountry(authorDto.getCountry());
        return author;
    }

}
