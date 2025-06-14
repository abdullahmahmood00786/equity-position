
package com.equity.positions.trades.services.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import com.equity.positions.trades.entity.Position;
import com.equity.positions.trades.entity.Transaction;
import com.equity.positions.trades.models.constants.Action;
import com.equity.positions.trades.models.constants.TradeType;
import com.equity.positions.trades.repository.TransactionRepository;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PositionControllerIntegrationTest {

	static {
		System.setProperty("spring.profiles.active", "test");
	}
	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private TransactionRepository transactionRepository;

	@BeforeEach
	void setUp() {
		transactionRepository.deleteAll();
	}

	@Test
	void shouldProcessTransactionsAndReturnPositions() {
		// Given
		var transaction1 = new Transaction(1L, 1, "REL", 50, Action.INSERT, TradeType.BUY);
		var transaction2 = new Transaction(2L, 1, "ITC", 40, Action.INSERT, TradeType.SELL);
		var transaction3 = new Transaction(3L, 1, "INF", 70, Action.INSERT, TradeType.BUY);
		var transaction4 = new Transaction(1L, 2, "REL", 60, Action.UPDATE, TradeType.BUY);
		var transaction5 = new Transaction(2L, 2, "ITC", 30, Action.CANCEL, TradeType.BUY);
		var transaction6 = new Transaction(4L, 1, "INF", 20, Action.INSERT, TradeType.SELL);
		var transaction11 = new Transaction(101L, 1, "VB", 10, Action.INSERT, TradeType.BUY);
		var transaction12 = new Transaction(101L, 2, "VB", 20, Action.UPDATE, TradeType.BUY);
		var transaction13 = new Transaction(101L, 3, "VB", 30, Action.CANCEL, TradeType.SELL);
		var transaction14 = new Transaction(102L, 1, "VB", 50, Action.INSERT, TradeType.BUY);
		var transaction15 = new Transaction(102L, 2, "VB", 50, Action.UPDATE, TradeType.BUY);
		var transaction16 = new Transaction(102L, 3, "VB", 50, Action.CANCEL, TradeType.BUY);

		// When
		restTemplate.postForEntity("/api/transactions", transaction1, Void.class);
		restTemplate.postForEntity("/api/transactions", transaction2, Void.class);
		restTemplate.postForEntity("/api/transactions", transaction3, Void.class);
		restTemplate.postForEntity("/api/transactions", transaction4, Void.class);
		restTemplate.postForEntity("/api/transactions", transaction5, Void.class);
		restTemplate.postForEntity("/api/transactions", transaction6, Void.class);
		restTemplate.postForEntity("/api/transactions", transaction11, Void.class);
		restTemplate.postForEntity("/api/transactions", transaction12, Void.class);
		restTemplate.postForEntity("/api/transactions", transaction13, Void.class);
		restTemplate.postForEntity("/api/transactions", transaction14, Void.class);
		restTemplate.postForEntity("/api/transactions", transaction15, Void.class);
		restTemplate.postForEntity("/api/transactions", transaction16, Void.class);
		

		var response = restTemplate.getForEntity("/api/transactions/positions", Position[].class);

		// Then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).containsExactlyInAnyOrder(new Position("REL", 60), new Position("ITC", 0),
				new Position("INF", 50),new Position("VB", 0));
	}
}
