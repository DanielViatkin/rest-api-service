package com.epam.esm.repository;

import com.epam.esm.config.JpaEmbeddedDBConfig;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.repository.impl.GiftCertificateRepositoryImpl;
import com.epam.esm.repository.sort.SortParams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JpaEmbeddedDBConfig.class)
@Transactional
public class GiftCertificateRepositoryTest {

//    @PersistenceContext
//    private EntityManager entityManager;

    private static final Tag FIRST_TAG = new Tag(1L, new HashSet<>(), "tag 1");
    private static final Tag SECOND_TAG = new Tag(2L, new HashSet<>(), "tag 2");
    private static final Tag THIRD_TAG = new Tag(3L, new HashSet<>(), "tag 3");

    private static GiftCertificate CERTIFICATE_TO_CREATE = new GiftCertificate(
            "certificate 4", "description 4", new BigDecimal("4.4"), 4,
            Instant.now(), Instant.now());

    private static final GiftCertificate CERTIFICATE_TO_UPDATE = new GiftCertificate(
            1L, "cccc", "description 1", new BigDecimal("328.00"), 1,
            Instant.parse("2021-01-01T01:11:10.00Z"),
            Instant.parse("2021-01-01T01:11:11.00Z"));
    private static final GiftCertificate FIRST_CERTIFICATE = new GiftCertificate(
            1L, "certificate 1", "description 1", new BigDecimal("1.10"), 1,
            Instant.parse("2021-01-01T01:11:10.00Z"),
            Instant.parse("2021-01-01T01:11:11.00Z"));
    private static final GiftCertificate SECOND_CERTIFICATE = new GiftCertificate(
            2L, "certificate 2", "description 2", new BigDecimal("2.20"), 2,
            Instant.parse("2022-02-02T02:22:20.00Z"),
            Instant.parse("2022-02-02T02:22:22.00Z"));
    private static final GiftCertificate THIRD_CERTIFICATE = new GiftCertificate(
            3L, "certificate 3", "description 3", new BigDecimal("3.30"), 3,
            Instant.parse("2023-03-03T03:33:30.00Z"),
            Instant.parse("2023-03-03T03:33:31.00Z"));
    @Autowired
    private final GiftCertificateRepository certificateRepository;

    @Autowired
    public GiftCertificateRepositoryTest(GiftCertificateRepositoryImpl certificateRepository){
        this.certificateRepository = certificateRepository;
        FIRST_CERTIFICATE.setTags(new HashSet<>(Arrays.asList(FIRST_TAG, SECOND_TAG, THIRD_TAG)));
        SECOND_CERTIFICATE.setTags(new HashSet<>(Arrays.asList(SECOND_TAG)));
        FIRST_TAG.setId(1L);
        SECOND_TAG.setId(2L);
        THIRD_TAG.setId(3L);
    }

    @Test
    public void givenCertificate_whenCreate_thenCertificateExist(){

        certificateRepository.create(CERTIFICATE_TO_CREATE);
        Optional<GiftCertificate> giftCertificate = certificateRepository
                .findByName(CERTIFICATE_TO_CREATE.getName());
        CERTIFICATE_TO_CREATE.setId(giftCertificate.get().getId());
        System.out.println(CERTIFICATE_TO_CREATE.getId());
        Assertions.assertTrue(giftCertificate.isPresent());
    }

//    @Test
//    public void givenCertificateId_whenDelete_thenDelete(){
//
//        certificateRepository.delete(FIRST_CERTIFICATE);
//
//        assertFalse(certificateRepository.findById(FIRST_CERTIFICATE.getId()).isPresent());
//    }

    @Test
    public void givenSortParams_whenGetAllSorted_thenReturnAllSorted(){
        SortParams sortParams = new SortParams(Arrays.asList("price"), Arrays.asList("ASC"));

        List<GiftCertificate> certificates = certificateRepository.getCertificateByTag(new ArrayList<String>(), sortParams,
                null, null, PageRequest.of(0,3));

        Assertions.assertEquals(Arrays.asList(FIRST_CERTIFICATE.getName(), SECOND_CERTIFICATE.getName(),THIRD_CERTIFICATE.getName()),
                Arrays.asList(certificates.get(0).getName(),certificates.get(1).getName(),certificates.get(2).getName()));
    }

    @Test
    public void givenTagId_whenGetAllWithTags_thenReturnByTagName(){

        List<GiftCertificate> certificates = certificateRepository.getCertificateByTag(Arrays.asList(THIRD_TAG.getName()),null,
                null, null, PageRequest.of(0,3));

        Assertions.assertEquals(certificates.stream().findAny().get().getName(),(FIRST_CERTIFICATE.getName()));
    }

    @Test
    public void givenTagIdAndSortParams_whenGetAllWithTags_thenReturnByTagNameSorted(){
        SortParams sortParams = new SortParams(Arrays.asList("price"), Arrays.asList("ASC"));

        List<GiftCertificate> certificates = certificateRepository.getCertificateByTag(Arrays.asList(SECOND_TAG.getName()), sortParams,
                null, null, PageRequest.of(0,2));
        System.out.println(certificates.get(0).getName() + " " + certificates.get(1).getName());
        Assertions.assertEquals(certificates.get(0).getName(), FIRST_CERTIFICATE.getName());
        Assertions.assertEquals(certificates.get(1).getName(), SECOND_CERTIFICATE.getName());
    }

    @Test
    public void givenName_whenGet_thenReturnByName(){

        Optional<GiftCertificate> giftCertificateOptional = certificateRepository.findByName(FIRST_CERTIFICATE.getName());

        Assertions.assertTrue(giftCertificateOptional.isPresent());
        Assertions.assertEquals(giftCertificateOptional.get().getId(), FIRST_CERTIFICATE.getId());
    }

    @Test
    public void givenId_whenGet_thenReturnById(){

        Optional<GiftCertificate> giftCertificateOptional = certificateRepository.findById(FIRST_CERTIFICATE.getId());

        Assertions.assertTrue(giftCertificateOptional.isPresent());
        Assertions.assertEquals(giftCertificateOptional.get().getName(), FIRST_CERTIFICATE.getName());
    }

    @Test
    public void givenIdAndUpdateParams_whenUpdateAndGetById_thenUpdateById(){

        certificateRepository.update(CERTIFICATE_TO_UPDATE);
        Optional<GiftCertificate> giftCertificateOptional = certificateRepository.findById(FIRST_CERTIFICATE.getId());

        Assertions.assertEquals(giftCertificateOptional.get().getPrice(), new BigDecimal("328.00"));
        Assertions.assertEquals(giftCertificateOptional.get().getName(), "cccc");
    }

}
