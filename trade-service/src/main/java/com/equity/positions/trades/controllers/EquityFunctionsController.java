package com.equity.positions.trades.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.equity.positions.trades.entity.Position;
import com.equity.positions.trades.entity.Transaction;
import com.equity.positions.trades.models.TransactionDTO;
import com.equity.positions.trades.services.PositionService;
import com.equity.positions.trades.services.PositionServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Transaction API", description = "Operations for managing equity transactions")
public class EquityFunctionsController {
	@Autowired
	private PositionService positionService;

	@Operation(summary = "Create a new transaction", description = "Processes a new equity position transaction", responses = {
			@ApiResponse(responseCode = "200", description = "Transaction created successfully", content = @Content(schema = @Schema(implementation = TransactionDTO.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@PostMapping
	public ResponseEntity<Void> addTransaction(@RequestBody TransactionDTO dto) {
		positionService.processTransaction(dto);
		return ResponseEntity.accepted().build();
	}

	@Operation(summary = "Get current positions", description = "Returns aggregated positions for all securities")
	@ApiResponse(responseCode = "200", description = "Successfully retrieved positions", content = @Content(schema = @Schema(implementation = Position.class)))
	@GetMapping("/positions")
	public ResponseEntity<List<Position>> getPositions() {
		return ResponseEntity.ok(positionService.getCurrentPositions());
	}
	
	@Operation(summary = "Get current transactions", description = "Returns all transactions in system")
	@ApiResponse(responseCode = "200", description = "Successfully retrieved transactions", content = @Content(schema = @Schema(implementation = Transaction.class)))
	@GetMapping
	public ResponseEntity<List<Transaction>> getTransactions() {
		return ResponseEntity.ok(positionService.getTransactions());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleValidationExceptions(IllegalArgumentException ex) {
		return ResponseEntity.badRequest().body(ex.getMessage());
	}
}