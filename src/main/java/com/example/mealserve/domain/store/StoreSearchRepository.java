package com.example.mealserve.domain.store;

import com.example.mealserve.domain.store.document.StoreDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreSearchRepository extends ElasticsearchRepository<StoreDocument, Long> {

    List<StoreDocument> findAllByNameContainsIgnoreCase(String keyword);
}
