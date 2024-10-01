package org.clx.library.book.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is mandatory")
    @Size(min = 2, message = "Title must have at least 2 characters")
    private String title;

    @NotBlank(message = "Author is mandatory")
    private String author;
    private String genre;
    private String publisher;
    @NotBlank(message = "ISBN is mandatory")
    private String isbn;
    @NotNull(message = "Published year is mandatory")
    private Integer publisherYear;
}
