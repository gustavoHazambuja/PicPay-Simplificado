package com.picpaysimplificado.Services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.picpaysimplificado.DTO.UserDTO;
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

    public User findByDocument(String document){
        return userRepository.findUserByDocument(document).orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
    }

    public User createUser(User user){
        return userRepository.save(user);
    }

    public User addUser(UserDTO data){
       User newUser = new User(data);
       this.createUser(newUser);
       return newUser;
    }

    public Page<User> getAllUsers(Pageable pageable){
        return userRepository.findAll(pageable);
    }

}
