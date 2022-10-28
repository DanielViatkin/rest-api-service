package com.epam.esm.repository.impl;

import com.epam.esm.constant.database.ColumnConstant;
import com.epam.esm.model.entity.User;
import com.epam.esm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
public class UserRepositoryImpl extends AbstractRepositoryImpl<User> implements UserRepository {

    @Autowired
    public UserRepositoryImpl(EntityManager entityManager){
        super(entityManager, User.class);
    }

    @Override
    public Optional<User> findById(long id){
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = query.from(User.class);
        userRoot.fetch("orders", JoinType.LEFT);
        userRoot.fetch("roles", JoinType.LEFT);

        query.where(criteriaBuilder.equal(userRoot.get(ColumnConstant.ID),id));

        TypedQuery<User> certificateTypedQuery = entityManager.createQuery(query.select(userRoot));
        if (certificateTypedQuery.getResultList().size() == 0 ){
            return Optional.empty();
        }

        return Optional.of(certificateTypedQuery.getSingleResult());
    }

    @Override
    public Optional<User> findByLogin(String login) {
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = query.from(User.class);
        userRoot.fetch("orders", JoinType.LEFT);
        userRoot.fetch("roles", JoinType.LEFT);

        query.where(criteriaBuilder.equal(userRoot.get(ColumnConstant.USER_LOGIN),login));

        TypedQuery<User> certificateTypedQuery = entityManager.createQuery(query.select(userRoot));
        if (certificateTypedQuery.getResultList().size() == 0 ){
            return Optional.empty();
        }

        return Optional.of(certificateTypedQuery.getSingleResult());
    }
}
