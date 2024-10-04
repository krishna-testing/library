//package org.clx.library.book.service.ServieImpl;
//
//import lombok.RequiredArgsConstructor;
//import org.clx.library.book.entities.BorrowingRecord;
//import org.clx.library.book.repositories.BorrowingRecordRepository;
//import org.clx.library.book.service.BorrowingRecordService;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class BorrowingRecordServiceImpl implements BorrowingRecordService {
//
//    private final BorrowingRecordRepository borrowingRecordRepository;
//
//    @Override
//    public BorrowingRecord saveBorrowingRecord(BorrowingRecord record) {
//        return borrowingRecordRepository.save(record);
//    }
//
//    @Override
//    public BorrowingRecord updateBorrowingRecord(BorrowingRecord record) {
//        return borrowingRecordRepository.save(record);
//    }
//
//    @Override
//    public void deleteBorrowingRecord(Integer id) {
//        borrowingRecordRepository.deleteById(id);
//    }
//
//    @Override
//    public BorrowingRecord getBorrowingRecordById(Integer id) {
//        return borrowingRecordRepository.findById(id).orElse(null);
//    }
//
//    @Override
//    public List<BorrowingRecord> getAllBorrowingRecords() {
//        return borrowingRecordRepository.findAll();
//    }
//}
