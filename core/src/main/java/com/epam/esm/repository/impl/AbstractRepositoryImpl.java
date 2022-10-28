package com.epam.esm.repository.impl;

import com.epam.esm.constant.database.ColumnConstant;
import com.epam.esm.repository.AbstractRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Transactional
public abstract class AbstractRepositoryImpl<T> implements AbstractRepository<T> {

    @PersistenceContext
    protected final EntityManager entityManager;
    protected final CriteriaBuilder criteriaBuilder;
    protected final Class<T> entityType;

    public AbstractRepositoryImpl(EntityManager entityManager,  Class<T> entityType){
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
        this.entityType = entityType;
    }

    @Override
    public T create(T entity){
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Optional<T> findById(long id) {
        return findByField(ColumnConstant.ID, id);
    }

    @Override
    public List<T> findAll(Pageable pageable) {
        CriteriaQuery<T> query = criteriaBuilder.createQuery(entityType);
        Root<T> root = query.from(entityType);
        return entityManager.createQuery(query.select(root))
                .setFirstResult((int)pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public T update(T entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void delete(T entity) {
        if (entity != null){
            entityManager.remove(entity);
        }
    }

    @Override
    public Optional<T> findByField(String fieldName, Object value){
        CriteriaQuery<T> entityQuery = criteriaBuilder.createQuery(entityType);

        Root<T> entity = entityQuery.from(entityType);
        Predicate fieldPredicate = criteriaBuilder.equal(entity.get(fieldName), value);
        entityQuery.where(fieldPredicate);
        entityQuery.select(entity);
        TypedQuery<T> query = entityManager.createQuery(entityQuery);
        if (query.getResultList().size() == 0){
            return Optional.empty();
        }
        return Optional.of(query.getSingleResult());
    }
}
