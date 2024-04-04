package com.springsecurity.security.domain.services;

import com.springsecurity.security.domain.dto.AuthRequestLogInDto;
import com.springsecurity.security.domain.dto.AuthResponseDto;
import com.springsecurity.security.domain.repositories.IUserRepository;
import com.springsecurity.security.domain.usecases.IUserUseCase;
import com.springsecurity.security.persistence.entities.UserEntity;
import com.springsecurity.security.util.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserUseCase {
    private final IUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserDetailServiceImpl userDetailService;

    @Override
    public Iterable<UserEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return Optional.ofNullable(repository.findById(id).orElseThrow(() -> new UsernameNotFoundException("usuario no encontrado")));
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return Optional.ofNullable(repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("usuario no encontrado")));
    }

    @Override
    public UserEntity save(UserEntity newUser) {
        newUser.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
        return repository.save(newUser);
    }

    @Override
    public Iterable<UserEntity> saveAll(Iterable<UserEntity> users) {
        users.forEach(userEntity -> userEntity.setPassword(this.passwordEncoder.encode(userEntity.getPassword())));
        return repository.saveAll(users);
    }

    @Override
    public void delete(Long id) {
        this.repository.delete(id);
    }

    @Override
    public AuthResponseDto logIn(AuthRequestLogInDto authRequestLogInDto) {
        String username = authRequestLogInDto.username();
        String password = authRequestLogInDto.password();

        Authentication authentication = this.authenticate(username,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = this.jwtUtils.createToken(authentication);
        return new AuthResponseDto(username,"User logged successfuly", accessToken, true);
    }

    public Authentication authenticate(String username, String password){
        UserDetails userDetails = this.userDetailService.loadUserByUsername(username);
        if (userDetails==null){
            throw new BadCredentialsException("Invalid username");
        }
        if (!this.passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(username,userDetails.getPassword(),userDetails.getAuthorities());
    }
}
