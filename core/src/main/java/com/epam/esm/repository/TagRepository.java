package com.epam.esm.repository;

import com.epam.esm.model.MostUsedTag;
import com.epam.esm.model.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends AbstractRepository<Tag>{
    List<String> findAllTagsName();
    Optional<Tag> findByName(String name);
    Optional<MostUsedTag> findMostUsedTagWithHightCoastByUserId(long userId);
}
