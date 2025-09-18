package com.obsidiam.exchange.service;

import com.obsidiam.exchange.service.model.ExchangeOrder;
import com.obsidiam.exchange.persistence.entity.ExchangeOrderEntity;
import com.obsidiam.exchange.persistence.repository.ExchangeOrderRepository;
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

    @Autowired
    private ExchangeOrderRepository exchangeOrderRepository;

    /**
     * The transactional annotation ensures that nothing will be committed to the database until
     * the method returns successfully. Otherwise, it will rollback the transaction. This should
     * take care of some of the possible failure scenarios although not all of them. More details
     * on this can be found in the README file of the project.
     */
    @Override
    @Transactional
    public Long createExchangeOrder(ExchangeOrder exchangeOrder) {
        exchangeOrder.setStatus(Status.PENDING);
        ExchangeOrderEntity savedExchangeOrder = exchangeOrderRepository.save(toExchangeOrderEntity(exchangeOrder));

        List<ExchangeOrderEntity> pendingOrders = exchangeOrderRepository.findByStatus(Status.PENDING);

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

