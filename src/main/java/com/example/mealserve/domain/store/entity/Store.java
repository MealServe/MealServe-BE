package com.example.mealserve.domain.store.entity;

import com.example.mealserve.domain.account.entity.Account;
import com.example.mealserve.domain.store.dto.StoreRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "store")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, unique = true)
    private String tel;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Builder
    private Store(String name, String address, String tel, Account account) {
        this.name = name;
        this.address = address;
        this.tel = tel;
        this.account = account;
    }

    public static Store of(StoreRequestDto requestDto, Account account) {
        return Store.builder()
                .name(requestDto.getName())
                .address(requestDto.getAddress())
                .tel(requestDto.getTel())
                .account(account)
                .build();
    }

    public void update(StoreRequestDto storeRequestDto) {
        this.name = storeRequestDto.getName();
        this.address = storeRequestDto.getAddress();
        this.tel = storeRequestDto.getTel();
    }
}
