package com.example.mealserve.domain.menu;

import com.example.mealserve.domain.menu.dto.MenuRequestDto;
import com.example.mealserve.domain.menu.dto.MenuResponseDto;
import com.example.mealserve.domain.store.entity.Store;
import com.example.mealserve.exception.CustomException;
import com.example.mealserve.exception.ErrorCode;
import com.example.mealserve.security.UserDetailsImpl;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@RestController
@MultipartConfig
@RequiredArgsConstructor
@RequestMapping("/api/stores/menus")
public class MenuController {
    private final MenuService menuService;

    @PostMapping("")
    @PreAuthorize("hasAnyRole('OWNE111R')")
    public ResponseEntity<MenuResponseDto> addMenu(@Valid @RequestPart("menu") MenuRequestDto menuRequestDto,
                                                   @RequestPart("imageFile") MultipartFile image,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                   HttpServletRequest request) throws IOException {
        Store store = userDetails.getStore();
        if (store == null) {
            throw new CustomException(ErrorCode.STORE_NOT_FOUND);
        }
        menuRequestDto.updateImageFile(image);
        MenuResponseDto menuResponseDto = menuService.addMenu(menuRequestDto, store.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(menuResponseDto);
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<MenuResponseDto> getMenu(@PathVariable Long menuId) {
        MenuResponseDto menuResponseDto = menuService.getMenu(menuId);
        return ResponseEntity.status(HttpStatus.OK).body(menuResponseDto);
    }

    @PatchMapping("/{menuId}")
    @PreAuthorize("hasAnyRole('OWNER')")
    public ResponseEntity<MenuResponseDto> updateMenu(@PathVariable Long menuId,
                                                      @ModelAttribute MenuRequestDto menuRequestDto,
                                                      @RequestParam(value = "imageFile", required = false) MultipartFile image) throws IOException {
        MenuResponseDto menuResponseDto = menuService.updateMenu(menuId, menuRequestDto, image);
        return ResponseEntity.status(HttpStatus.OK).body(menuResponseDto);
    }

    @DeleteMapping("/{menuId}")
    @PreAuthorize("hasAnyRole('OWNER')")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long menuId) {
        Long storeId = menuService.deleteMenu(menuId);
        URI storeUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/stores/{storeId}")
                .buildAndExpand(storeId)
                .toUri();
        return ResponseEntity.status(HttpStatus.SEE_OTHER).location(storeUri).build();
    }
}
