package org.clx.library.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.clx.library.payload.ApiResponse;
import org.clx.library.services.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;


    //what i need ideally is card_id and book_id

    // Issue Book Endpoint
    @PostMapping("/issueBook")
    public ResponseEntity<ApiResponse> issueBook(@RequestParam(value = "cardId") int cardId,
                                                 @RequestParam("bookId") int bookId) {
        log.info("Received request to issue book with ID: {} to card ID: {}", bookId, cardId);

        try {
            String transactionId = transactionService.issueBooks(cardId, bookId);
            log.info("Book with ID: {} successfully issued to card ID: {}. Transaction ID: {}", bookId, cardId, transactionId);
            ApiResponse response = new ApiResponse(HttpStatus.OK, "Your Transaction was successful. Here is your Txn ID: " + transactionId, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to issue book with ID: {} to card ID: {}. Error: {}", bookId, cardId, e.getMessage());
            ApiResponse response = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/returnBook")
    public ResponseEntity<ApiResponse> returnBook(@RequestParam("cardId") int cardId,
                                     @RequestParam("bookId") int bookId){
        log.info("Received request to return book with ID: {} for card ID: {}", bookId, cardId);
        try{
            String transactionId=transactionService.returnBooks(cardId,bookId);
            log.info("Book with ID: {} successfully returned for card ID: {}. Transaction ID: {}", bookId, cardId, transactionId);
            ApiResponse response = new ApiResponse(HttpStatus.OK, "Your Transaction was Successful here is your Txn id:" + transactionId, null);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch (Exception e){
            log.error("Failed to return book with ID: {} for card ID: {}. Error: {}", bookId, cardId, e.getMessage());
            ApiResponse response = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}
