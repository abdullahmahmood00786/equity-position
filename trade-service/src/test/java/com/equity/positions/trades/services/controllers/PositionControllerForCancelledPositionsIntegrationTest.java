
package com.equity.positions.trades.services.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.equity.positions.trades.entity.Position;
import com.equity.positions.trades.entity.Transaction;
import com.equity.positions.trades.models.constants.Action;
import com.equity.positions.trades.models.constants.TradeType;
import com.equity.positions.trades.repository.TransactionRepository;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class PositionControllerForCancelledPositionsIntegrationTest {

	static {
		System.setProperty("spring.profiles.active", "test2");
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
	void shouldCancelTransactionsAndReturnPositions() {
		// Given
		var transaction1 = new Transaction(1L, 1, "REL", 50, Action.INSERT, TradeType.BUY);
		var transaction2 = new Transaction(1L, 2, "REL", 40, Action.UPDATE, TradeType.BUY);
		var transaction3 = new Transaction(1L, 3, "INF", 70, Action.CANCEL, TradeType.SELL);
		

		// When
		restTemplate.postForEntity("/api/transactions", transaction1, Void.class);
		restTemplate.postForEntity("/api/transactions", transaction2, Void.class);
		restTemplate.postForEntity("/api/transactions", transaction3, Void.class);
		
		var response = restTemplate.getForEntity("/api/transactions/positions", Position[].class);

		// Then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).containsExactlyInAnyOrder(new Position("REL", 0));
	}
}
