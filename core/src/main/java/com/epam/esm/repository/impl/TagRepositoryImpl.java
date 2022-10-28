package com.epam.esm.repository.impl;

import com.epam.esm.constant.database.ColumnConstant;
import com.epam.esm.model.MostUsedTag;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class TagRepositoryImpl extends AbstractRepositoryImpl<Tag> implements TagRepository {

    private static final String MOST_USED_WITH_HIGHEST_COST_TAG_QUERY =
            "SELECT t.id as 'id', t.name as 'tagName', MAX(o.cost) as 'highestCost'\n" +
                    "FROM orders o\n" +
                    "JOIN gift_certificate_has_tag gcht ON o.user_id = :userId AND o.gift_certificate_id = gcht.gift_certificate_id\n" +
                    "JOIN tags t ON gcht.tag_id = t.id\n" +
                    "GROUP BY t.id\n" +
                    "ORDER BY COUNT(t.id) DESC, MAX(o.cost) DESC\n" +
                    "LIMIT 1";

    private static final String RESULT_MAPPER_NAME = "MostUsedTagMapping";

    @Autowired
    public TagRepositoryImpl(EntityManager entityManager){
        super(entityManager, Tag.class);
    }

    @Override
    public Optional<Tag> findById(long id){
        CriteriaQuery<Tag> query = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> tagRoot = query.from(Tag.class);
        tagRoot.fetch("certificates",JoinType.LEFT);

        query.where(criteriaBuilder.equal(tagRoot.get(ColumnConstant.ID),id));

        TypedQuery<Tag> certificateTypedQuery = entityManager.createQuery(query.select(tagRoot));
        if (certificateTypedQuery.getResultList().size() == 0 ){
            return Optional.empty();
        }
        return Optional.of(certificateTypedQuery.getSingleResult());
    }

    @Override
    public List<String> findAllTagsName() {
        List<String> tagsName = new ArrayList<>();
        for (Tag tag : findAll(PageRequest.of(0,5000))){
            tagsName.add(tag.getName());
            System.out.println(tagsName.size());
        }
        return tagsName;
    }

    @Override
    public Optional<Tag> findByName(String name) {
        CriteriaQuery<Tag> query = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> tagRoot = query.from(Tag.class);
        tagRoot.fetch("certificates",JoinType.LEFT);

        query.where(criteriaBuilder.equal(tagRoot.get(ColumnConstant.TAG_NAME),name));

        TypedQuery<Tag> certificateTypedQuery = entityManager.createQuery(query.select(tagRoot));
        if (certificateTypedQuery.getResultList().size() == 0 ){
            return Optional.empty();
        }
        return Optional.of(certificateTypedQuery.getSingleResult());
    }

    @Override
    public Optional<MostUsedTag> findMostUsedTagWithHightCoastByUserId(long userId) {
        Query query = entityManager.createNativeQuery(
                MOST_USED_WITH_HIGHEST_COST_TAG_QUERY, RESULT_MAPPER_NAME);
        query.setParameter("userId", userId);
        MostUsedTag usedTagDto = (MostUsedTag) query.getSingleResult();
        System.out.println(usedTagDto);
        return Optional.of(usedTagDto);
    }

    @Override
    public void delete(Tag tag) {
        Set<GiftCertificate> certificates = tag.getCertificates();
        certificates.forEach(entityManager::merge);
        entityManager.remove(tag);
    }
}
