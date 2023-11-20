package com.example.mealserve.domain.order.dto;

import com.example.mealserve.domain.order.entity.DeliverStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MessageResponseDto {
    private final String status;

    @Builder
    private MessageResponseDto(String status) {
        this.status = status;
    }

    public static MessageResponseDto from(DeliverStatus deliverStatus) {
        return MessageResponseDto.builder()
                .status(deliverStatus.getDescription())
                .build();
    }
}
