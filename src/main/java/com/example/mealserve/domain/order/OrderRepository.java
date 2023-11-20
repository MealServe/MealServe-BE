package com.example.mealserve.domain.order;

import com.example.mealserve.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o " +
            "left join fetch o.account " +
            "left join fetch o.menu " +
            "where o.menu.store.id = :storeId")
    List<Order> findAllByStoreId(Long storeId);

    List<Order> findAllByAccountId(Long accountId);
}
