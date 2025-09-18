package com.obsidiam.exchange.api.dto;

import com.obsidiam.exchange.service.model.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExchangeOrderStatusResponseDto {

    private Status status;
}
