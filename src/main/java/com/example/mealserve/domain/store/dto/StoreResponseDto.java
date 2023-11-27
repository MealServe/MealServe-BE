package com.example.mealserve.domain.store.dto;

import com.example.mealserve.domain.menu.dto.MenuResponseDto;
import com.example.mealserve.domain.store.document.StoreDocument;
import com.example.mealserve.domain.store.entity.Store;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreResponseDto {

    private final Long id;
    private final String name;
    private final String address;
    private final String tel;
    private final List<MenuResponseDto> menus;

    @Builder
    private StoreResponseDto(Long id, String name, String address, String tel, List<MenuResponseDto> menus) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.tel = tel;
        this.menus = menus;
    }

    public static StoreResponseDto of(Store store, List<MenuResponseDto> menus) {
        return StoreResponseDto.builder()
            .id(store.getId())
            .name(store.getName())
            .address(store.getAddress())
            .tel(store.getTel())
            .menus(menus)
            .build();
    }

    public static StoreResponseDto from(Store store) {
        return StoreResponseDto.builder()
            .id(store.getId())
            .name(store.getName())
            .address(store.getAddress())
            .tel(store.getTel())
            .build();
    }

    public static StoreResponseDto fromEsDocument(StoreDocument storeDocument) {
        return StoreResponseDto.builder()
                .id(storeDocument.getId())
                .name(storeDocument.getName())
                .address(storeDocument.getAddress())
                .tel(storeDocument.getTel())
                .build();
    }
}