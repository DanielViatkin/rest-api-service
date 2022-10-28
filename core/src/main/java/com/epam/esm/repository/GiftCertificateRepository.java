package com.epam.esm.repository;


import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.repository.sort.SortParams;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateRepository extends AbstractRepository<GiftCertificate>{
    Optional<GiftCertificate> findByName(String name);
    List<GiftCertificate> getCertificateByTag(List<String> tags, SortParams sortParamsMap, String searchParam, String searchInfo, Pageable pageable);
}
