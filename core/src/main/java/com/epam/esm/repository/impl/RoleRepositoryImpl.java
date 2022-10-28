package com.epam.esm.repository.impl;

import com.epam.esm.constant.database.ColumnConstant;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Role;
import com.epam.esm.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
public class RoleRepositoryImpl extends AbstractRepositoryImpl<Role> implements RoleRepository {

    @Autowired
    public RoleRepositoryImpl(EntityManager entityManager){
        super(entityManager, Role.class);
    }

    @Override
    public Optional<Role> findByName(String name) {
        CriteriaQuery<Role> query = criteriaBuilder.createQuery(Role.class);
        Root<Role> roleRoot = query.from(Role.class);
        roleRoot.fetch("users", JoinType.LEFT);

        query.where(criteriaBuilder.equal(roleRoot.get(ColumnConstant.ROLE_NAME), name));
        query.select(roleRoot);
        TypedQuery<Role> roleTypedQuery = entityManager.createQuery(query);
        if (roleTypedQuery.getResultList().size() == 0){
            return Optional.empty();
        }
        return Optional.of(roleTypedQuery.getSingleResult());
    }
}
