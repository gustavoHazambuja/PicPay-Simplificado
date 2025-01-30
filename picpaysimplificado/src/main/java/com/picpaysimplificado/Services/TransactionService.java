package com.picpaysimplificado.Services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.picpaysimplificado.DTO.TransactionDTO;
import com.picpaysimplificado.Entities.User;
import com.picpaysimplificado.Entities.Transaction.Transaction;
import com.picpaysimplificado.Exceptions.TransactionException;
import com.picpaysimplificado.Repositories.TransactionRepository;

@Service
public class TransactionService {
    
    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    RestTemplate restTemplate;

    public void createTransaction(TransactionDTO transaction){
        User sender = userService.findById(transaction.senderId());
        User receiver= userService.findById(transaction.receiverId());

        userService.validateTransaction(sender, transaction.value());

        boolean isAuthorized = this.authorizeTransaction(sender, transaction.value());
        if(!isAuthorized){
            throw new TransactionException("Transação não autorizada");
        }

        Transaction t = new Transaction();
        t.setAmount(transaction.value());
        t.setSender(sender);
        t.setReceiver(receiver);
        t.setTimesTamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        this.transactionRepository.save(t);
        this.userService.addUser(sender);
        this.userService.addUser(receiver);

    }

    private boolean authorizeTransaction(User sender, BigDecimal value){
        ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);

        if(authorizationResponse.getStatusCode() == HttpStatus.OK){
            String message =  (String) authorizationResponse.getBody().get("message");
            return "Autorizado".equalsIgnoreCase(message);
        }
        else{
            return false;
        }
    }

}
