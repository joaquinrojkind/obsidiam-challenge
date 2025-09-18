package com.obsidiam.exchange.persistence.entity;

import com.obsidiam.exchange.service.model.Currency;
import com.obsidiam.exchange.service.model.Status;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class ExchangeOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "exchange_type", nullable = false)
    private ExchangeType exchangeType;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "source_currency", nullable = false)
    private Currency sourceCurrency;

    @Column(name = "target_currency", nullable = false)
    private Currency targetCurrency;

    @Column(name = "status", nullable = false)
    private Status status;
}
