package com.epam.esm.repository.impl;

import com.epam.esm.constant.database.ColumnConstant;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.entity.User;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepositoryImpl extends AbstractRepositoryImpl<Order> implements OrderRepository {

    @Autowired
    public OrderRepositoryImpl(EntityManager entityManager){
        super(entityManager, Order.class);
    }

    @Override
    public Optional<Order> findById(long id){
        CriteriaQuery<Order> query = criteriaBuilder.createQuery(Order.class);
        Root<Order> orderRoot = query.from(Order.class);
        orderRoot.fetch("certificate", JoinType.LEFT);

        query.where(criteriaBuilder.equal(orderRoot.get(ColumnConstant.ID),id));

        TypedQuery<Order> certificateTypedQuery = entityManager.createQuery(query.select(orderRoot));
        if (certificateTypedQuery.getResultList().size() == 0 ){
            return Optional.empty();
        }
        return Optional.of(certificateTypedQuery.getSingleResult());
    }

    @Override
    public List<Order> getAllByUserId(long userId, Pageable pageable){
        CriteriaQuery<Order> query = criteriaBuilder.createQuery(Order.class);
        Root<Order> orderRoot = query.from(Order.class);
        Predicate userIdPredicate = criteriaBuilder.equal(orderRoot.get("user"), userId);
        query.where(userIdPredicate);
        query.select(orderRoot);
        return entityManager.createQuery(query)
                .setFirstResult((int)pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }
}
