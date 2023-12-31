package com.example.mealserve.domain.account;

import com.example.mealserve.domain.account.dto.SignupRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/signup")
    public ResponseEntity<Void> registerAccount(@RequestBody SignupRequestDto requestDto) {
        accountService.registerNewAccount(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
