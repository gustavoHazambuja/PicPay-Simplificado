package com.picpaysimplificado.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.picpaysimplificado.DTO.TransactionDTO;
import com.picpaysimplificado.Entities.Transaction.Transaction;
import com.picpaysimplificado.Services.TransactionService;

@RestController
@RequestMapping(value = "/transactions")
public class TransactionController {
    

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createdTransactions(@RequestBody TransactionDTO dto){
       Transaction newTransaction = transactionService.createTransaction(dto);

       return new ResponseEntity<Transaction>(newTransaction, HttpStatus.CREATED);
        
    }

    @GetMapping
    public ResponseEntity<Page<Transaction>> getAlltransactions(Pageable pageable){
        Page<Transaction> result = transactionService.getAllTransactions(pageable);

        return new ResponseEntity<>(result,HttpStatus.OK);


    } 

}
