package com.springsecurity.security.domain.services;

import com.springsecurity.security.domain.repositories.IUserRepository;
import com.springsecurity.security.exception.ErrorAlertMessages;
import com.springsecurity.security.persistence.entities.UserEntity;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final IUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = this.repository.findByUsername(username)
                .orElseThrow(() -> new ValidationException(ErrorAlertMessages.USER_NOT_EXISTS_MESSAGE));
        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
        userEntity.getRoles().forEach(roleEntity ->
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_".concat(roleEntity.getRoleEnum().name())))
        );
        userEntity.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .forEach(permissionEntity -> grantedAuthorities.add(new SimpleGrantedAuthority(permissionEntity.getName())));

        return new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.isEnabled(),
                userEntity.isAccountNoExpired(),
                userEntity.isCredentialNoExpired(),
                userEntity.isAccountNoLocked(), grantedAuthorities);
    }
}
