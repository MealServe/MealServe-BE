package com.example.mealserve.domain.store;

import com.example.mealserve.domain.store.document.StoreDocument;
import com.example.mealserve.domain.store.dto.StoreResponseDto;
import com.example.mealserve.global.exception.CustomException;
import com.example.mealserve.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreSearchService {

    private final StoreSearchRepository storeSearchRepository;

    public List<StoreResponseDto> getStoreDocumentByKeyword(String keyword) {
        List<StoreDocument> storeList = storeSearchRepository.findAllByNameContainsIgnoreCase(keyword);
        checkIfStoresExist(storeList);
        return storeList.stream()
                .map(StoreResponseDto::fromEsDocument)
                .toList();
    }

    private void checkIfStoresExist(List<StoreDocument> storeList) {
        if (storeList.isEmpty()) {
            throw new CustomException(ErrorCode.STORE_NOT_MATCH);
        }
    }
}
