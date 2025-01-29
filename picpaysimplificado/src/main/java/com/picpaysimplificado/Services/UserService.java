package com.picpaysimplificado.Services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.picpaysimplificado.Entities.User;
import com.picpaysimplificado.Entities.UserType;
import com.picpaysimplificado.Exceptions.BalanceException;
import com.picpaysimplificado.Exceptions.UserTypeException;
import com.picpaysimplificado.Repositories.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public void validateTransaction(User sender, BigDecimal amount){

        if(sender.getUserType() == UserType.MERCHANT){ // Se for logísta
            throw new UserTypeException("Usuário do tipo logísta não pode realizar a transação.");
        }

        if(sender.getBalance().compareTo(amount) < 0){
            throw new BalanceException("Saldo insuficiente.");
        }
    }

    public User findById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
    }

    public User addUser(User u){
        return userRepository.save(u);
    }
}
