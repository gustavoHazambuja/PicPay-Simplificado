package com.picpaysimplificado.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.picpaysimplificado.Entities.Transaction.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    
}
