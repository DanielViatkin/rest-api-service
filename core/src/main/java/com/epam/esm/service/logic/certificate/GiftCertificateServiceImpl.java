package com.epam.esm.service.logic.certificate;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.impl.GiftCertificateRepositoryImpl;
import com.epam.esm.repository.impl.TagRepositoryImpl;
import com.epam.esm.repository.sort.SortParams;
import com.epam.esm.service.exception.*;
import com.epam.esm.service.validator.Validator;
import com.epam.esm.service.validator.impl.GiftCertificateValidator;
import com.epam.esm.service.validator.impl.ParamsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.time.Instant;
import java.util.*;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateRepository certificateRepository;
    private final TagRepository tagRepository;
    private final Validator<GiftCertificate> giftCertificateValidator;
    private final Validator<Tag> tagValidator;


    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepositoryImpl certificateRepository,
                                      TagRepositoryImpl tagRepository,
                                      Validator<GiftCertificate> giftCertificateValidator,
                                      Validator<Tag> tagValidator) {
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
        this.giftCertificateValidator = giftCertificateValidator;
        this.tagValidator = tagValidator;
    }

    @Override
    @Transactional
    public GiftCertificate create(GiftCertificate giftCertificate){
        validateGiftCertificate(giftCertificate);
        String certificateName = giftCertificate.getName();
        certificateRepository.findByName(certificateName).ifPresent(s -> {throw new EntityAlreadyExistException("certificate.already.exist");});
        return createCertificate(giftCertificate);
    }

    @Override
    @Transactional
    public GiftCertificate createWithTags(GiftCertificate giftCertificate){
        Set<Tag> tags = giftCertificate.getTags();
        validateGiftCertificate(giftCertificate);
        validateTags(tags);
        String certificateName = giftCertificate.getName();
        certificateRepository.findByName(certificateName).ifPresent(s -> {throw new EntityAlreadyExistException("certificate.already.exist");});
        addTags(tags, giftCertificate);

        createCertificate(giftCertificate);
        return giftCertificate;
    }

    private void validateGiftCertificate(GiftCertificate giftCertificate) {
        if(!giftCertificateValidator.isValid(giftCertificate)){
            throw new InvalidCertificateException("certificate.invalid.data", "");
        }
    }

    private void validateTags(Set<Tag> tags){
        tags.forEach(tag -> {
            if(!tagValidator.isValid(tag)){
                throw new InvalidTagException("tag.invalid.name", tag.getName());
            }
        });
    }

    private GiftCertificate createCertificate(GiftCertificate giftCertificate) {
        Instant time = Instant.now();
        giftCertificate.setCreateDate(time);
        giftCertificate.setLastUpdateDate(time);
        return certificateRepository.create(giftCertificate);
    }

    private void addTags(Set<Tag> tags,  GiftCertificate certificate){
        Set<Tag> iteratedTags = new HashSet<>(tags);
        for (Tag tag : iteratedTags) {
            Optional<Tag> tagFromDB = tagRepository.findByName(tag.getName());
            if(tagFromDB.isPresent()) {
                certificate.removeTag(tag);
                certificate.addTag(tagFromDB.get());
            }
        }
    }

    @Override
    public GiftCertificate getById(long id) {
        return certificateRepository.findById(id).orElseThrow(()-> new NotFoundEntityException("certificate.not.found", id));
    }

    @Override
    public List<GiftCertificate> getAll(int page, int size) {
        Pageable pageble = getPageble(page, size);
        return certificateRepository.findAll(pageble);
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
    @Transactional
    public List<GiftCertificate> getAllWithTags(List<String> tagsName, List<String> sortParams,
                                                List<String> sortTypes, String searchParam,
                                                String searchInfo, int page, int size) {
        if (tagsName == null && sortParams == null &&
                sortTypes == null && searchInfo == null &&
                searchParam == null){
            return getAll(page, size);
        }
        Pageable pageable = getPageble(page, size);

        SortParams sortParamsMap = null;
        if (sortParams != null && sortTypes != null){
            sortParamsMap = new SortParams(sortParams, sortTypes);
        }
        if (tagsName != null){
            tagsName.stream().forEach(name -> tagRepository.findByName(name).orElseThrow(() -> new NotFoundEntityException("tag.not.found.name",name)));
        }

        validateAllParams(sortParams, sortTypes, searchParam);
        List<GiftCertificate> giftCertificates = certificateRepository.getCertificateByTag(tagsName, sortParamsMap, searchParam, searchInfo, pageable);
        return giftCertificates;
    }

    private void validateAllParams(List<String> sortParams,
                                   List<String> sortTypes, String searchParam){
        ParamsValidator paramsValidator = new ParamsValidator();
        if (!paramsValidator.isSortParamsValid(sortParams)){
            throw new InvalidParamException("param.invalid.sort.param");
        }
        if (!paramsValidator.isSortTypesValid(sortTypes)){
            throw new InvalidParamException("param.invalid.sort.type");
        }
        if (!paramsValidator.isSearchParamsValid(searchParam)){
            throw new InvalidParamException("param.invalid.search.param");
        }
    }

    @Override
    public void deleteCertificateById(long id) {
        GiftCertificate certificate = certificateRepository.findById(id).orElseThrow(() -> new NotFoundEntityException("certificate.not.found", id));
        certificateRepository.delete(certificate);
    }

    @Override
    @Transactional
    public GiftCertificate updateCertificateById(long certificateId, GiftCertificate giftCertificate) {
        GiftCertificate certificateFromDb = certificateRepository.findById(certificateId).
                orElseThrow(() -> new NotFoundEntityException("certificate.not.found", certificateId));
        fillUpdateFields(giftCertificate, certificateFromDb);

        return certificateRepository.update(certificateFromDb);
    }

    private void fillUpdateFields(GiftCertificate frontCertificate, GiftCertificate updatedCertificate){
        GiftCertificateValidator giftCertificateValidator = new GiftCertificateValidator();

        Optional.ofNullable(frontCertificate.getName()).
                ifPresent(certificateName -> {
                    if(!giftCertificateValidator.isValidName(certificateName)){
                        throw new InvalidCertificateException("certificate.invalid.name", certificateName);
                    }
                    updatedCertificate.setName(certificateName);
                });

        Optional.ofNullable(frontCertificate.getDescription()).
                ifPresent(description -> {
                    if(!giftCertificateValidator.isValidDescription(description)){
                        throw new InvalidCertificateException("certificate.invalid.description", description);
                    }
                    updatedCertificate.setDescription(description);
                });

        Optional.ofNullable(frontCertificate.getPrice()).
                ifPresent(price -> {
                    if(!giftCertificateValidator.isValidPrice(price)){
                        throw new InvalidCertificateException("certificate.invalid.price", price);
                    }
                    updatedCertificate.setPrice(price);
                });

        Optional.ofNullable(frontCertificate.getDuration()).
                ifPresent(duration -> {
                    if(!giftCertificateValidator.isValidDuration(duration)){
                        throw new InvalidCertificateException("certificate.invalid.duration", duration);
                    }
                    updatedCertificate.setDuration(duration);
                });

        if (frontCertificate.getTags() != null && !frontCertificate.getTags().isEmpty()){
            Set<Tag> tags = frontCertificate.getTags();
            validateTags(tags);
            for(Tag tag : tags) {
                Optional<Tag> tagFromDB = tagRepository.findByName(tag.getName());
                if (tagFromDB.isPresent()) {
                    updatedCertificate.addTag(tagFromDB.get());
                } else {
                    Tag createdTag = tagRepository.create(tag);
                    updatedCertificate.addTag(createdTag);
                }
            }
        }

        updatedCertificate.setLastUpdateDate(Instant.now());
    }

}
