package com.obsidiam.exchange.service;

import com.obsidiam.exchange.persistence.entity.ExchangeOrderEntity;
import com.obsidiam.exchange.persistence.repository.ExchangeOrderRepository;
import com.obsidiam.exchange.service.model.ExchangeOrder;
import com.obsidiam.exchange.service.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ExchangeOrderServiceImpl implements ExchangeOrderService {

    private ThreadLocal<Integer> retryCounter = new ThreadLocal<>();

    private static final Integer RETRY_COUNTER_MAX = 2;

    @Autowired
    private ExchangeOrderRepository exchangeOrderRepository;

    /**
     * The transactional annotation ensures that nothing will be committed to the database until
     * the method returns successfully. Otherwise, it will rollback the transaction.
     */
    @Override
    @Transactional
    public Long createExchangeOrder(ExchangeOrder exchangeOrder) {

        ExchangeOrderEntity savedExchangeOrder = null;

        if (retryCounter.get() == null) {
            retryCounter.set(0);
        }

        if (retryCounter.get() <= RETRY_COUNTER_MAX) {
            try {
                List<ExchangeOrderEntity> pendingOrders = exchangeOrderRepository.findByStatus(Status.PENDING);

                exchangeOrder.setStatus(Status.PENDING);
                savedExchangeOrder = exchangeOrderRepository.save(toExchangeOrderEntity(exchangeOrder));

                List<ExchangeOrderEntity> filteredOrders = pendingOrders.stream()
                        .filter(order -> !order.getExchangeType().equals(exchangeOrder.getExchangeType()))
                        .filter(order -> order.getSourceCurrency().equals(exchangeOrder.getSourceCurrency()))
                        .filter(order -> order.getTargetCurrency().equals(exchangeOrder.getTargetCurrency()))
                        .filter(order -> order.getAmount().equals(exchangeOrder.getAmount()))
                        .collect(Collectors.toList());

                ExchangeOrderEntity match = filteredOrders.stream().findFirst().orElse(null);
                if (Objects.nonNull(match)) {
                    savedExchangeOrder.setStatus(Status.PROCESSED);
                    exchangeOrderRepository.save(savedExchangeOrder);
                    match.setStatus(Status.PROCESSED);
                    exchangeOrderRepository.save(match);
                }
            } catch (RuntimeException ex) {

                if (retryCounter.get() <= RETRY_COUNTER_MAX) {
                    Integer updatedCounter = retryCounter.get() + 1;
                    retryCounter.set(updatedCounter);
                    this.createExchangeOrder(exchangeOrder);
                } else {
                    throw new RuntimeException();
                }
            }
        }
        return savedExchangeOrder.getId();
    }

    @Override
    public Status checkExchangeOrderStatus(Long exchangeOrderId) {

        return Status.valueOf(
                exchangeOrderRepository.findById(exchangeOrderId).orElseThrow(EntityNotFoundException::new)
                        .getStatus().name());
    }

    private ExchangeOrderEntity toExchangeOrderEntity(ExchangeOrder exchangeOrder) {
             return ExchangeOrderEntity.builder()
                .exchangeType(exchangeOrder.getExchangeType())
                .amount(exchangeOrder.getAmount())
                .sourceCurrency(exchangeOrder.getSourceCurrency())
                .targetCurrency(exchangeOrder.getTargetCurrency())
                .status(exchangeOrder.getStatus())
                .build();
    }
}

