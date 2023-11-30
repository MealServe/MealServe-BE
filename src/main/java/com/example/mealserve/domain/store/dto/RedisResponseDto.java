package com.example.mealserve.domain.store.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class RedisResponseDto {
    private List<StoreResponseDto> responseDtos = new ArrayList<>();

    public RedisResponseDto(List<StoreResponseDto> responseDtos) {
        this.responseDtos = responseDtos;
    }
}
