package com.obsidiam.exchange.persistence.repository;

import com.obsidiam.exchange.persistence.entity.ExchangeOrderEntity;
import com.obsidiam.exchange.service.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeOrderRepository extends JpaRepository<ExchangeOrderEntity, Long> {

	List<ExchangeOrderEntity> findByStatus(Status status);
}
