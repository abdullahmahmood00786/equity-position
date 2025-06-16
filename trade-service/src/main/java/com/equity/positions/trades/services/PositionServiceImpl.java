package com.equity.positions.trades.services;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.equity.positions.trades.entity.Position;
import com.equity.positions.trades.entity.Transaction;
import com.equity.positions.trades.models.TransactionDTO;
import com.equity.positions.trades.models.constants.Action;
import com.equity.positions.trades.models.constants.TradeType;
import com.equity.positions.trades.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
public class PositionServiceImpl implements PositionService {
	
	public static final Logger log = LoggerFactory.getLogger(PositionServiceImpl.class);


	@Autowired
	private TransactionRepository transactionRepository;

	private final Map<String, Integer> positions = new ConcurrentHashMap<>();
	private final Lock lock = new ReentrantLock();

	@Transactional
	@Override
	public void processTransaction(TransactionDTO dto) {
		lock.lock();
		try {

			Transaction transaction = new Transaction(dto.tradeId(), dto.version() == null ? 1 : dto.version(),
					dto.securityCode(), dto.quantity(), dto.action(), dto.tradeType());

			validateTransaction(transaction);

			performAction(transaction);
			log.info("Transaction is processed.");
		} finally {
			lock.unlock();
		}
	}

	private void performAction(Transaction transaction) {
		var existingTransactions = transactionRepository.findByTradeIdOrderByVersionAsc(transaction.getTradeId());
		switch (transaction.getAction()) {
		case INSERT -> {
			

			if (existingTransactions.isEmpty()) {
				processInsert(transaction);
			} else {
				/*
				 * boolean isCanceled =
				 * existingTransactions.stream().anyMatch(tx->tx.getAction().equals(Action.
				 * CANCEL)); if(isCanceled) { throw new
				 * IllegalStateException("Trade is already cancelled, No Change is Allowed."); }
				 * processUpdate(transaction);
				 */
				throw new IllegalStateException("Trade transactions are already present, Can not be created.");
			}

		}
		case UPDATE -> {

			boolean isCanceled = existingTransactions.stream().anyMatch(tx -> tx.getAction().equals(Action.CANCEL));
			if (isCanceled) {
				throw new IllegalStateException("Trade is already cancelled, No Change is Allowed.");
			}
			processUpdate(transaction, existingTransactions);
		}
		case CANCEL -> {

			boolean isCanceled = existingTransactions.stream().anyMatch(tx -> tx.getAction().equals(Action.CANCEL));
			if (isCanceled) {
				throw new IllegalStateException("Trade is already cancelled, No Change is Allowed.");
			}
			processCancel(transaction, existingTransactions);
		}
		}
		log.info("Transaction with TRADE ID : {} , ACTION :{}, OF TYPE {} is processed.",transaction.getTradeId(),transaction.getAction(),transaction.getTradeType());

	}

	@Transactional
	@Override
	public void processTransactions(List<TransactionDTO> dtos) {
		lock.lock();
		try {
			dtos.stream().map(dto -> new Transaction(dto.tradeId(), dto.version() == null ? 1 : dto.version(),
					dto.securityCode(), dto.quantity(), dto.action(), dto.tradeType())).forEach(transaction -> {
						validateTransaction(transaction);
						performAction(transaction);
					});

		} finally {
			lock.unlock();
		}
	}

	@Override
	public List<Position> getCurrentPositions() {
		return positions.entrySet().stream().map(entry -> new Position(entry.getKey(), entry.getValue())).toList();
	}

	@Override
	public List<Transaction> getTransactions() {
		return transactionRepository.findAll();
	}

	void validateTransaction(Transaction transaction) {
		if (transaction.getQuantity() <= 0) {
			throw new IllegalArgumentException("Quantity must be positive");
		}

		if (transaction.getAction() == Action.INSERT && transaction.getVersion() != 1) {
			throw new IllegalArgumentException("INSERT must be version 1");
		}

		if (transactionRepository.existsByTransactionId(transaction.getTransactionId())) {
			throw new IllegalArgumentException("Duplicate transaction ID");
		}
	}

	private void processInsert(Transaction transaction) {
		int delta = transaction.getTradeType() == TradeType.BUY ? transaction.getQuantity()
				: -transaction.getQuantity();
		updatePosition(transaction.getSecurityCode(), delta);
		transactionRepository.save(transaction);
	}

	private void processUpdate(Transaction transaction, List<Transaction> existingTransactions) {

		if (existingTransactions.isEmpty()) {
			throw new IllegalStateException("No existing transaction to update");
		}
		var transactionWithMaxVersion = existingTransactions.stream().sorted(Comparator.comparing(Transaction::getVersion).reversed()).findFirst().orElse(null);
		 // reverse later transaction
		int reverseDelta = transactionWithMaxVersion.getTradeType() == TradeType.BUY ? -transactionWithMaxVersion.getQuantity() : transactionWithMaxVersion.getQuantity();
		updatePosition(transactionWithMaxVersion.getSecurityCode(), reverseDelta);

		// Apply new version
		int newDelta = transaction.getTradeType() == TradeType.BUY ? transaction.getQuantity()
				: -transaction.getQuantity();
		updatePosition(transaction.getSecurityCode(), newDelta);
		transaction.setVersion(transactionWithMaxVersion.getVersion() + 1);
		transactionRepository.save(transaction);
	}

	private void processCancel(Transaction transaction, List<Transaction> existingTransactions) {
		

		if (existingTransactions.isEmpty()) {
			throw new IllegalStateException("No existing transaction to cancel");
		}
		var transactionToCancel = existingTransactions.stream().sorted(Comparator.comparing(Transaction::getVersion).reversed()).findFirst().orElse(null);

		positions.put(transactionToCancel.getSecurityCode(), 0);
		transaction.setVersion(transactionToCancel.getVersion()+1);
		transactionRepository.save(transaction);
		
		var allTransactions = transactionRepository.findBySecurityCode(transaction.getSecurityCode());
		
		var tradeWiseTx = allTransactions.stream().collect(Collectors.groupingBy(Transaction::getTradeId));

		var transactionsHavingActiveTrades = tradeWiseTx.entrySet().stream()
			    .filter(entry -> entry.getValue().stream()
			        .noneMatch(t -> t.getAction().equals(Action.CANCEL))).collect(Collectors.toMap(Map.Entry::getKey, entry->entry.getValue().stream().max(Comparator.comparingLong(Transaction::getVersion)).orElse(null)));
		
		transactionsHavingActiveTrades.values().stream().filter(t->t!=null).forEach(t -> {
			int delta = t.getTradeType() == TradeType.BUY ? t.getQuantity() : -t.getQuantity();
			updatePosition(t.getSecurityCode(), delta);
		});
		
		
	}

	private void updatePosition(String securityCode, int delta) {
		positions.merge(securityCode, delta, Integer::sum);
	}
}