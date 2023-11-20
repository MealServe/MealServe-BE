package com.example.mealserve.domain.order.entity;

import com.example.mealserve.domain.menu.entity.Menu;
import com.example.mealserve.domain.account.entity.Account;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "`order`")
public class Order extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private DeliverStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menuId")
    private Menu menu;

    @Builder
    private Order(Account account, Menu menu, Integer quantity) {
        this.account = account;
        this.menu = menu;
        this.quantity = quantity;
        this.status = DeliverStatus.PREPARE;
    }

    public static Order of(Account account, Menu menu, Integer quantity) {
        return Order.builder()
                .account(account)
                .menu(menu)
                .quantity(quantity)
                .build();
    }

    public void complete() {
        this.status = DeliverStatus.COMPLETE;
    }
}
