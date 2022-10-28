package com.epam.esm.service.validator.impl;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.validator.Validator;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TagValidator implements Validator<Tag> {
    private static final int MAX_TAG_LENGTH = 45;
    private static final int MIN_TAG_LENGTH = 3;

    @Override
    public boolean isValid(Tag tag) {
        String tagName = tag.getName();
        return !(tagName.length() > MAX_TAG_LENGTH ||
                tagName.length() < MIN_TAG_LENGTH);
    }
}
