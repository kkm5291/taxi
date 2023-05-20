package com.withtaxi.taxi.config;

import com.withtaxi.taxi.config.oauth.PrincipalOauth2UserService;
import com.withtaxi.taxi.handler.CustomAuthenticationFailure;
import com.withtaxi.taxi.jwt.ExceptionHandlerFilter;
import com.withtaxi.taxi.jwt.JwtAuthenticationFilter;
import com.withtaxi.taxi.jwt.JwtAuthorizationFilter;
import com.withtaxi.taxi.jwt.JwtProvider;
import com.withtaxi.taxi.repository.RefreshTokenRepository;
import com.withtaxi.taxi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록되도록 함
@RequiredArgsConstructor
public class SecurityConfig {

    private final PrincipalOauth2UserService principalOauth2UserService;
    private final UserRepository userRepository;
    private final CustomAuthenticationFailure customAuthenticationFailure;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().cors();

        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .apply(new MyCustomDsl())
                .and()
                .authorizeRequests()
                .requestMatchers("/", "/**").permitAll()
                .and()
                // oauth설정
                .oauth2Login()
                .userInfoEndpoint()
                .userService(principalOauth2UserService);
        return http.build();
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

            http
                    .addFilter(new ExceptionHandlerFilter(authenticationManager))
                    .addFilter(new JwtAuthenticationFilter(authenticationManager, jwtProvider, refreshTokenRepository))
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository, jwtProvider));

        }
    }
}
