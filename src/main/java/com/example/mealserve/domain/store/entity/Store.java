package com.example.mealserve.domain.store.entity;

import com.example.mealserve.domain.account.entity.Account;
import com.example.mealserve.domain.menu.entity.Menu;
import com.example.mealserve.domain.store.dto.StoreRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 기본 생성자의 생성을 방지, 지정한 생성자를 사용하도록 강제!
// PROTECTED: 상속은 가능
@Getter
@Entity
@Table(name = "store")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String tel;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;


    @Builder
    private Store(String name, String address, String tel) {
        this.name = name;
        this.address = address;
        this.tel = tel;
    }

    public void update(StoreRequestDto storeRequestDto) {
        this.name = storeRequestDto.getName();
        this.address = storeRequestDto.getAddress();
        this.tel = storeRequestDto.getTel();
    }
}
