package com.example.mealserve.domain.store;

import com.example.mealserve.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    boolean existsByName(String name);

    List<Store> findAllByNameContaining(String keyword);
}
