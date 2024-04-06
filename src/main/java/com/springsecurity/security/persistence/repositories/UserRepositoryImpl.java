package com.springsecurity.security.persistence.repositories;

import com.springsecurity.security.domain.repositories.IUserRepository;
import com.springsecurity.security.persistence.crud.IUserCrudRepository;
import com.springsecurity.security.persistence.entities.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements IUserRepository {
    private final IUserCrudRepository crud;

    @Transactional(readOnly = true)
    @Override
    public List<UserEntity> findAll() {
        return (List<UserEntity>) this.crud.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UserEntity> findById(Long id) {
        return this.crud.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return this.crud.findByUsername(username);
    }

    @Transactional
    @Override
    public UserEntity save(UserEntity newUser) {
        return this.crud.save(newUser);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        this.crud.deleteById(id);
    }

    @Transactional
    @Override
    public Iterable<UserEntity> saveAll(Iterable<UserEntity> users) {
        return crud.saveAll(users);
    }
}
