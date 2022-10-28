package com.epam.esm.repository;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AbstractRepository<T> {
    T create(T entity);
    Optional<T> findById(long id);
    List<T> findAll(Pageable pageable);
    T update(T entity);
    void delete(T id);
    Optional<T> findByField(String fieldName, Object value);
}
