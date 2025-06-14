package com.equity.positions.trades.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.equity.positions.trades.entity.Position;
import com.equity.positions.trades.entity.Transaction;
import com.equity.positions.trades.models.TransactionDTO;
import com.equity.positions.trades.models.constants.Action;
import com.equity.positions.trades.models.constants.TradeType;
import com.equity.positions.trades.repository.TransactionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PositionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private PositionServiceImpl positionService;

    @Test
    void testProcessTransactionInsert() {
        // Arrange
        TransactionDTO dto = new TransactionDTO(1l, 1l,1,"securityCode",100, Action.INSERT, TradeType.BUY);
        when(transactionRepository.findByTradeIdOrderByVersionAsc(dto.tradeId())).thenReturn(new ArrayList<>());

        // Act
        positionService.processTransaction(dto);

        // Assert
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testProcessTransactionUpdate() {
        // Arrange
        TransactionDTO dto = new TransactionDTO(2l,2l, 2, "securityCode", 15, Action.UPDATE, TradeType.BUY);
        Transaction existingTransaction = new Transaction(2l, 1, "securityCode", 10, Action.INSERT, TradeType.BUY);
        when(transactionRepository.findByTradeIdOrderByVersionAsc(dto.tradeId())).thenReturn(List.of(existingTransaction));

        // Act
        positionService.processTransaction(dto);

        // Assert
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testProcessTransactionCancel() {
        // Arrange
        TransactionDTO dto = new TransactionDTO(2l,2l, 2, "securityCode", 10, Action.CANCEL, TradeType.BUY);
        Transaction existingTransaction = new Transaction(2l, 1, "securityCode", 10, Action.INSERT, TradeType.BUY);
        when(transactionRepository.findByTradeIdOrderByVersionAsc(dto.tradeId())).thenReturn(List.of(existingTransaction));

        // Act
        positionService.processTransaction(dto);

        // Assert
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testGetCurrentPositions() {
        // Act
        List<Position> positions = positionService.getCurrentPositions();

        // Assert
        assertNotNull(positions);
    }

    @Test
    void testGetTransactions() {
        // Arrange
        when(transactionRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Transaction> transactions = positionService.getTransactions();

        // Assert
        assertNotNull(transactions);
    }

    @Test
    void testValidateTransactionQuantity() {
        // Arrange
        Transaction transaction = new Transaction(1l, 1, "securityCode", -10, Action.INSERT, TradeType.BUY);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> positionService.validateTransaction(transaction));
    }
    
    @Test
    void testValidateTransactionQuantityZero() {
        // Arrange
        Transaction transaction = new Transaction(1l, 1, "securityCode", 0, Action.INSERT, TradeType.BUY);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> positionService.validateTransaction(transaction));
    }

    @Test
    void testValidateTransactionVersion() {
        // Arrange
        Transaction transaction = new Transaction(2l, 2, "securityCode", 10, Action.INSERT, TradeType.BUY);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> positionService.validateTransaction(transaction));
    }
}