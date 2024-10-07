package org.clx.library.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.clx.library.bookabhay.entities.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// Use plural for table naming convention
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users") // Use plural for table naming convention
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private Long contactNumber;
    private String collegeName;
    private String email;
    private String gender;
    private String password;
    @ManyToMany
    @JsonIgnore
    private List<Book> savedPost = new ArrayList<>();
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
}