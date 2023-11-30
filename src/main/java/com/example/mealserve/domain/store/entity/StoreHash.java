package com.example.mealserve.domain.store.entity;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@Getter
@RedisHash(value = "storehash", timeToLive = 120)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreHash {

    @Id
    private String id;

    private String name;

    private String address;

    private String tel;

    private LocalDateTime createdAt;

    @Builder
    private StoreHash(String name, String address, String tel, LocalDateTime createdAt) {
        this.name = name;
        this.address = address;
        this.tel = tel;
        this.createdAt = LocalDateTime.now();
    }

    public static StoreHash from(Store store) {
        return StoreHash.builder()
                .name(store.getName())
                .address(store.getAddress())
                .tel(store.getTel())
                .build();
    }


}
