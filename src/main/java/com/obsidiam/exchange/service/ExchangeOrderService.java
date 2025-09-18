package com.obsidiam.exchange.service;

import com.obsidiam.exchange.service.model.ExchangeOrder;
import com.obsidiam.exchange.service.model.Status;

public interface ExchangeOrderService {

    Long createExchangeOrder(ExchangeOrder exchangeOrder);

    Status checkExchangeOrderStatus(Long exchangeOrderId);
}
