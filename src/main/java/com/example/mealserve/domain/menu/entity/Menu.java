package com.example.mealserve.domain.menu.entity;

import com.example.mealserve.domain.store.entity.Store;
import com.example.mealserve.exception.CustomException;
import com.example.mealserve.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Builder
    private Menu(String name, Integer price, String image, Store store) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.store = store;
    }

    public static Menu of(String name, Integer price, String image, Store store) {
        return Menu.builder()
                .name(name)
                .price(price)
                .image(image)
                .store(store)
                .build();
    }

    public void updateMenuDetail(String name, Integer price, String image) {
        if (name != null && !name.isEmpty()) this.name = name;
        if (price != null) {
            if (price < 0) {
                throw new CustomException(ErrorCode.NEGATIVE_PRICE_NOT_ALLOWED);
            }
            this.price = price;
        }
        if (image != null && !image.isEmpty()) this.image = image;
    }
}

