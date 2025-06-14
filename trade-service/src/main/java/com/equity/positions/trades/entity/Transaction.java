package com.equity.positions.trades.entity;

import jakarta.persistence.*;

import com.equity.positions.trades.models.constants.Action;
import com.equity.positions.trades.models.constants.TradeType;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Schema(description = "Represents an equity transaction")
public class Transaction {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the transaction", example = "1")
    private Long transactionId;
    
    @Column(nullable = false)
    @Schema(description = "Trade identifier", example = "12345")
    private Long tradeId;
    
    @Column(nullable = false)
    @Schema(description = "Version number of the trade", example = "1")
    private Integer version;
    
    @Column(nullable = false, length = 10)
    @Schema(description = "Security code", example = "REL")
    private String securityCode;
    
    @Column(nullable = false)
    @Schema(description = "Quantity of shares", example = "100")
    private Integer quantity;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Action type", example = "INSERT")
    private Action action;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Trade type", example = "BUY")
    private TradeType tradeType;
    
    // Builder pattern for immutability
    public Transaction(Long tradeId, Integer version, String securityCode, 
                     Integer quantity, Action action, TradeType tradeType) {
        this.tradeId = tradeId;
        this.version = version;
        this.securityCode = securityCode;
        this.quantity = quantity;
        this.action = action;
        this.tradeType = tradeType;
    }
    
    public Transaction() {
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Long getTradeId() {
		return tradeId;
	}

	public void setTradeId(Long tradeId) {
		this.tradeId = tradeId;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public TradeType getTradeType() {
		return tradeType;
	}

	public void setTradeType(TradeType tradeType) {
		this.tradeType = tradeType;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Transaction [transactionId=").append(transactionId).append(", tradeId=").append(tradeId)
				.append(", version=").append(version).append(", securityCode=").append(securityCode)
				.append(", quantity=").append(quantity).append(", action=").append(action).append(", tradeType=")
				.append(tradeType).append("]");
		return builder.toString();
	}
    
	
    
}