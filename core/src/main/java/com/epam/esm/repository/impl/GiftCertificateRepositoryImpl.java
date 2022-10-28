package com.epam.esm.repository.impl;

import com.epam.esm.constant.database.ColumnConstant;
import com.epam.esm.constant.database.TableConstant;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.sort.SortParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class GiftCertificateRepositoryImpl extends AbstractRepositoryImpl<GiftCertificate> implements GiftCertificateRepository {

    @Autowired
    public GiftCertificateRepositoryImpl(EntityManager entityManager){
        super(entityManager, GiftCertificate.class);
    }

    @Override
    public Optional<GiftCertificate> findByName(String name) {
        return findByField(ColumnConstant.CERTIFICATE_NAME, name);
    }

    @Override
    public Optional<GiftCertificate> findById(long id){
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> certificateRoot = query.from(GiftCertificate.class);
        certificateRoot.fetch("tags", JoinType.LEFT);

        query.where(criteriaBuilder.equal(certificateRoot.get(ColumnConstant.ID),id));
        query.select(certificateRoot);
        TypedQuery<GiftCertificate> certificateTypedQuery = entityManager.createQuery(query);
        if (certificateTypedQuery.getResultList().size() == 0){
            return Optional.empty();
        }
        return Optional.of(certificateTypedQuery.getSingleResult());
    }

    @Override
    public void delete(GiftCertificate entity) {
        Set<Tag> tags = entity.getTags();
        tags.forEach(entity::removeTag);
        entityManager.remove(entity);
    }

    @Override
    public List<GiftCertificate> getCertificateByTag(List<String> tags, SortParams sortParamsMap,
                                                     String searchParam, String searchInfo,
                                                     Pageable pageable) {
        CriteriaQuery<GiftCertificate> query = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> certificateRoot = query.from(GiftCertificate.class);
        query.select(certificateRoot);
        query.distinct(true);

        List<Predicate> predicates = new LinkedList<>();
        ifExistAddSearchParam(predicates, certificateRoot, searchParam, searchInfo);

        List<Order> orderList = ifExistAddOrders(certificateRoot, sortParamsMap);

        if (!tags.isEmpty()) {
            predicates.clear();
            orderList.clear();
            Join<GiftCertificate, Tag> join = certificateRoot.join("tags");

            CriteriaBuilder.In<String> inClause = criteriaBuilder.in(join.get(ColumnConstant.TAG_NAME));
            for (String tagName : tags) {
                inClause.value(tagName);
            }

            if (!(searchParam == null || searchInfo == null)) {
                predicates.add(criteriaBuilder.like(join.get(searchParam), "%" + searchInfo + "%"));
            }
            orderList = ifExistAddOrders(certificateRoot, sortParamsMap);

            Predicate[] predArray = predListToArray(predicates);
            query.where(predArray);
            query.where(inClause);
            query.orderBy(orderList);
            query.groupBy(certificateRoot.get(ColumnConstant.ID));
            query.having(criteriaBuilder.greaterThanOrEqualTo(criteriaBuilder.count(certificateRoot.get(ColumnConstant.ID)),(long) tags.size()));
        } else {
            Predicate[] predArray = predListToArray(predicates);
            query.where(predArray);
            query.orderBy(orderList);
        }

        List<GiftCertificate> result = entityManager.createQuery(query)
                .setFirstResult((int)pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        System.out.println(result.size());
        return result;
    }

    private Predicate[] predListToArray(List<Predicate> predList){
        Predicate[] predArray = new Predicate[predList.size()];
        predList.toArray(predArray);
        return predArray;
    }

    private void ifExistAddSearchParam(List<Predicate> predicates, Root<GiftCertificate> certificateRoot, String searchParam, String searchInfo){
        if (!(searchParam == null || searchInfo == null)) {
            predicates.add(criteriaBuilder.like(certificateRoot.get(searchParam), "%" + searchInfo + "%"));
        }
    }

    private List<Order> ifExistAddOrders(Root<GiftCertificate> certificateRoot, SortParams sortParams){
        List<Order> orderList = new LinkedList<>();
        if (sortParams != null) {
            Set<String> columnNames = sortParams.getSortParamsMap().keySet();
            for (String columnName : columnNames){
                if (sortParams.getSortParamsMap().get(columnName).toString().equalsIgnoreCase("asc")){
                    orderList.add(criteriaBuilder.asc(certificateRoot.get(columnName)));
                } else {
                    orderList.add(criteriaBuilder.desc(certificateRoot.get(columnName)));
                }
            }
        }
        return orderList;
    }

}
