package com.example.mealserve.domain.order.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponseDto {

    private final List<OrderDto> orders;
    private final int totalPrice;

    @Builder
    private OrderResponseDto(List<OrderDto> orders, int totalPrice) {
        this.orders = orders;
        this.totalPrice = totalPrice;
    }

    public static OrderResponseDto of(List<OrderDto> orders, int totalPrice) {
        return OrderResponseDto.builder()
                .orders(orders)
                .totalPrice(totalPrice)
                .build();
    }
}
