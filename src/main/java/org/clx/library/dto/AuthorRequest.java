package org.clx.library.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.clx.library.model.Author;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorRequest {
    @NotEmpty(message = "name should not be empty")
    @NotBlank
    private String name;
    @NotEmpty(message = "name should not be empty")
    @Column(unique = true)
    @Email
    private String email;
    @NotBlank
    @NotEmpty(message = "country shold not be empty")
    private String country;
    private int age;


    public Author authorRequestToAuthor() {
        Author author = new Author();
        author.setName(name);
        author.setEmail(email);
        author.setAge(age);
        author.setCountry(country);
        return author;
    }

    public AuthorRequest authorToAuthorRequest(Author author) {
        return AuthorRequest.builder().name(author.getName()).age(author.getAge()).country(author.getCountry()).email(author.getEmail()).build();
    }
}
