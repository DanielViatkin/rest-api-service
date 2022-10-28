package com.epam.esm.service;

import com.epam.esm.model.dto.TagDto;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.repository.impl.GiftCertificateRepositoryImpl;
import com.epam.esm.repository.impl.TagRepositoryImpl;
import com.epam.esm.repository.sort.SortParams;
import com.epam.esm.service.exception.*;
import com.epam.esm.service.logic.certificate.GiftCertificateServiceImpl;
import com.epam.esm.service.validator.Validator;
import com.epam.esm.service.validator.impl.GiftCertificateValidator;
import com.epam.esm.service.validator.impl.ParamsValidator;
import com.epam.esm.service.validator.impl.TagValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class GiftCertificateServiceTest {
    private static final long ID = 1;
    private static final GiftCertificate GIFT_CERTIFICATE = new GiftCertificate(ID, "name", "description", BigDecimal.ONE, 1, Instant.now(), Instant.now());
    private static final Set<TagDto> TAGS_DTO =
            new HashSet<>(Arrays.asList(new TagDto(1L, "first"), new TagDto (2L, "second")));
    private static final Set<Tag> TAGS =
            new HashSet<>(Arrays.asList(new Tag(1L,new HashSet<>(), "first"), new Tag (2L, new HashSet<>(), "second")));
    private GiftCertificateValidator certificateValidator;
    private GiftCertificateRepositoryImpl certificateRepository;
    private TagRepositoryImpl tagRepository;
    private Validator<Tag> tagValidator;
    private GiftCertificateServiceImpl certificateService;
    private ParamsValidator paramsValidator;
    private Tag oneTag = new Tag(3L, new HashSet<>(), "third");;
    private Pageable pagebale;


    @BeforeEach
    public void init() {
        certificateRepository = Mockito.mock(GiftCertificateRepositoryImpl.class);
        certificateValidator = Mockito.mock(GiftCertificateValidator.class);
        tagRepository = Mockito.mock(TagRepositoryImpl.class);
        tagValidator = Mockito.mock(TagValidator.class);
        certificateService = new GiftCertificateServiceImpl(certificateRepository, tagRepository,
                certificateValidator, tagValidator);
        paramsValidator = Mockito.mock(ParamsValidator.class);
        pagebale = PageRequest.of(0,10);
    }

    @Test
    public void testCreateWhenValidAndUnique(){
        when(certificateValidator.isValid(any())).thenReturn(true);
        when(certificateRepository.findByName(anyString())).thenReturn(Optional.empty());
        certificateService.create(GIFT_CERTIFICATE);
        verify(certificateRepository).create(GIFT_CERTIFICATE);
    }

    @Test
    public void testCreateThrowInvalidCertificateExceptionWhenNotValid(){
        when(certificateValidator.isValid(any())).thenReturn(false);
        assertThrows(InvalidCertificateException.class, () -> certificateService.create(GIFT_CERTIFICATE));
    }

    @Test
    public void testCreateShouldNotThrowInvalidTagExceptionWhenNotValid(){
        when(certificateValidator.isValid(any())).thenReturn(true);
        when(tagValidator.isValid(any())).thenReturn(true);
        GIFT_CERTIFICATE.setTags(TAGS);
        assertDoesNotThrow(() -> certificateService.createWithTags(GIFT_CERTIFICATE));
    }

    @Test
    public void testCreateShouldThrowEntityAlreadyExistExceptionWhenValidAndExist(){
        when(certificateValidator.isValid(any())).thenReturn(true);
        when(certificateRepository.findByName(anyString())).thenReturn(Optional.of(GIFT_CERTIFICATE));
        assertThrows(EntityAlreadyExistException.class, () -> certificateService.create(GIFT_CERTIFICATE));
    }

    @Test
    public void testGetByIdShouldReturnWhenFind(){
        when(certificateRepository.findById(ID)).thenReturn(Optional.of(GIFT_CERTIFICATE));
        certificateService.getById(ID);
        verify(certificateRepository).findById(ID);
    }

    @Test
    public void testGetByIdShouldThrowNotFoundException(){
        when(certificateRepository.findById(ID+1)).thenReturn(Optional.empty());
        assertThrows(NotFoundEntityException.class, () -> certificateService.getById(ID+1));
    }

    @Test
    public void testGetAllShouldGetAll(){
        certificateService.getAll(0,10);
        verify(certificateRepository).findAll(pagebale);
    }

    @Test
    public void testGetAllShouldGetAllShouldThrowInvalidPageDataException(){
        assertThrows(InvalidPagebaleParametersException.class, () -> certificateService.getAll(0,0));
    }

    @Test
    public void testGetAllWithTagsShouldReturnAllSortedSearchedWithTagWhenAllComponentExist(){
        when(paramsValidator.isSortParamsValid(any())).thenReturn(true);
        when(paramsValidator.isSortTypesValid(any())).thenReturn(true);
        when(paramsValidator.isSearchParamsValid(any())).thenReturn(true);
        when(tagRepository.findByName(anyString())).thenReturn(Optional.of(oneTag));
        certificateService.getAllWithTags(Arrays.asList("me"),
                Arrays.asList("name"), Arrays.asList("asc"), "name", "asf", pagebale.getPageNumber(), pagebale.getPageSize());
        verify(certificateRepository).getCertificateByTag(any(), any(), anyString(), anyString(), any());
    }

    @Test
    public void testGetAllWithTagsShouldReturnAllWithWhenNoTagNameAndSortParamAndSearchParam(){
        certificateService.getAllWithTags(null, null,
                null, null, null, pagebale.getPageNumber(), pagebale.getPageSize());
        verify(certificateRepository).findAll(pagebale);
    }

    @Test
    public void testGetAllWitTagsShouldReturnAllSortingWhenSortParamsExist(){
        when(paramsValidator.isSortParamsValid(any())).thenReturn(true);
        when(paramsValidator.isSortTypesValid(any())).thenReturn(true);
        when(paramsValidator.isSearchParamsValid(any())).thenReturn(true);
        certificateService.getAllWithTags(null, Arrays.asList("name"), Arrays.asList("asc"),
                null, null, pagebale.getPageNumber(), pagebale.getPageSize());
        verify(certificateRepository).getCertificateByTag(nullable(List.class), any(), nullable(String.class), nullable(String.class), any());
    }

    @Test
    public void testGetAllWitTagsShouldReturnAllSearchingWhenSearchParamExist(){
        when(paramsValidator.isSortParamsValid(any())).thenReturn(true);
        when(paramsValidator.isSortTypesValid(any())).thenReturn(true);
        when(paramsValidator.isSearchParamsValid(any())).thenReturn(true);
        certificateService.getAllWithTags(null, null, null,
                "name", "asf", pagebale.getPageNumber(), pagebale.getPageSize());
        verify(certificateRepository).getCertificateByTag(nullable(List.class), nullable(SortParams.class),anyString(), anyString(), any());
    }

    @Test
    public void testGetAllWitTagsShouldReturnAllByTagWhenTagExist(){
        when(paramsValidator.isSortParamsValid(any())).thenReturn(true);
        when(paramsValidator.isSortTypesValid(any())).thenReturn(true);
        when(paramsValidator.isSearchParamsValid(any())).thenReturn(true);
        when(tagRepository.findByName(anyString())).thenReturn(Optional.of(oneTag));
        certificateService.getAllWithTags(Arrays.asList("first"), null, null,
                null, null, pagebale.getPageNumber(), pagebale.getPageSize());
        verify(certificateRepository).getCertificateByTag(anyList(), nullable(SortParams.class), nullable(String.class),
                nullable(String.class), any());
    }

    @Test
    public void testGetAllWitTagsShouldReturnAllByTagSortingWhenTagAndSortParamsExists(){
        when(paramsValidator.isSortParamsValid(any())).thenReturn(true);
        when(paramsValidator.isSortTypesValid(any())).thenReturn(true);
        when(paramsValidator.isSearchParamsValid(any())).thenReturn(true);
        when(tagRepository.findByName(anyString())).thenReturn(Optional.of(oneTag));
        certificateService.getAllWithTags(Arrays.asList("first"), Arrays.asList("name"), Arrays.asList("asc"),
                null, null, pagebale.getPageNumber(), pagebale.getPageSize());
        verify(certificateRepository).getCertificateByTag(anyList(), any(), nullable(String.class), nullable(String.class), any());
    }

    @Test
    public void testGetAllWitTagsShouldReturnAllByTagSearchingWhenTagAndSearchParamsExists(){
        when(paramsValidator.isSortParamsValid(any())).thenReturn(true);
        when(paramsValidator.isSortTypesValid(any())).thenReturn(true);
        when(paramsValidator.isSearchParamsValid(any())).thenReturn(true);
        when(tagRepository.findByName(anyString())).thenReturn(Optional.of(oneTag));
        certificateService.getAllWithTags(Arrays.asList("first"), null, null,
                "name", "name", pagebale.getPageNumber(), pagebale.getPageSize());
        verify(certificateRepository).getCertificateByTag(anyList(), nullable(SortParams.class), anyString(), anyString(), any());
    }

    @Test
    public void testGetAllWitTagsShouldReturnAllSortingAndSearchingWhenSortParamsAndSearchParamsExists(){
        when(paramsValidator.isSortParamsValid(any())).thenReturn(true);
        when(paramsValidator.isSortTypesValid(any())).thenReturn(true);
        when(paramsValidator.isSearchParamsValid(any())).thenReturn(true);
        certificateService.getAllWithTags(null, Arrays.asList("name"), Arrays.asList("asc"),
                "name", "name", pagebale.getPageNumber(), pagebale.getPageSize());
        verify(certificateRepository).getCertificateByTag(nullable(List.class), any(), anyString(), anyString(), any());
    }

    @Test
    public void testGetAllWithTagsShouldThrowInvalidTagExceptionWhenTagInvalid(){
        when(tagRepository.findByName(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundEntityException.class, () -> certificateService.getAllWithTags(Arrays.asList("1"),null,
                null,null,null, pagebale.getPageNumber(), pagebale.getPageSize()));
    }

    @Test
    public void testUpdateByIdShouldThrowNotFoundException(){
        when(certificateRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundEntityException.class, () -> certificateService.updateCertificateById(ID+100, GIFT_CERTIFICATE));
    }

    @Test
    public void testUpdateByIdShouldThrowInvalidTagException(){
        when(certificateRepository.findById(anyLong())).thenReturn(Optional.of(GIFT_CERTIFICATE));
        when(certificateValidator.isValidName(anyString())).thenReturn(true);
        when(certificateValidator.isValidDescription(anyString())).thenReturn(true);
        when(certificateValidator.isValidPrice(any())).thenReturn(true);
        when(certificateValidator.isValidDuration(anyInt())).thenReturn(true);
        when(tagValidator.isValid(any())).thenReturn(false);
        GIFT_CERTIFICATE.setTags(TAGS);
        assertThrows(InvalidTagException.class, () -> certificateService.updateCertificateById(ID+100, GIFT_CERTIFICATE));
    }

    @Test
    public void testDeleteByIdShouldDelete(){
        when(certificateRepository.findById(anyLong())).thenReturn(Optional.of(GIFT_CERTIFICATE));
        certificateService.deleteCertificateById(ID);
        verify(certificateRepository).delete(any());
    }

    @Test
    public void testDeleteByIdShouldThrowNotFoundExceptionWhenWrongId(){
        when(certificateRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundEntityException.class, () -> certificateService.deleteCertificateById(anyLong()));
    }

}
