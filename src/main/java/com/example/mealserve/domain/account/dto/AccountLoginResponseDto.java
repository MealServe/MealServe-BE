package com.example.mealserve.domain.account.dto;

import com.example.mealserve.domain.account.entity.RoleTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountLoginResponseDto {

    private RoleTypeEnum role;
}
