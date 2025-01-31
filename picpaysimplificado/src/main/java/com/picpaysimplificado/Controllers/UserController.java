package com.picpaysimplificado.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.picpaysimplificado.DTO.UserDTO;
import com.picpaysimplificado.Entities.User;
import com.picpaysimplificado.Services.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDTO user){
        User newUser = userService.addUser(user);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(Pageable pageable){
        Page<User> result = userService.getAllUsers(pageable);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Long id){
        User result = userService.findById(id);

        return new ResponseEntity<>(result, HttpStatus.FOUND);
    }

    @GetMapping(value = "/search-document")
    public ResponseEntity<User> searchByDocument(@RequestParam(defaultValue = "")String document){
        User result = userService.findByDocument(document);

        return new ResponseEntity<>(result,HttpStatus.FOUND);
    }
    
}
