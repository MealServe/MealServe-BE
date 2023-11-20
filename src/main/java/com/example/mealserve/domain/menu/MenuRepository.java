package com.example.mealserve.domain.menu;

import com.example.mealserve.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface MenuRepository extends JpaRepository<Menu, Long> {
    Menu findByName(String menu);
    Optional<Menu> findByIdAndStoreId(Long storeId, Long menuId);
    List<Menu> findByStoreId(Long storeId);
}
