package com.picpaysimplificado.Services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
    private NotificationService notificationService;

    @Autowired
    private RestTemplate restTemplate;

    public Transaction createTransaction(TransactionDTO transaction){
        User sender = userService.findById(transaction.senderId());
        User receiver= userService.findById(transaction.receiverId());

        userService.validateTransaction(sender, transaction.value());

        boolean isAuthorized = this.authorizeTransaction(sender, transaction.value());
        if(!isAuthorized){
            throw new TransactionException("Transação não autorizada");
        }

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimesTamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        this.transactionRepository.save(newTransaction);
        this.userService.createUser(sender);
        this.userService.createUser(receiver);

        notificationService.sendNotification(sender, "Transação realizada com sucesso.");

        notificationService.sendNotification(receiver, "Transação recebida com sucesso.");

        return newTransaction;

    }

    private boolean authorizeTransaction(User sender, BigDecimal value){
        ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);

        if (authorizationResponse.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> body = authorizationResponse.getBody();
            
            if (body != null && body.containsKey("data")) {
                Map<String, Object> data = (Map<String, Object>) body.get("data");
                
                if (data != null && data.containsKey("authorization")) {
                    return (Boolean) data.get("authorization"); // Converte corretamente para Boolean
                }
            }
        }
        return false;
    }

    public List<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }

}
