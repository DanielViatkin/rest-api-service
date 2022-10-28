package com.epam.esm.service.logic.certificate;

import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.service.exception.EntityAlreadyExistException;
import com.epam.esm.service.exception.InvalidCertificateException;
import com.epam.esm.service.exception.InvalidTagException;
import com.epam.esm.service.exception.NotFoundEntityException;

import java.util.List;

public interface GiftCertificateService {
    GiftCertificate createWithTags(GiftCertificate giftCertificate) throws EntityAlreadyExistException, InvalidCertificateException;
    GiftCertificate create(GiftCertificate giftCertificate) throws EntityAlreadyExistException, InvalidCertificateException, InvalidTagException;
    GiftCertificate getById(long id) throws NotFoundEntityException;
    List<GiftCertificate> getAll(int page, int size);
    List<GiftCertificate> getAllWithTags(List<String> tagsName, List<String> sortParams,
                                         List<String> sortTypes, String searchParam, String searchInfo,
                                         int page, int size) throws
            InvalidTagException;
    GiftCertificate updateCertificateById(long id, GiftCertificate giftCertificate) throws InvalidTagException, NotFoundEntityException;
    void deleteCertificateById(long id) throws NotFoundEntityException;
}
