package com.withtaxi.taxi.controller;

import com.withtaxi.taxi.model.User;
import com.withtaxi.taxi.model.dto.UserRequestDto;
import com.withtaxi.taxi.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/join")
public class JoinController {

    private final JoinService joinService;

    /***
     * 회원가입 API
     * @param user
     * @return db에 값 저장
     */
    @PostMapping("")
    public User registerUser(@RequestBody UserRequestDto user) {
        return joinService.registerUser(user);
    }

    /***
     * 아이디 중복체크
     * @param userId
     * @return true : 중복 있음
     *         false : 중복 없음
     * @return httpStatus 200 ok
     */
    @GetMapping("/user-id/{userId}/dup")
    public ResponseEntity<Boolean> checkUserIdDuplicate(@PathVariable String userId) {
        return ResponseEntity.ok(joinService.checkUserIdDuplicate(userId));
    }

    /***
     * 닉네임 중복체크
     * @param nickName
     * @return true : 중복 있음
     *         false : 중복 없음
     * @return httpStatus 200 ok
     */
    @GetMapping("/user-nickname/{nickName}/dup")
    public ResponseEntity<Boolean> checkNickNameDuplicate(@PathVariable String nickName) {
        return ResponseEntity.ok(joinService.checkNickNameDuplicate(nickName));
    }


    /***
     * 이메일 중복체크
     * @param email
     * @return true : 중복 있음
     *         false : 중복 없음
     * @return httpStatus 200 ok
     */
    @GetMapping("/user-email/{email}/dup")
    public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable String email) {
        return ResponseEntity.ok(joinService.checkEmailDuplicate(email));
    }

}
