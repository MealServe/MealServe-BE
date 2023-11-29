package com.example.mealserve.domain.store;

import com.example.mealserve.domain.store.entity.StoreHash;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreHashRepository extends CrudRepository<StoreHash, String> {

}
