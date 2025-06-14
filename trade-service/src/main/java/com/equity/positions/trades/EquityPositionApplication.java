package com.equity.positions.trades;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EquityPositionApplication {

    public static void main(String[] args) {
        SpringApplication.run(EquityPositionApplication.class, args);
    }
}
