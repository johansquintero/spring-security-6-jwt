package com.springsecurity.security.domain.services;

import com.springsecurity.security.domain.dto.AuthRequestLogInDto;
import com.springsecurity.security.domain.dto.AuthRequestSignUpDto;
import com.springsecurity.security.domain.dto.AuthResponseDto;
import com.springsecurity.security.domain.repositories.IRoleRepository;
import com.springsecurity.security.domain.repositories.IUserRepository;
import com.springsecurity.security.domain.usecases.IUserUseCase;
import com.springsecurity.security.exception.ErrorAlertMessages;
import com.springsecurity.security.persistence.entities.RoleEntity;
import com.springsecurity.security.persistence.entities.UserEntity;
import com.springsecurity.security.util.JwtUtils;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserUseCase {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserDetailServiceImpl userDetailService;
    private final IRoleRepository roleRepository;

    @Override
    public Iterable<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return Optional.of(userRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException(ErrorAlertMessages.USER_NOT_EXISTS_MESSAGE)));
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return Optional.of(userRepository
                .findByUsername(username)
                .orElseThrow(() -> new ValidationException(ErrorAlertMessages.USER_NOT_EXISTS_MESSAGE)));
    }

    @Override
    public UserEntity save(UserEntity newUser) {
        var x = this.userRepository.
                findByUsername(newUser.getUsername());
        if (x.isPresent()) {
            throw new ValidationException(ErrorAlertMessages.USER_ALREADY_EXISTS_MESSAGE);
        }
        newUser.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
        return userRepository.save(newUser);
    }

    @Override
    public Iterable<UserEntity> saveAll(Iterable<UserEntity> users) {
        users.forEach(userEntity -> {
                    var x = this.userRepository.
                            findByUsername(userEntity.getUsername());
                    if (x.isPresent()) {
                        throw new ValidationException(ErrorAlertMessages.USER_ALREADY_EXISTS_MESSAGE);
                    }
                    userEntity.setPassword(this.passwordEncoder.encode(userEntity.getPassword()));
                }
        );
        return userRepository.saveAll(users);
    }

    @Override
    public void delete(Long id) {
        this.userRepository.delete(id);
    }

    @Override
    public AuthResponseDto logIn(AuthRequestLogInDto authRequestLogInDto) {
        String username = authRequestLogInDto.username();
        String password = authRequestLogInDto.password();

        Authentication authentication = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = this.jwtUtils.createToken(authentication);
        return new AuthResponseDto(username, "User logged successfully", accessToken, true);
    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = this.userDetailService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new ValidationException("Invalid username");
        }
        if (!this.passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new ValidationException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }

    @Override
    public AuthResponseDto signUp(AuthRequestSignUpDto authRequestSignUpDto) {
        String username = authRequestSignUpDto.username();
        String password = authRequestSignUpDto.password();
        List<String> roleList = authRequestSignUpDto.authRolesRequestDto().rolesListName();

        Optional<UserEntity> userCheck = this.userRepository.findByUsername(username);
        Set<RoleEntity> roleEntitySet = new HashSet<>(this.roleRepository.findAllByEnum(roleList));

        if (userCheck.isPresent()) {
            throw new ValidationException(ErrorAlertMessages.USER_ALREADY_EXISTS_MESSAGE);
        }else if (roleEntitySet.isEmpty()){
            throw new ValidationException(ErrorAlertMessages.ROLES_NOT_EXIST_MESSAGE);
        }
        UserEntity newUser = UserEntity.builder()
                .username(username)
                .password(this.passwordEncoder.encode(password))
                .roles(roleEntitySet)
                .credentialNoExpired(true)
                .accountNoExpired(true)
                .isEnabled(true)
                .accountNoLocked(true)
                .build();
        UserEntity userCreated = this.userRepository.save(newUser);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userCreated.getRoles().forEach(roleEntity -> authorities.add(new SimpleGrantedAuthority("ROLE_".concat(roleEntity.getRoleEnum().name()))));
        userCreated.getRoles()
                .stream()
                .flatMap(roleEntity -> roleEntity.getPermissions().stream())
                .forEach(permissionEntity -> authorities.add(new SimpleGrantedAuthority(permissionEntity.getName())));
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userCreated.getUsername(),
                userCreated.getPassword(),
                authorities);
        String accessToken = this.jwtUtils.createToken(authentication);
        return new AuthResponseDto(userCreated.getUsername(), "User created successfully", accessToken, true);
    }
}
