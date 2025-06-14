package com.equity.positions.trades.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.equity.positions.trades.entity.Position;
import com.equity.positions.trades.entity.Transaction;
import com.equity.positions.trades.models.TransactionDTO;
import com.equity.positions.trades.models.constants.Action;
import com.equity.positions.trades.models.constants.TradeType;
import com.equity.positions.trades.services.PositionService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class EquityFunctionsControllerTest {

    @Mock
    private PositionService positionService;

    @InjectMocks
    private EquityFunctionsController controller;

    @Test
    void testAddTransaction() {
        // Arrange
        TransactionDTO dto =  new TransactionDTO(2l,2l, 2, "securityCode", 10, Action.INSERT, TradeType.BUY);

        // Act
        ResponseEntity<Void> response = controller.addTransaction(dto);

        // Assert
        assertEquals(202, response.getStatusCodeValue());
        verify(positionService, times(1)).processTransaction(dto);
    }

    @Test
    void testGetPositions() {
        // Arrange
        when(positionService.getCurrentPositions()).thenReturn(List.of());

        // Act
        ResponseEntity<List<Position>> response = controller.getPositions();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void testGetTransactions() {
        // Arrange
        when(positionService.getTransactions()).thenReturn(List.of());

        // Act
        ResponseEntity<List<Transaction>> response = controller.getTransactions();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void testHandleValidationExceptions() {
        // Arrange
        String errorMessage = "Invalid input";
        IllegalArgumentException ex = new IllegalArgumentException(errorMessage);

        // Act
        ResponseEntity<String> response = controller.handleValidationExceptions(ex);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errorMessage, response.getBody());
    }
}