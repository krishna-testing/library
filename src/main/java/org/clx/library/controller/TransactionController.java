package org.clx.library.controller;

import org.clx.library.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class TransactionController {

    @Autowired
    TransactionService transactionService;


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
                                     @RequestParam("bookId") int bookId) throws Exception {
        try{
            String transaction_id=transactionService.returnBooks(cardId,bookId);
            return new ResponseEntity<>(
                    "Your Transaction was Successful here is your Txn id:"+transaction_id,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}
