package com.example.mealserve.domain.order;

import com.example.mealserve.domain.account.AccountRepository;
import com.example.mealserve.domain.account.entity.Account;
import com.example.mealserve.domain.menu.MenuRepository;
import com.example.mealserve.domain.menu.entity.Menu;
import com.example.mealserve.domain.order.dto.*;
import com.example.mealserve.domain.order.entity.DeliverStatus;
import com.example.mealserve.domain.order.entity.Order;
import com.example.mealserve.domain.store.StoreRepository;
import com.example.mealserve.domain.store.entity.Store;
import com.example.mealserve.global.exception.CustomException;
import com.example.mealserve.global.exception.ErrorCode;
import com.example.mealserve.global.websocket.AlarmHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j(topic = "OrderService")
@Service
@RequiredArgsConstructor
public class OrderService {

    private final AccountRepository accountRepository;
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final AlarmHandler alarmHandler;

    @Transactional
    public OrderResponseDto orderIn(Long storeId, OrderListRequestDto requestDtoList, Account customer) {
        Store store = findStore(storeId);
        Account owner = findAccount(store.getAccount().getId());
        List<OrderDto> orderDtoList = new ArrayList<>();

        int totalPrice = 0;
        for (OrderRequestDto requestDto : requestDtoList.getOrders()) {
            Menu menu = getMenu(requestDto);

            Order newOrder = Order.of(customer, menu, requestDto.getQuantity(), DeliverStatus.PREPARE);
            orderRepository.save(newOrder);

            orderDtoList.add(OrderDto.fromCustomer(newOrder));
            totalPrice += menu.getPrice() * newOrder.getQuantity();
        }
        checkEnoughPoint(customer, totalPrice).payPoint(totalPrice);
        accountRepository.save(customer);

        alarmHandler.sendNotification(owner.getEmail(), "주문이 들어왔습니다.");

        return OrderResponseDto.of(orderDtoList, totalPrice);
    }

    @Transactional(readOnly = true)
    public List<OrderListResponseDto> getOrders(Account owner) {
        Store store = owner.getStore();
        List<Order> orders = orderRepository.findAllByStoreIdAndStatus(store.getId());
        checkOrderExists(orders);
        List<OrderDto> orderDtoList = new ArrayList<>();
        List<OrderListResponseDto> orderListResponseDtos = new ArrayList<>();

        int i = 0, j = 0, totalPrice = 0;
        while (true) {
            Account user = orders.get(i).getAccount();
            if (user == orders.get(i + j).getAccount()) {
                orderDtoList.add(OrderDto.fromOwner(orders.get(i + j)));
                totalPrice += orders.get(i + j).getMenu().getPrice() * orders.get(i + j).getQuantity();
                j++;
            } else {
                orderListResponseDtos.add(OrderListResponseDto.of(user, orderDtoList, totalPrice));
                i += j;
                j = 0;
                totalPrice = 0;
                orderDtoList.clear();
            }
            if (i + j == orders.size()) {
                orderListResponseDtos.add(OrderListResponseDto.of(user, orderDtoList, totalPrice));
                break;
            }
        }
        return orderListResponseDtos;
    }

    @Transactional
    public void completeOrders(Account owner, Long accountId) {
        Store store = owner.getStore();
        Account customer = findAccount(accountId);
        List<Order> orders = orderRepository.findAllByAccountIdAndStoreAndStatus(accountId, store);

        checkOrderExists(orders);
        int totalPrice = 0;
        for (Order order : orders) {
            checkOrderStatus(order).complete();
            totalPrice += order.getMenu().getPrice() * order.getQuantity();
        }
        owner.earnPoint(totalPrice);
        accountRepository.save(owner);

        alarmHandler.sendNotification(customer.getEmail(), "배달이 완료되었습니다.");
    }

    private Store findStore(Long id) {
        return storeRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.STORE_NOT_FOUND));
    }

    private Account findAccount(Long id) {
        return accountRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
    }

    private Menu getMenu(OrderRequestDto requestDto) {
        return menuRepository.findById(requestDto.getMenuId())
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));
    }

    private Account checkEnoughPoint(Account customer, int totalPrice) {
        if (customer.getPoint() < totalPrice)
            throw new CustomException(ErrorCode.INSUFFICIENT_POINT);
        return customer;
    }

    private void checkOrderExists(List<Order> orders) {
        if (orders.isEmpty()) throw new CustomException(ErrorCode.ORDER_NOT_FOUND);
    }

    private Order checkOrderStatus(Order order) {
        if (order.getStatus().equals(DeliverStatus.COMPLETE))
            throw new CustomException(ErrorCode.ORDER_NOT_FOUND);
        return order;
    }
}
