package com.obsidiam.exchange.service.model;

import com.obsidiam.exchange.persistence.entity.ExchangeType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class ExchangeOrder {

    private ExchangeType exchangeType;
    private Double amount;
    private Currency sourceCurrency;
    private Currency targetCurrency;
    @Setter private Status status;
}
