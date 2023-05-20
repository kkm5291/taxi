package com.withtaxi.taxi.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.withtaxi.taxi.config.auth.PrincipalDetails;
import com.withtaxi.taxi.model.User;
import com.withtaxi.taxi.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;



    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, JwtProvider jwtProvider) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("2. JwtAuthorizationFilter");
//        try {
            String jwtHeader = jwtProvider.resolveToken(request);

            if (jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
                chain.doFilter(request, response);
                return;
            }

            String jwtToken = jwtProvider.resolveToken(request).replace("Bearer ", "");
            String userId = jwtProvider.getUserId(jwtToken);


            if (jwtToken != null && jwtProvider.validationToken(jwtToken)) {
                User userEntity = userRepository.findByUserId(userId);

                PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);

                chain.doFilter(request, response);
            }
//        } catch (ExpiredJwtException e) {
//            sendErrorResponse(response, "만료된 토큰");
//        } catch (SignatureException e) {
//            sendErrorResponse(response, "서명이 일치하지 않는 토큰입니다");
//        } catch (MalformedJwtException e) {
//            sendErrorResponse(response, "유효하지 않는 토큰입니다");
//        }
    }


}
