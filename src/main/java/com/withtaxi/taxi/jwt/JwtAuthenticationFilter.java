package com.withtaxi.taxi.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.withtaxi.taxi.config.auth.PrincipalDetails;
import com.withtaxi.taxi.model.User;
import com.withtaxi.taxi.model.dto.TokenDto;
import com.withtaxi.taxi.model.RefreshToken;
import com.withtaxi.taxi.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    // /login으로 오면 일로들어옴
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        System.out.println("1. JwtAuthenticationFilter");
        try {
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserId(), user.getPassword());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            System.out.println("로그인 완료");

            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        TokenDto tokenDto = jwtProvider.createToken(principalDetails.getUser().getUserId(), principalDetails.getUser().getUniversity());

        RefreshToken refreshToken = RefreshToken.builder()
                .userKey(principalDetails.getUser().getUserId())
                .token(tokenDto.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);



//        // hash방식으로 작동
//        String jwtToken = JWT.create()
//                .withSubject("TaxiProjectToken")
//                .withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 100))) // 1분
//                .withClaim("id", principalDetails.getUser().getUserId())
//                .withClaim("name", principalDetails.getUser().getName())
//                .sign(Algorithm.HMAC512(""));
//
//        String refreshToken = JWT.create()
//                .withExpiresAt(new Date(System.currentTimeMillis() + 14 * 24 * 60 * 1000L))
//                .sign(Algorithm.HMAC512(""));

        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", "Bearer " + tokenDto.getRefreshToken());
        response.getWriter().write(new ObjectMapper().writeValueAsString(tokenDto));

    }
}

