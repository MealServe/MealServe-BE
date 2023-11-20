package com.example.mealserve.domain.order.dto;

import com.example.mealserve.domain.account.entity.Account;
import com.example.mealserve.domain.order.entity.DeliverStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderListResponseDto {

    private final Long customerId;
    private final String address;

    private final List<OrderDto> menus;
    private final LocalDateTime createdAt;

    private final int totalPrice;
    private final DeliverStatus status;

    @Builder
    private OrderListResponseDto(Long customerId, String address, List<OrderDto> menus, LocalDateTime createdAt, int totalPrice, DeliverStatus status) {
        this.customerId = customerId;
        this.address = address;
        this.menus = menus;
        this.createdAt = createdAt;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public static OrderListResponseDto from(Account account, List<OrderDto> menus, int totalPrice) {
        return OrderListResponseDto.builder()
                .customerId(account.getId())
                .address(account.getAddress())
                .menus(menus)
                .createdAt(menus.get(0).getCreatedAt())
                .totalPrice(totalPrice)
                .status(menus.get(0).getStatus())
                .build();
    }
}
