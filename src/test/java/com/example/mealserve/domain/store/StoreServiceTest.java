package com.example.mealserve.domain.store;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.mealserve.domain.account.entity.Account;
import com.example.mealserve.domain.account.entity.AccountRole;
import com.example.mealserve.domain.menu.MenuRepository;
import com.example.mealserve.domain.menu.dto.MenuResponseDto;
import com.example.mealserve.domain.store.dto.StoreRequestDto;
import com.example.mealserve.domain.store.dto.StoreResponseDto;
import com.example.mealserve.domain.store.entity.Store;
import com.example.mealserve.global.exception.CustomException;
import com.example.mealserve.global.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    StoreRepository storeRepository;

    @Mock
    MenuRepository menuRepository;

    @InjectMocks
    StoreService storeService;


    @Nested
    @DisplayName("가게 등록")
    class RegisterTest {

        @Test
        @DisplayName("가게 등록 성공")
        void storeRegisterSuccess() {
            // given
            StoreRequestDto storeRequestDto = new StoreRequestDto("테스트 가게명", "테스트 주소", "01012341234");

            Account account = Account.builder()
//            .email("test@gmail.com")
//            .password("abcd1234")
//            .phone("01012341234")
//            .address("test address")
//            .role(AccountRole.OWNER)
                .build();

            Store savedStore = Store.builder()
                .name("테스트 가게명")
                .address("테스트 주소")
                .tel("01012341234")
                .build();

            given(storeRepository.existsByName(storeRequestDto.getName())).willReturn(false);
            given(storeRepository.save(any())).willReturn(savedStore);

            // when
            StoreResponseDto result = storeService.registerStore(storeRequestDto, account);

            // then
            assertThat(result.getName()).isEqualTo("테스트 가게명");
            assertThat(result.getAddress()).isEqualTo("테스트 주소");
            assertThat(result.getTel()).isEqualTo("01012341234");
            assertThat(result.getMenus()).isNull();
        }

        @Test
        @DisplayName("가게 등록 실패")
        void storeRegisterFail() {
            // given
            StoreRequestDto storeRequestDto = new StoreRequestDto("테스트 가게명", "테스트 주소", "01012341234");
            Account account = Account.builder().build();
            given(storeRepository.existsByName(storeRequestDto.getName())).willReturn(true);

            // when
            Exception exception = assertThrows(CustomException.class,
                () -> storeService.registerStore(storeRequestDto, account));

            // then
            assertThat(exception.getMessage()).isEqualTo(ErrorCode.STORE_ALREADY_EXISTS.getMessage());

        }
    }


    @Nested
    @DisplayName("가게 업데이트")
    class UpdateTest {

        @Test
        @DisplayName("가게 업데이트 성공")
        void storeUpdateSuccess() {
            // given
            Long storeId = 10L;
            StoreRequestDto storeRequestDto = new StoreRequestDto("업데이트 가게명", "업데이트 주소", "01012345678");

            Store store = Store.builder()
                .name("테스트 가게명")
                .address("테스트 주소")
                .tel("01012341234")
                .build();

            given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

            // when
            StoreResponseDto result = storeService.updateStore(storeId, storeRequestDto);

            // then
            assertThat(result.getName()).isEqualTo("업데이트 가게명");
            assertThat(result.getAddress()).isEqualTo("업데이트 주소");
            assertThat(result.getTel()).isEqualTo("01012345678");
            assertThat(result.getMenus()).isNull();

            // storeRepository.findById(storeId)가 1번 실행됐는지 확인
            verify(storeRepository, times(1)).findById(storeId);
        }


        @Test
        @DisplayName("가게 업데이트 실패")
        void storeUpdateFail() {
            // given
            Long storeId = 10L;
            StoreRequestDto storeRequestDto = new StoreRequestDto("업데이트 가게명", "업데이트 주소", "01012345678");

            given(storeRepository.findById(storeId)).willReturn(Optional.empty());

            // when
            Exception exception = assertThrows(CustomException.class,
                () -> storeService.updateStore(storeId, storeRequestDto));

            // then
            assertThat(exception.getMessage()).isEqualTo(ErrorCode.STORE_NOT_FOUND.getMessage());
        }
    }


    @Nested
    @DisplayName("가게 삭제")
    class DeleteTest {

        @Test
        @DisplayName("가게 삭제 성공")
        void storeDeleteSuccess() {
            // given
            Long storeId = 100L;

            Store store = Store.builder()
                .name("테스트 가게명")
                .address("테스트 주소")
                .tel("01012341234")
                .build();

            given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

            StoreService storeService = new StoreService(storeRepository, menuRepository);

            // when
            storeService.deleteStore(storeId);

            // then
            verify(storeRepository, times(1)).delete(store);
        }

        @Test
        @DisplayName("가게 삭제 실패")
        void storeDeleteFail() {
            // given
            Long storeId = 100L;

            given(storeRepository.findById(storeId)).willReturn(Optional.empty());

            // when
            Exception exception = assertThrows(CustomException.class,
                () -> storeService.deleteStore(storeId));

            // then
            assertThat(exception.getMessage()).isEqualTo(ErrorCode.STORE_NOT_FOUND.getMessage());

        }
    }


    @Nested
    @DisplayName("가게 조회")
    class GetTest {

        @Test
        @DisplayName("가게 조회 성공")
        void storeGetSuccess() {
            // given
            Long storeId = 50L;
            List<MenuResponseDto> menuResponseDto = new ArrayList<>();

            Store store = Store.builder()
                .name("테스트 가게명")
                .address("테스트 주소")
                .tel("01012341234")
                .build();

            given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

            // when
            StoreResponseDto result = storeService.getStore(storeId);

            // then
            assertThat(result.getName()).isEqualTo("테스트 가게명");
            assertThat(result.getAddress()).isEqualTo("테스트 주소");
            assertThat(result.getTel()).isEqualTo("01012341234");
            assertThat(result.getMenus()).isEqualTo(menuResponseDto);

        }


        @Test
        @DisplayName("가게 조회 실패")
        void storeGetFail() {
            // given
            Long storeId = 50L;

            given(storeRepository.findById(storeId)).willReturn(Optional.empty());

            // when
            Exception exception = assertThrows(CustomException.class,
                () -> storeService.getStore(storeId));

            // then
            assertThat(exception.getMessage()).isEqualTo(ErrorCode.STORE_NOT_FOUND.getMessage());

        }
    }

    @Nested
    @DisplayName("가게 리스트 조회")
    class GetStoreListTest {

        @Test
        @DisplayName("가게 리스트 조회 성공")
        void storeListGetSuccess() {
            // given
            int page = 0;
            int size = 10;
            Pageable pageRequest = PageRequest.of(page, size);
            List<Store> storeList = new ArrayList<>();
            for (int i = 0; i < size * 2; i++) {
                Store store = Store.builder()
                    .name("테스트 가게명" + i)
                    .address("테스트 주소" + i)
                    .tel("01012341234" + i)
                    .build();
                storeList.add(store);
            }
            int start = (int) pageRequest.getOffset();
            int end = Math.min((start + pageRequest.getPageSize()), storeList.size());

            Page<Store> stores = new PageImpl<>(storeList.subList(start, end), pageRequest, storeList.size());

            given(storeRepository.findAll(pageRequest)).willReturn(stores);

            // when
            Page<StoreResponseDto> result = storeService.getStores(page, size);

            // then
            assertThat(result.getTotalElements()).isEqualTo(size * 2);
            assertThat(result.getTotalPages()).isEqualTo((2));
        }
    }
}
