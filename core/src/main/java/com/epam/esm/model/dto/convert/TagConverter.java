package com.epam.esm.model.dto.convert;

import com.epam.esm.model.dto.TagDto;
import com.epam.esm.model.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagConverter implements DtoConvert<Tag, TagDto>{

    @Override
    public Tag convertToEntity(TagDto dto) {
        Tag tag = new Tag();
        tag.setId(dto.getId());
        tag.setName(dto.getName());
        return tag;
    }

    @Override
    public TagDto convertToDto(Tag entity) {
        TagDto tagDto = new TagDto();
        tagDto.setId(entity.getId());
        tagDto.setName(entity.getName());
        return tagDto;
    }
}
