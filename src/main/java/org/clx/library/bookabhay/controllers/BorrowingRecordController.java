//package org.clx.library.book.controllers;
//
//import lombok.RequiredArgsConstructor;
//import org.clx.library.book.entities.BorrowingRecord;
//import org.clx.library.book.service.BorrowingRecordService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/borrowing-records")
//public class BorrowingRecordController {
//
//    private final BorrowingRecordService borrowingRecordService;
//
//    @PostMapping
//    public ResponseEntity<BorrowingRecord> createBorrowingRecord(@RequestBody BorrowingRecord record) {
//        System.out.println(record.getBorrowingDate()+" before db");
//        BorrowingRecord createdRecord = borrowingRecordService.saveBorrowingRecord(record);
//        System.out.println(createdRecord.getBorrowingDate());
//        return new ResponseEntity<>(createdRecord, HttpStatus.CREATED);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<BorrowingRecord> getBorrowingRecordById(@PathVariable Integer id) {
//        BorrowingRecord record = borrowingRecordService.getBorrowingRecordById(id);
//        return record != null ? new ResponseEntity<>(record, HttpStatus.OK)
//                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<BorrowingRecord>> getAllBorrowingRecords() {
//        List<BorrowingRecord> records = borrowingRecordService.getAllBorrowingRecords();
//        return new ResponseEntity<>(records, HttpStatus.OK);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<BorrowingRecord> updateBorrowingRecord(@PathVariable Integer id, @RequestBody BorrowingRecord record) {
//        record.setId(id);
//        BorrowingRecord updatedRecord = borrowingRecordService.updateBorrowingRecord(record);
//        return new ResponseEntity<>(updatedRecord, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteBorrowingRecord(@PathVariable Integer id) {
//        borrowingRecordService.deleteBorrowingRecord(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//}
