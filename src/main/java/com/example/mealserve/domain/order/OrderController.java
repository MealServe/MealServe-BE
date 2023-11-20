package com.example.mealserve.domain.order;

import com.example.mealserve.domain.account.entity.Account;
import com.example.mealserve.domain.order.dto.OrderListResponseDto;
import com.example.mealserve.domain.order.dto.OrderRequestDto;
import com.example.mealserve.domain.order.dto.OrderResponseDto;
import com.example.mealserve.global.tool.LoginAccount;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{storeId}/orders")
    public OrderResponseDto orderIn(@PathVariable Long storeId,
                                    @RequestBody @Valid List<OrderRequestDto> requestDtoList,
                                    @LoginAccount Account account) {

        return orderService.orderIn(storeId, requestDtoList, account);
    }

    @GetMapping("/orders")
    public List<OrderListResponseDto> getOrders(@LoginAccount Account account) {
        return orderService.getOrders(account);
    }
}
