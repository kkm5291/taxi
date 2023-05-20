package com.withtaxi.taxi.controller;

import com.nimbusds.oauth2.sdk.dpop.verifiers.AccessTokenValidationException;
import com.withtaxi.taxi.config.auth.PrincipalDetails;
import com.withtaxi.taxi.model.User;
import com.withtaxi.taxi.model.dto.TokenDto;
import com.withtaxi.taxi.model.dto.TokenRequestDto;
import com.withtaxi.taxi.model.dto.UserRequestDto;
import com.withtaxi.taxi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Map;

/**
 * User 관련 Controller
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    private final UserService userService;

    /***
     * user 정보 조회 API
     * 내정보
     * @param authentication
     * @return principalDetails.getUser()
     */
    @GetMapping("/info")
    public User user(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        return principalDetails.getUser();
    }


    /***
     * 회원탈퇴
     * @param authentication jwt토큰이 헤더에 달려있어야함
     * @return 회원탈퇴시 1 반환
     */
    @DeleteMapping("/withdrawal")
    public int removeUser(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        return userService.removeUser(principalDetails.getUser().getUserId());
    }

    /***
     * 아이디 찾기
     * @param user
     * @return 일치하는 아이디가 있으면 아이디 반환
     *         일치하는 아이디가 없으면 NPE
     */
    @PostMapping("/findId")
    public ResponseEntity findId(@RequestBody UserRequestDto user) {

        return new ResponseEntity(userService.findId(user.getName(), user.getEmail()), HttpStatus.OK);

    }

    /***
     * 비밀번호 재확인
     * @param passwordMap
     * @param authentication
     * @return 비밀번호 일치시 1
     *         비밀번호 불일치시 0 반환
     *         +
     *         httpStatus 200 ok 반환
     */
    @PostMapping("/checkPassword")
    public ResponseEntity<Integer> checkPassword(@RequestBody Map<String, String> passwordMap, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        try {
            return new ResponseEntity(userService.checkPassword(passwordMap.get("password"), principalDetails), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /***
     * 비밀번호 수정
     * @param passwordMap
     * @param authentication
     * @return 비밀번호 변경 후 1 반환
     *         +
     *         httpStatus 200 ok 반환
     */
    @PutMapping("/modifyPassword")
    public ResponseEntity<Integer> modifyPassword(@RequestBody Map<String, String> passwordMap, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        return new ResponseEntity(userService.modifyUserPassword(passwordMap.get("password"), principalDetails), HttpStatus.OK);
    }

    /***
     * 회원정보 수정
     * @param user
     * @param authentication
     * @return 닉네임, 모바일, 이메일, 학교 변경
     *         변경 완료시 1 반환
     *         +
     *         httpStatus 200
     */
    @PutMapping("/modifyUserInfo")
    public ResponseEntity<Integer> modifyUserInfo(@RequestBody UserRequestDto user, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        return new ResponseEntity(userService.modifyUserInformation(principalDetails, user), HttpStatus.OK);
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody TokenRequestDto tokenRequestDto) throws Exception {
        try {
            return new ResponseEntity<>(userService.reissue(tokenRequestDto), HttpStatus.OK);
        } catch (InvalidParameterException e) {
            return new ResponseEntity<>("유효하지 않은 토큰입니다", HttpStatus.BAD_REQUEST);
        } catch (SignatureException e) {
            return new ResponseEntity<>("서명이 다른 토큰입니다", HttpStatus.BAD_REQUEST);
        }
    }

}