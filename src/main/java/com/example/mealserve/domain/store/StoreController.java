package com.example.mealserve.domain.store;

import com.example.mealserve.domain.account.entity.Account;
import com.example.mealserve.domain.store.dto.StoreRequestDto;
import com.example.mealserve.domain.store.dto.StoreResponseDto;
import com.example.mealserve.global.tool.LoginAccount;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<StoreResponseDto> registerStore(@RequestBody @Validated StoreRequestDto storeRequestDto,
                                                          @LoginAccount Account account) {
        StoreResponseDto responseBody = storeService.registerStore(storeRequestDto, account);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PutMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> updateStore(@PathVariable Long storeId,
                                                        @RequestBody @Validated StoreRequestDto storeRequestDto) {
        StoreResponseDto responseBody = storeService.updateStore(storeId, storeRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long storeId) {
        storeService.deleteStore(storeId);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/")
            .build()
            .toUri();
        return ResponseEntity.status(HttpStatus.OK).location(uri).build();
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> getStore(@PathVariable Long storeId) {
        StoreResponseDto responseBody = storeService.getStore(storeId);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @GetMapping
    public ResponseEntity<Page<StoreResponseDto>> getStores(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        Page<StoreResponseDto> responseBody = storeService.getStores(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
