//package org.clx.library.book.entities;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//import java.time.LocalDate;
//
//@Entity
//@Data
//public class BorrowingRecord {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//    @ManyToOne
//    private Book book;
//    private LocalDate borrowingDate;
//    private LocalDate returnDate;
//    private String username;
//}
