package com.example.mealserve.domain.order;

import com.example.mealserve.domain.account.entity.Account;
import com.example.mealserve.domain.account.entity.RoleTypeEnum;
import com.example.mealserve.domain.order.dto.MessageResponseDto;
import com.example.mealserve.domain.order.dto.OrderListResponseDto;
import com.example.mealserve.domain.order.dto.OrderRequestDto;
import com.example.mealserve.domain.order.dto.OrderResponseDto;

import com.example.mealserve.domain.order.entity.DeliverStatus;
import com.example.mealserve.global.tool.LoginAccount;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
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
                                    @LoginAccount Account customer) {

        return orderService.orderIn(storeId, requestDtoList, customer);
    }

    @Secured(RoleTypeEnum.Authority.OWNER)
    @GetMapping("/orders")
    public List<OrderListResponseDto> getOrders(@LoginAccount Account owner) {
        return orderService.getOrders(owner);
    }

    @Secured(RoleTypeEnum.Authority.OWNER)
    @PutMapping("/orders/{accountId}")
    public MessageResponseDto completeOrders(@LoginAccount Account owner, @PathVariable Long accountId) {
        orderService.completeOrders(owner, accountId);
        return MessageResponseDto.from(DeliverStatus.COMPLETE);
    }
}
