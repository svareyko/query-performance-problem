package com.example.test.repository;

import com.example.test.model.DataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author s.vareyko
 * @since 10/22/2018
 */
@Repository
public interface DataRepository extends JpaRepository<DataEntity, Long> {
    List<DataEntity> findAllByTextContains(String text);
}
