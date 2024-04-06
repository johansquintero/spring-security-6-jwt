package com.springsecurity.security.persistence.crud;

import com.springsecurity.security.persistence.entities.RoleEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IRoleCrudRepository extends CrudRepository<RoleEntity, Long> {
    List<RoleEntity> getRolesEntitiesByRoleEnumIn(List<String> roleNames);
}
