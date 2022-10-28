package com.epam.esm.service.logic.tag;

import com.epam.esm.model.MostUsedTag;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.entity.User;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.impl.TagRepositoryImpl;
import com.epam.esm.repository.impl.UserRepositoryImpl;
import com.epam.esm.service.exception.*;
import com.epam.esm.service.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final Validator<Tag> tagValidator;

    @Autowired
    public TagServiceImpl(TagRepositoryImpl tagRepository,
                          UserRepositoryImpl userRepository,
                          Validator<Tag> tagValidator) {
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.tagValidator = tagValidator;
    }

    @Override
    @Transactional
    public Tag create(Tag tag) {
        tagRepository.findByName(tag.getName()).ifPresent(s-> {throw new EntityAlreadyExistException("tag.already.exist");});
        if(!tagValidator.isValid(tag)){
            throw new InvalidTagException("tag.invalid.name", tag.getName());
        }
        return tagRepository.create(tag);
    }

    @Override
    public List<Tag> getAll(int page, int size) {
        Pageable pageable = getPageble(page, size);
        return tagRepository.findAll(pageable);
    }

    private Pageable getPageble(int page, int size){
        Pageable pageble;
        try {
            pageble = PageRequest.of(page, size);
        } catch (IllegalArgumentException e) {
            throw new InvalidPagebaleParametersException("pageable.invalid.data",e);
        }
        return pageble;
    }

    @Override
    public Tag getTagById(long id) {
        return tagRepository.findById(id).orElseThrow(()-> new NotFoundEntityException("tag.not.found", id));
    }

    @Override
    public void deleteTagById(long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(()-> new NotFoundEntityException("tag.not.found", id));
        tagRepository.delete(tag);
    }

    @Override
    public MostUsedTag getMostUsedTagByUserId(long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new NotFoundEntityException("user.not.found", userId));
        isSelfCheck(user.getLogin());
        return tagRepository.findMostUsedTagWithHightCoastByUserId(userId).orElseThrow(() -> new NotFoundEntityException("tag.not.found", userId));
    }

    private void isSelfCheck(String  userDbLogin){
        String loginFromJwt = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!loginFromJwt.equals(userDbLogin)){
            throw new NoAuthoritiesException("no.self.check", "40301");
        }
    }
}
