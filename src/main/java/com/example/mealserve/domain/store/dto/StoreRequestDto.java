package com.example.mealserve.domain.store.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreRequestDto {

    @NotBlank(message = "가게 이름을 입력해 주세요")
    private String name;

    @NotBlank(message = "주소를 입력해 주세요")
    private String address;

    @NotBlank(message = "전화번호를 입력해 주세요")
    private String tel;
}
