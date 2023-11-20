package com.example.mealserve.domain.account.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountLoginRequestDto {

    @Email(message = "형식에 맞게 입력하세요")
    @NotBlank(message = "이메일은 비어있을 수 없습니다")
    private String email;

    @NotBlank(message = "비밀번호는 비어있을 수 없습니다")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,15}$",
            message = "비밀번호는 8~15자리면서 알파벳, 숫자, 특수만자를 포함해야합니다")
    private String password;
}
