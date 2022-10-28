package com.epam.esm.model.dto.convert;

import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class GiftCertificateConverter implements DtoConvert<GiftCertificate, GiftCertificateDto> {
    private final TagConverter tagConverter;

    @Autowired
    public GiftCertificateConverter(TagConverter tagConverter){
        this.tagConverter = tagConverter;
    }

    @Override
    public GiftCertificate convertToEntity(GiftCertificateDto dto) {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(dto.getId());
        certificate.setName(dto.getName());
        certificate.setDescription(dto.getDescription());
        certificate.setPrice(dto.getPrice());
        certificate.setDuration(dto.getDuration());
        certificate.setCreateDate(dto.getCreateDate());
        certificate.setLastUpdateDate(dto.getLastUpdateDate());
        if (dto.getTags() != null){
            Set<Tag> tags = new HashSet<>();
            for (TagDto tagDto : dto.getTags()){
                tags.add(tagConverter.convertToEntity(tagDto));
            }
            certificate.setTags(tags);
        }
        return certificate;
    }

    @Override
    public GiftCertificateDto convertToDto(GiftCertificate entity) {
        GiftCertificateDto certificateDto = new GiftCertificateDto();
        certificateDto.setId(entity.getId());
        certificateDto.setName(entity.getName());
        certificateDto.setDescription(entity.getDescription());
        certificateDto.setPrice(entity.getPrice());
        certificateDto.setDuration(entity.getDuration());
        certificateDto.setCreateDate(entity.getCreateDate());
        certificateDto.setLastUpdateDate(entity.getLastUpdateDate());
        if (entity.getTags() != null){
            Set<TagDto> tagDtos = new HashSet<>();
            for (Tag tag : entity.getTags()){
                tagDtos.add(tagConverter.convertToDto(tag));
            }
            certificateDto.setTags(tagDtos);
        }
        return certificateDto;
    }
}
