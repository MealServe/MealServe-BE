package com.example.mealserve.domain.store.document;

import com.example.mealserve.domain.store.entity.Store;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Document(indexName = "stores")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreDocument {

    @Id
    private Long id;

    private String name;

    private String address;

    private String tel;

    @Builder
    private StoreDocument(Long id, String name, String address, String tel) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.tel = tel;
    }

    public static StoreDocument from(Store store) {
        return StoreDocument.builder()
                .id(store.getId())
                .name(store.getName())
                .address(store.getAddress())
                .tel(store.getTel())
                .build();
    }
}
