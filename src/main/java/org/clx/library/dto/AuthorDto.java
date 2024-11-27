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
@Builder
public class AuthorDto {
    private int id;
    private String name;
    private String email;
    private int age;
    private String country;
    private List<BookDto> booksWritten;

    // Map Author entity to AuthorDto
    public AuthorDto mapToDto(Author author) {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setName(author.getName());
        authorDto.setEmail(author.getEmail());
        authorDto.setAge(author.getAge());
        authorDto.setCountry(author.getCountry());
        List<BookDto> list = author.getBooksWritten().stream().map(BookDto::bookToBookDto).toList();
        authorDto.setBooksWritten(list);
        return authorDto;
    }

    // Map AuthorDto to Author entity
    public static Author mapToEntity(AuthorDto authorDto) {
       return Author.builder().id(authorDto.getId()).name(authorDto.getName()).email(authorDto.getEmail()).age(authorDto.getAge()).country(authorDto.getCountry()).build();
    }

}
