package org.clx.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Genre genre;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Author author;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Card card;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean available;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Transaction> transactions;

    public boolean getAvailable() {
        return available;
    }

}