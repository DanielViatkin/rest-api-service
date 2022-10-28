package com.epam.esm.service.logic.user;

import com.epam.esm.model.entity.User;
import com.epam.esm.service.exception.InvalidUserPassword;

import java.util.List;

public interface UserService {
    User getById(long id);
    List<User> getAll(int page, int size);
    User getByLogin(String login);
    User create(User user);
    void changePassword(String login, String currentPassword, String newPassword) throws InvalidUserPassword;
}
