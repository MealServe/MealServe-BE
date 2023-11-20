package com.example.mealserve.domain.account.service;

import com.example.mealserve.domain.account.dto.AccountJoinRequestDto;
import com.example.mealserve.domain.account.entity.Account;
import com.example.mealserve.domain.account.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerNewAccount(AccountJoinRequestDto requestDto) {

        // 이메일 중복 확인
        String email = requestDto.getEmail();
        if (accountRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 인코딩
        String password = passwordEncoder.encode(requestDto.getPassword());

        Account account = Account.builder()
                .email(email)
                .password(password)
                .phone(requestDto.getPhone())
                .address(requestDto.getAddress())
                .isOwner(requestDto.isOwner())
                .build();

        // 계정 저장
        accountRepository.save(account);
    }
}
