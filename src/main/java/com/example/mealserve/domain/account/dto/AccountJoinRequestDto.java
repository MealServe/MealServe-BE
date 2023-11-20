package com.example.mealserve.domain.account.dto;

import com.example.mealserve.domain.account.entity.Account;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountJoinRequestDto {

    @NotBlank(message = "이메일은 비어있을 수 없습니다")
    @Email(message = "형식에 맞게 입력하세요")
    private String email;

    @NotBlank(message = "비밀번호는 비어있을 수 없습니다")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,15}$",
            message = "비밀번호는 8~15자리면서 알파벳, 숫자, 특수문자를 포함해야합니다")
    private String password;

    @NotBlank(message = "주소는 비어있을 수 없습니다")
    private String address;

    @NotBlank(message = "전화번호는 비어있을 수 없습니다")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "전화번호는 10~11자리의 숫자이어야 합니다")
    private String phone;

    private boolean isOwner;


    @Builder
    public Account toEntity() {
        return Account.builder()
                .email(email)
                .password(password)
                .phone(phone)
                .address(address)
                .isOwner(isOwner)
                .build();
    }
}
