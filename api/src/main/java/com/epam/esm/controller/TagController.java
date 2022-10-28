package com.epam.esm.controller;

import com.epam.esm.link.TagLinkBuilder;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.model.dto.convert.TagConverter;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.logic.tag.TagService;
import com.epam.esm.service.logic.tag.TagServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;
    private final TagLinkBuilder tagLinkBuilder;
    private final TagConverter tagConverter;

    @Autowired
    public TagController(TagServiceImpl tagService,
                         TagLinkBuilder tagLinkBuilder,
                         TagConverter tagConverter){
        this.tagService =  tagService;
        this.tagLinkBuilder = tagLinkBuilder;
        this.tagConverter = tagConverter;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('tag:create')")
    public TagDto create(@RequestBody @Validated TagDto tagDto){
        Tag tag = tagConverter.convertToEntity(tagDto);
        tag = tagService.create(tag);
        TagDto resultTagDto = tagConverter.convertToDto(tag);
        tagLinkBuilder.addTagRelatedLinks(resultTagDto);
        Link selfLink = linkTo(TagController.class).slash(tag.getId()).withSelfRel();
        resultTagDto.add(selfLink);
        return resultTagDto;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    public CollectionModel<TagDto> getAll(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                          @RequestParam(value = "size", defaultValue = "20", required = false) int size){
        List<Tag> tags = tagService.getAll(page, size);
        Set<TagDto> tagDtos = new HashSet<>();
        for(Tag tag : tags){
            tagDtos.add(tagConverter.convertToDto(tag));
        }
        tagLinkBuilder.addTagsRelatedLinks(tagDtos);
        Link selfLink = linkTo(TagController.class).withSelfRel();
        return CollectionModel.of(tagDtos, selfLink);
    }

    @GetMapping("/{id}")
    public TagDto getTagById(@PathVariable("id") long id){
        Tag tag = tagService.getTagById(id);
        System.out.println(tag.getCertificates().size());
        TagDto tagDto = tagConverter.convertToDto(tag);
        tagLinkBuilder.addTagRelatedLinks(tagDto);
        Link selfLink = linkTo(TagController.class).slash(tag.getId()).withSelfRel();
        tagDto.add(selfLink);
        return tagDto;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('tag:delete')")
    public CollectionModel<TagDto> deleteTagById(@PathVariable("id") long id){
        tagService.deleteTagById(id);
        return CollectionModel.empty();
    }
}
