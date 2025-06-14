package com.equity.positions.trades.models;

import com.equity.positions.trades.models.constants.Action;
import com.equity.positions.trades.models.constants.TradeType;

public record TransactionDTO(
	    Long transactionId,
	    Long tradeId,
	    Integer version,
	    String securityCode,
	    Integer quantity,
	    Action action,
	    TradeType tradeType
	) {}
