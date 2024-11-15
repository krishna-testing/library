package org.clx.library.controller;

import lombok.RequiredArgsConstructor;
import org.clx.library.exception.BookNotFoundException;
import org.clx.library.exception.CardNotFoundException;
import org.clx.library.services.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;


    //what i need ideally is card_id and book_id

    // Issue Book Endpoint
    @PostMapping("/issueBook")
    public ResponseEntity<String> issueBook(@RequestParam(value = "cardId") int cardId,
                                            @RequestParam("bookId") int bookId) {
        try {
            String transactionId = transactionService.issueBooks(cardId, bookId);
            return new ResponseEntity<>("Your Transaction was successful. Here is your Txn ID: " + transactionId, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/returnBook")
    public ResponseEntity<String> returnBook(@RequestParam("cardId") int cardId,
                                     @RequestParam("bookId") int bookId) throws BookNotFoundException, CardNotFoundException {
        try{
            String transactionId=transactionService.returnBooks(cardId,bookId);
            return new ResponseEntity<>(
                    "Your Transaction was Successful here is your Txn id:"+transactionId,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}
