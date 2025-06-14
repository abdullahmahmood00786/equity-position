package com.equity.positions.trades.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.equity.positions.trades.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    boolean existsByTransactionId(Long transactionId);
    List<Transaction> findByTradeIdOrderByVersionAsc(Long tradeId);
    List<Transaction> findBySecurityCode(String securityCode);
    Optional<Transaction> findByTradeIdAndVersion(Long tradeId, Integer version);
    List<Transaction> findAllByOrderByTradeIdAscVersionAsc();

}
