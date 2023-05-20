package com.withtaxi.taxi.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExceptionHandlerFilter extends BasicAuthenticationFilter {
    public ExceptionHandlerFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {


        System.out.println("3. ExceptionHandlerFilter");
        try {
        chain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            sendErrorResponse(response, "만료된 토큰");
        } catch (SignatureException e) {
            sendErrorResponse(response, "서명이 일치하지 않는 토큰입니다");
        } catch (MalformedJwtException e) {
            sendErrorResponse(response, "유효하지 않는 토큰입니다");
        }


    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(message));
    }
}
