package com.epam.esm.controller;

import com.epam.esm.link.GiftCertificateLinkBuilder;
import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.model.dto.convert.GiftCertificateConverter;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.service.logic.certificate.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.epam.esm.service.logic.certificate.GiftCertificateServiceImpl;

import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/certificates")
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;
    private final GiftCertificateLinkBuilder certificateLinkBuilder;
    private final GiftCertificateConverter certificateConverter;


    @Autowired
    public GiftCertificateController(GiftCertificateServiceImpl giftCertificateService,
                                     GiftCertificateLinkBuilder certificateLinkBuilder,
                                     GiftCertificateConverter certificateConverter){
        this.giftCertificateService =  giftCertificateService;
        this.certificateLinkBuilder = certificateLinkBuilder;
        this.certificateConverter = certificateConverter;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('certificate:create')")
    public GiftCertificateDto create(@RequestBody @Validated GiftCertificateDto certificateDto){
        GiftCertificate certificate = certificateConverter.convertToEntity(certificateDto);
        certificate = giftCertificateService.create(certificate);
        GiftCertificateDto resultCertificateDto = certificateConverter.convertToDto(certificate);
        certificateLinkBuilder.addCertificateTodoLinks(resultCertificateDto);
        return resultCertificateDto;
    }

    @PostMapping("/with-tags")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('certificate:create')")
    public GiftCertificateDto createWithTags(@RequestBody @Validated GiftCertificateDto certificateDto){
        GiftCertificate certificate = certificateConverter.convertToEntity(certificateDto);
        certificate = giftCertificateService.createWithTags(certificate);
        GiftCertificateDto resultCertificateDto = certificateConverter.convertToDto(certificate);
        Set<TagDto> tags = resultCertificateDto.getTags();
        certificateLinkBuilder.addCertificateRelatedLinks(resultCertificateDto, tags);
        return resultCertificateDto;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificateDto getById(@PathVariable long id){
        GiftCertificate certificate = giftCertificateService.getById(id);

        GiftCertificateDto certificateDto = certificateConverter.convertToDto(certificate);
        Set<TagDto> tags = certificateDto.getTags();
        certificateLinkBuilder.addCertificateRelatedLinks(certificateDto, tags);
        return certificateDto;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<GiftCertificateDto> getAll(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size){
        List<GiftCertificate> certificates = giftCertificateService.getAll(page, size);
        List<GiftCertificateDto> certificateDtos = new ArrayList<>();
        for(GiftCertificate certificate : certificates){
            certificate.getTags().clear();
            certificateDtos.add(certificateConverter.convertToDto(certificate));
        }

        return certificateLinkBuilder.addCertificatesRelatedLinksWithoutTags(certificateDtos, page, size);
    }

    @GetMapping(value = "/with-tags", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<GiftCertificateDto> getAllWithTags(@RequestParam(name = "tag_name", required = false) List<String> tagsName,
                                                   @RequestParam(name = "sort_param", required = false) List<String> sortParams,
                                                   @RequestParam(name = "sort_type", required = false) List<String> sortTypes,
                                                   @RequestParam(name = "search_param", required = false) String searchParam,
                                                   @RequestParam(name = "search_info", required = false) String searchInfo,
                                                   @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                   @RequestParam(value = "size", defaultValue = "20", required = false) int size){
        List<GiftCertificate> certificates;
        certificates = giftCertificateService.getAllWithTags(tagsName, sortParams, sortTypes, searchParam, searchInfo, page, size);

        List<GiftCertificateDto> certificateDtos = new ArrayList<>();
        for(GiftCertificate certificate : certificates){
            certificateDtos.add(certificateConverter.convertToDto(certificate));
        }
        return certificateLinkBuilder.addCertificatesRelatedLinks(certificateDtos, page, size);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('certificate:update')")
    public GiftCertificateDto updateCertificate(@PathVariable("id") long id, @RequestBody GiftCertificateDto certificateDto){
        GiftCertificate certificate = certificateConverter.convertToEntity(certificateDto);
        certificate = giftCertificateService.updateCertificateById(id, certificate);
        GiftCertificateDto resultCertificateDto = certificateConverter.convertToDto(certificate);
        System.out.println(resultCertificateDto.getTags().iterator().next().getId());
        certificateLinkBuilder.addCertificateTodoLinks(resultCertificateDto);
        resultCertificateDto.getTags().clear();
        Link selfLink = linkTo(methodOn(GiftCertificateController.class).updateCertificate(id, certificateDto)).withSelfRel();
        resultCertificateDto.add(selfLink);
        return resultCertificateDto;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('certificate:delete')")
    public CollectionModel<Void> deleteCertificate(@PathVariable("id") long id){
        giftCertificateService.deleteCertificateById(id);
        return CollectionModel.empty();
    }

}
