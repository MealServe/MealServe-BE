package com.example.mealserve.domain.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OrderRequestDto {

    @NotNull
    private Long menuId;

    @NotNull
    @Min(value = 1, message = "1인분 이상 입력해 주세요.")
    private Integer quantity;
}
