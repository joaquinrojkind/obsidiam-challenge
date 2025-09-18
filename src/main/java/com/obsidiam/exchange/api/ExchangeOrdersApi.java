package com.obsidiam.exchange.api;

import com.obsidiam.exchange.api.dto.ExchangeOrderDto;
import com.obsidiam.exchange.api.dto.ExchangeOrderResponseDto;
import com.obsidiam.exchange.api.dto.ExchangeOrderStatusResponseDto;
import com.obsidiam.exchange.service.ExchangeOrderService;
import com.obsidiam.exchange.service.model.ExchangeOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exchanges")
public class ExchangeOrdersApi {

    @Autowired
    private ExchangeOrderService exchangeOrderService;

    @PostMapping("/orders")
    public ResponseEntity<ExchangeOrderResponseDto> createExchangeOrder(@RequestBody ExchangeOrderDto exchangeOrderDto) {

        Long exchangeOrderId;
        try {
            exchangeOrderId = exchangeOrderService.createExchangeOrder(toExchangeOrder(exchangeOrderDto));
        } catch (RuntimeException e) {
            // TODO exception handling
            throw e;
        }
        ExchangeOrderResponseDto response = ExchangeOrderResponseDto.builder()
                .id(exchangeOrderId)
                .location(String.format("/exchanges/%s/status", exchangeOrderId))
                .build();

        return ResponseEntity.accepted().body(response);
    }

    @GetMapping("/orders/{id}/status")
    public ResponseEntity<ExchangeOrderStatusResponseDto> checkExchangeOrderStatus(@PathVariable("id") Long exchangeOrderId) {

        return ResponseEntity.ok(
                ExchangeOrderStatusResponseDto.builder()
                        .status(exchangeOrderService.checkExchangeOrderStatus(exchangeOrderId))
                        .build());
    }

    private ExchangeOrder toExchangeOrder(ExchangeOrderDto exchangeOrderDto) {
        return ExchangeOrder.builder()
                .exchangeType(exchangeOrderDto.getExchangeType())
                .amount(exchangeOrderDto.getAmount())
                .sourceCurrency(exchangeOrderDto.getSourceCurrency())
                .targetCurrency(exchangeOrderDto.getTargetCurrency())
                .build();
    }
}
