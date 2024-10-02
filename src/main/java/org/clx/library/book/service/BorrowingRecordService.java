package org.clx.library.book.service;

import org.clx.library.book.entities.BorrowingRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BorrowingRecordService {
    BorrowingRecord saveBorrowingRecord(BorrowingRecord record);
    BorrowingRecord updateBorrowingRecord(BorrowingRecord record);
    void deleteBorrowingRecord(Long id);
    BorrowingRecord getBorrowingRecordById(Long id);
    List<BorrowingRecord> getAllBorrowingRecords();
}
