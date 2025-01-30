package com.picpaysimplificado.DTO;

import java.math.BigDecimal;

import com.picpaysimplificado.Entities.UserType;

public record UserDTO(String fistName, String lastName, String document, BigDecimal balance, String email, String password, UserType userType) {
    
}
