package com.epam.esm.service.validator.impl;

import com.epam.esm.model.entity.User;
import com.epam.esm.service.validator.Validator;
import org.springframework.stereotype.Component;

@Component
public class UserValidator implements Validator<User> {
    private static final int MIN_LOGIN_LENGTH = 6;
    private static final int MAX_LOGIN_LENGTH = 45;

    @Override
    public boolean isValid(User entity) {
        String login = entity.getLogin();
        return !(login.length() < MIN_LOGIN_LENGTH || login.length() > MAX_LOGIN_LENGTH);
    }
}
