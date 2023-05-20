package com.withtaxi.taxi.controller;

import com.withtaxi.taxi.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    /***
     * 인증이메일 발급
     * @param email
     * @return code 번호 리턴
     * @throws Exception
     */
    @PostMapping("/mailConfirm")
    String mailConfirm(@RequestParam("email") String email) throws Exception {
        String code = emailService.sendSimpleMessage(email);

        return code;
    }

    /***
     * 임시 비밀번호 이메일로 발급
     * @param userId
     * @return 0 : 일치하는 회원정보 없음
     *         1 : 일치함
     * @throws Exception
     */
    @PostMapping("/issueTemporaryPassword")
    int issueTemporaryPassword(@RequestParam("userId") String userId) throws Exception {
        return emailService.issuedTemporaryPassword(userId);
    }
}
