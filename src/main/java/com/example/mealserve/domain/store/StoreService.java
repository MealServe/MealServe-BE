package com.example.mealserve.domain.store;

import com.example.mealserve.domain.account.entity.Account;
import com.example.mealserve.domain.menu.MenuRepository;
import com.example.mealserve.domain.menu.dto.MenuResponseDto;
import com.example.mealserve.domain.menu.entity.Menu;
import com.example.mealserve.domain.store.dto.RedisResponseDto;
import com.example.mealserve.domain.store.dto.StoreRequestDto;
import com.example.mealserve.domain.store.dto.StoreResponseDto;
import com.example.mealserve.domain.store.entity.Store;
import com.example.mealserve.global.exception.CustomException;
import com.example.mealserve.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public StoreResponseDto registerStore(StoreRequestDto storeRequestDto, Account account) {
        checkIfStoreExist(storeRequestDto);
        Store store = Store.of(storeRequestDto, account);

        Store savedStore = storeRepository.save(store);

        return StoreResponseDto.from(savedStore);
    }

    @Transactional
    public StoreResponseDto updateStore(Long storeId, StoreRequestDto storeRequestDto) {
        Store store = getStoreById(storeId);

        store.update(storeRequestDto);

        return StoreResponseDto.from(store);
    }

    @Transactional
    public void deleteStore(Long storeId) {
        Store store = getStoreById(storeId);

        storeRepository.delete(store);
    }

    @Transactional(readOnly = true)
    public StoreResponseDto getStore(Long storeId) {
        Store store = getStoreById(storeId);

        List<Menu> menus = menuRepository.findByStoreId(storeId);
        List<MenuResponseDto> menuResponseDtoList = menus.stream()
                .map(MenuResponseDto::from)
                .toList();

        return StoreResponseDto.of(store, menuResponseDtoList);
    }

    @Transactional(readOnly = true)
    public Page<StoreResponseDto> getStores(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Store> stores = storeRepository.findAll(pageable);
        return stores.map(StoreResponseDto::from);
    }

    @Cacheable(value = "storeCash")
    @Transactional(readOnly = true)
    public RedisResponseDto getStoreByKeyword(String keyword) {
        List<Store> storeList = storeRepository.findAllByNameContaining(keyword);
        checkIfStoresExist(storeList);

        List<StoreResponseDto> storeResponseDtoList = storeList.stream()
                .map(StoreResponseDto::from).toList();
        return new RedisResponseDto(storeResponseDtoList);
    }


    private void checkIfStoreExist(StoreRequestDto storeRequestDto) {
        if (storeRepository.existsByName(storeRequestDto.getName())) {
            throw new CustomException(ErrorCode.STORE_ALREADY_EXISTS);
        }
    }

    private void checkIfStoresExist(List<Store> storeList) {
        if (storeList.isEmpty()) {
            throw new CustomException(ErrorCode.STORE_NOT_MATCH);
        }
    }

    private Store getStoreById(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
    }
}
