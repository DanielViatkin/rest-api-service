package com.epam.esm.repository;

import com.epam.esm.model.entity.User;

import java.util.Optional;

public interface UserRepository extends AbstractRepository<User>{
    Optional<User> findByLogin(String login);
}
