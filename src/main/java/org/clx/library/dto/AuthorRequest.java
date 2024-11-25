package org.clx.library.dto;

import lombok.*;
import org.clx.library.model.Author;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorRequest {
    private String name;
    private String email;
    private int age;
    private String country;



    public Author authorRequestToAuthor(){
        Author author= new Author();
        author.setName(name);
        author.setEmail(email);
        author.setAge(age);
        author.setCountry(country);
        return author;
    }
}
