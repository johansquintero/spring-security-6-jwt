package com.springsecurity.security;

import com.springsecurity.security.domain.repositories.IUserRepository;
import com.springsecurity.security.domain.usecases.IUserUseCase;
import com.springsecurity.security.persistence.entities.PermissionEntity;
import com.springsecurity.security.persistence.entities.RoleEntity;
import com.springsecurity.security.persistence.entities.RoleEnum;
import com.springsecurity.security.persistence.entities.UserEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }

    @Bean
    CommandLineRunner init(IUserUseCase userService) {
        return args -> {
            //CREATE permissions
            PermissionEntity createPermission = PermissionEntity.builder().name("CREATE").build();
            PermissionEntity readPermission = PermissionEntity.builder().name("READ").build();
            PermissionEntity updatePermission = PermissionEntity.builder().name("UPDATE").build();
            PermissionEntity refactorPermission = PermissionEntity.builder().name("REFACTOR").build();
            PermissionEntity deletePermission = PermissionEntity.builder().name("DELETE").build();

            //CREATE ROLES
            RoleEntity adminRole = RoleEntity.builder()
                    .roleEnum(RoleEnum.ADMIN)
                    .permissions(Set.of(createPermission, readPermission, updatePermission, deletePermission)).build();

            RoleEntity userRole = RoleEntity.builder()
                    .roleEnum(RoleEnum.USER)
                    .permissions(Set.of(createPermission, readPermission)).build();
            RoleEntity invitedRole = RoleEntity.builder()
                    .roleEnum(RoleEnum.INVITED)
                    .permissions(Set.of(readPermission)).build();
            RoleEntity devRole = RoleEntity.builder()
                    .roleEnum(RoleEnum.DEVELOPER)
                    .permissions(Set.of(createPermission, readPermission, updatePermission, deletePermission, refactorPermission)).build();

            //CREATE USER
            UserEntity user1 = UserEntity.builder()
                    .username("johan")
                    .password("12345")
                    .isEnabled(true)
                    .accountNoExpired(true)
                    .accountNoLocked(true)
                    .credentialNoExpired(true)
                    .roles(Set.of(adminRole))
                    .build();

            UserEntity user2 = UserEntity.builder()
                    .username("pepe")
                    .password("12345")
                    .isEnabled(true)
                    .accountNoExpired(true)
                    .accountNoLocked(true)
                    .credentialNoExpired(true)
                    .roles(Set.of(userRole))
                    .build();

            UserEntity user3 = UserEntity.builder()
                    .username("juan")
                    .password("12345")
                    .isEnabled(true)
                    .accountNoExpired(true)
                    .accountNoLocked(true)
                    .credentialNoExpired(true)
                    .roles(Set.of(devRole))
                    .build();
            UserEntity user4 = UserEntity.builder()
                    .username("carlos")
                    .password("12345")
                    .isEnabled(true)
                    .accountNoExpired(true)
                    .accountNoLocked(true)
                    .credentialNoExpired(true)
                    .roles(Set.of(invitedRole))
                    .build();

            userService.saveAll(List.of(user1,user2,user3,user4));
        };
    }

}
