package com.equity.positions.trades.sample.load;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.equity.positions.trades.models.TransactionDTO;
import com.equity.positions.trades.models.constants.Action;
import com.equity.positions.trades.models.constants.TradeType;
import com.equity.positions.trades.services.PositionService;


@Service
@Profile("demo")
public class LoadSampleTransaction {
	
	public static final Logger log = LoggerFactory.getLogger(LoadSampleTransaction.class);

    @Bean
    public ApplicationRunner initializer(PositionService positionService) {
        return args -> {
            // Sample data for testing
            if (positionService !=null) {
            	
            	
            	positionService.processTransactions(List.of(
                        new TransactionDTO(null, 1L, 1, "REL", 50, Action.INSERT, TradeType.BUY),
                        new TransactionDTO(null, 2L, 1, "ITC", 40, Action.INSERT, TradeType.SELL),
                        new TransactionDTO(null, 3L, 1, "INF", 70, Action.INSERT, TradeType.BUY),
                        new TransactionDTO(null, 1L, 2, "REL", 60, Action.UPDATE, TradeType.BUY),
                        new TransactionDTO(null, 2L, 2, "ITC", 30, Action.CANCEL, TradeType.BUY),
                        new TransactionDTO(null, 4L, 1, "INF", 20, Action.INSERT, TradeType.SELL)
                ));
            }
            log.info("Sample Test OutPut : {}",positionService.getCurrentPositions());
            
        };
    }
}