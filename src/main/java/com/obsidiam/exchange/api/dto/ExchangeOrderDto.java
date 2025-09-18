package com.obsidiam.exchange.api.dto;

import com.obsidiam.exchange.persistence.entity.ExchangeType;
import com.obsidiam.exchange.service.model.Currency;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExchangeOrderDto {

    private ExchangeType exchangeType;
    private Double amount;
    private Currency sourceCurrency;
    private Currency targetCurrency;
}
