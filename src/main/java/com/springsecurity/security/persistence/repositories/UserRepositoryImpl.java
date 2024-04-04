package com.springsecurity.security.persistence.repositories;

import com.springsecurity.security.domain.repositories.IUserRepository;
import com.springsecurity.security.persistence.crud.IUserCrudRepository;
import com.springsecurity.security.persistence.entities.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements IUserRepository {
    private final IUserCrudRepository crud;

    @Override
    public Iterable<UserEntity> findAll() {
        return this.crud.findAll();
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return this.crud.findById(id);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return this.crud.findByUsername(username);
    }

    @Override
    public UserEntity save(UserEntity newUser) {
        return this.crud.save(newUser);
    }

    @Override
    public void delete(Long id) {
        this.crud.deleteById(id);
    }

    @Override
    public Iterable<UserEntity> saveAll(Iterable<UserEntity> users) {
        return crud.saveAll(users);
    }
}
