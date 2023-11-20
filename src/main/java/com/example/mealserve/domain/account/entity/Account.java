package com.example.mealserve.domain.account.entity;

import com.example.mealserve.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String phone;
    private String address;
    private Long point;

    @Enumerated(EnumType.STRING)
    private RoleTypeEnum role;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private Store store;

    @Builder
    public Account(String email,
                   String password,
                   String phone,
                   String address,
                   RoleTypeEnum role) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.point = role == RoleTypeEnum.CUSTOMER ? 1000000L : 0L;

    }
}
