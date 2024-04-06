package com.springsecurity.security.domain.repositories;

import com.springsecurity.security.persistence.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {
    List<UserEntity> findAll();

    Optional<UserEntity> findById(Long id);

    Optional<UserEntity> findByUsername(String username);

    UserEntity save(UserEntity newUser);

    Iterable<UserEntity> saveAll(Iterable<UserEntity> users);

    void delete(Long id);
}
