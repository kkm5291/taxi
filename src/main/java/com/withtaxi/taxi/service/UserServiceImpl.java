package com.withtaxi.taxi.service;

import com.withtaxi.taxi.config.auth.PrincipalDetails;
import com.withtaxi.taxi.jwt.JwtProvider;
import com.withtaxi.taxi.model.User;
import com.withtaxi.taxi.model.dto.TokenDto;
import com.withtaxi.taxi.model.dto.TokenRequestDto;
import com.withtaxi.taxi.model.dto.UserRequestDto;
import com.withtaxi.taxi.model.dto.UserResponseDto;
import com.withtaxi.taxi.model.RefreshToken;
import com.withtaxi.taxi.repository.RefreshTokenRepository;
import com.withtaxi.taxi.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public String findId(String name, String email) {
        User user = null;

        try {
            user = userRepository.findByNameAndEmail(name, email);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new UserResponseDto(user).getUserId();
    }

    @Override
    @Transactional
    public int removeUser(String userId) {
        userRepository.deleteByUserId(userId);

        return 1;
    }

    @Override
    public int checkPassword(String password, PrincipalDetails principalDetails) throws Exception {
        String reqPassword = password;

        if (!passwordEncoder.matches(reqPassword, principalDetails.getUser().getPassword())) {
            throw new Exception();
        }
        return 1;
    }

    @Override
    @Transactional
    public int modifyUserPassword(String password, PrincipalDetails principalDetails) {

        User user = principalDetails.getUser();

        String encPassword = passwordEncoder.encode(password);
        user.setPassword(encPassword);

        userRepository.save(user);

        return 1;
    }
    @Override
    public int modifyUserInformation(PrincipalDetails principalDetails, UserRequestDto user) {
        User modifyUser = principalDetails.getUser();

        modifyUser.setNickName(user.getNickName());
        modifyUser.setMobile(user.getMobile());
        modifyUser.setEmail(user.getEmail());
        modifyUser.setUniversity(user.getUniversity());

        userRepository.save(modifyUser);

        return 1;
    }

    @Override
    public TokenDto reissue(TokenRequestDto tokenRequestDto) throws InvalidParameterException, SignatureException {
        if (!jwtProvider.validationToken(tokenRequestDto.getRefreshToken())) {
            throw new InvalidParameterException("유효하지 않은 토큰입니다");
        }

        String accessToken = tokenRequestDto.getAccessToken();
        Authentication authentication = jwtProvider.getAuthentication(accessToken);

        User user = userRepository.findByUserId(authentication.getName());
        RefreshToken refreshToken = refreshTokenRepository.findByUserKey(user.getUserId());

        if (!refreshToken.getToken().equals(tokenRequestDto.getRefreshToken())) {
            throw new SignatureException("토큰이 일치하지 않습니다.");
        }

        TokenDto newCreatedToken = jwtProvider.createToken(user.getUserId(), user.getUniversity());
        RefreshToken updateRefreshToken = refreshToken.updateToken(newCreatedToken.getRefreshToken());


        refreshTokenRepository.save(updateRefreshToken);

        return newCreatedToken;
    }
}
