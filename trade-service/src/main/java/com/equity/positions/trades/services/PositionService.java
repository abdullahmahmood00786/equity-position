package com.equity.positions.trades.services;

import java.util.List;

import com.equity.positions.trades.entity.Position;
import com.equity.positions.trades.entity.Transaction;
import com.equity.positions.trades.models.TransactionDTO;

public interface PositionService {

	void processTransactions(List<TransactionDTO> dtos);

	void processTransaction(TransactionDTO dto);

	List<Position> getCurrentPositions();

	List<Transaction> getTransactions();

}
