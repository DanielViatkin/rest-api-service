package com.epam.esm.link;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.TagController;
import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.security.cert.Certificate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class GiftCertificateLinkBuilder {
    private final TagLinkBuilder tagLinkBuilder;

    @Autowired
    public GiftCertificateLinkBuilder(TagLinkBuilder tagLinkBuilder){
        this.tagLinkBuilder = tagLinkBuilder;
    }

    //TODO нужны ли ссылки на Update, delete, create если они имеют такой же url как и у selfLink
    public void addCertificateTodoLinks(GiftCertificateDto certificate){
        Link updateLink = linkTo(methodOn(GiftCertificateController.class).
                updateCertificate(certificate.getId(), certificate)).withRel("update");
        Link deleteLink = linkTo(methodOn(GiftCertificateController.class).
                deleteCertificate(certificate.getId())).withRel("delete");
        Link readLink = linkTo(methodOn(GiftCertificateController.class).
                getById(certificate.getId())).withRel("get");
        certificate.add(updateLink);
        certificate.add(deleteLink);
        certificate.add(readLink);
    }

    public void addCertificateRelatedLinks(GiftCertificateDto certificate, Set<TagDto> tags){
        for (TagDto tag : tags){
            if (tag.getLinks().isEmpty()) {
                tagLinkBuilder.addTagRelatedLinks(tag);
            }
        }
        addCertificateTodoLinks(certificate);
    }

    public CollectionModel<GiftCertificateDto> addCertificatesRelatedLinks(List<GiftCertificateDto> certificates, int page, int size){
        for (GiftCertificateDto certificate : certificates){
            addCertificateRelatedLinks(certificate, certificate.getTags());
        }
        Link selfLink = linkTo(methodOn(GiftCertificateController.class).getAll(page, size)).withSelfRel();
        return CollectionModel.of(certificates, selfLink);
    }

    public CollectionModel<GiftCertificateDto> addCertificatesRelatedLinksWithoutTags(List<GiftCertificateDto> certificates, int page, int size){
        for (GiftCertificateDto certificate : certificates){
            addCertificateTodoLinks(certificate);
        }
        Link selfLink = linkTo(methodOn(GiftCertificateController.class).getAll(page, size)).withSelfRel();
        CollectionModel<GiftCertificateDto> cm = CollectionModel.of(certificates, selfLink);
        return cm;
    }

}
