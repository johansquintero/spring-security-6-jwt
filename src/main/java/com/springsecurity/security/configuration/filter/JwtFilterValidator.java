package com.springsecurity.security.configuration.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.springsecurity.security.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

@AllArgsConstructor
public class JwtFilterValidator extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull  HttpServletResponse response,
                                    @NonNull  FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (jwtToken!=null){
            //Bearer dfghksdfsdkfjdsjkfghjhjk...
            //1234567
            jwtToken = jwtToken.substring(7);
            DecodedJWT decodedJwt = this.jwtUtils.validateToken(jwtToken);
            String username = this.jwtUtils.extractUsername(decodedJwt);
            String authorities = this.jwtUtils.getSpecificClaim(decodedJwt,"authorities").asString();
            //READ,WRITE,UPDATE...
            Collection<? extends GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = new UsernamePasswordAuthenticationToken(username,null,authorityList);
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
        }
        filterChain.doFilter(request,response);
    }
}
