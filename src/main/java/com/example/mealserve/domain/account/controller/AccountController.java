package com.example.mealserve.domain.account.controller;

import com.example.mealserve.domain.account.dto.AccountJoinRequestDto;
import com.example.mealserve.domain.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerAccount(@RequestBody AccountJoinRequestDto requestDto) {
        accountService.registerNewAccount(requestDto);
        return ResponseEntity.ok().build();
    }

}
