package com.epam.esm.service.logic.tag;

import com.epam.esm.model.MostUsedTag;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.exception.EntityAlreadyExistException;
import com.epam.esm.service.exception.NotFoundEntityException;

import java.util.List;

public interface TagService {
    Tag create(Tag tag) throws EntityAlreadyExistException;
    List<Tag> getAll(int page, int size);
    Tag getTagById(long id) throws NotFoundEntityException;
    void deleteTagById(long id) throws NotFoundEntityException;
    MostUsedTag getMostUsedTagByUserId(long id);
}
