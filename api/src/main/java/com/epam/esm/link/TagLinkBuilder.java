package com.epam.esm.link;

import com.epam.esm.controller.TagController;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.model.entity.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.List;
import java.util.Set;

@Component
public class TagLinkBuilder {
    public void addTagToDoLinks(TagDto tag){
        Link createLink = linkTo(methodOn(TagController.class).create(tag)).withRel("create");
        createLink.expand("POST").withRel("method");
        Link deleteLink = linkTo(methodOn(TagController.class).deleteTagById(tag.getId())).withRel("delete");
        Link getLink = linkTo(methodOn(TagController.class).getTagById(tag.getId())).withRel("get");
        tag.add(createLink);
        tag.add(deleteLink);
        tag.add(getLink);
    }

    public void addTagRelatedLinks(TagDto tag){
        addTagToDoLinks(tag);
    }

    public void addTagsRelatedLinks(Set<TagDto> tags){
        for (TagDto tag : tags){
            addTagRelatedLinks(tag);
        }
    }
}
