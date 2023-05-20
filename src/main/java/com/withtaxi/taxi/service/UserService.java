package com.withtaxi.taxi.service;


import com.nimbusds.oauth2.sdk.dpop.verifiers.AccessTokenValidationException;
import com.withtaxi.taxi.config.auth.PrincipalDetails;
import com.withtaxi.taxi.model.User;
import com.withtaxi.taxi.model.dto.TokenDto;
import com.withtaxi.taxi.model.dto.TokenRequestDto;
import com.withtaxi.taxi.model.dto.UserRequestDto;
import com.withtaxi.taxi.model.dto.UserResponseDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

public interface UserService {

    /***
     * 아이디 찾기
     * @param name
     * @param email
     * @return
     */
    String findId(String name, String email);

    int removeUser(String userId);


    int checkPassword(String password, PrincipalDetails principalDetails) throws Exception;

    int modifyUserPassword(String password, PrincipalDetails principalDetails);

    int modifyUserInformation(PrincipalDetails principalDetails, UserRequestDto user);

    TokenDto reissue(TokenRequestDto requestDto) throws Exception;
}

