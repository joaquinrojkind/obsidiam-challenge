package com.obsidiam.exchange.api.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExchangeOrderResponseDto {

    private Long id;
    private String location;
}
