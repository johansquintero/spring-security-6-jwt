package com.springsecurity.security.persistence.crud;

import com.springsecurity.security.persistence.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IUserCrudRepository extends CrudRepository<UserEntity , Long> {
    Optional<UserEntity> findByUsername(String username);
}
