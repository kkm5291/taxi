package com.withtaxi.taxi.message;

public enum EmailMessage {
    TEMPORARY_PASSWORD_MESSAGE("임시 비밀번호"),
    AUTHENTICATION_NUMBER("인증 번호"),
    SIGN_UP_CODE("회원가입"),
    LOGIN_CODE("로그인");


    private final String emailMessage;

    EmailMessage(String emailMessage) {
        this.emailMessage = emailMessage;
    }

    public String getEmailMessage() {
        return this.emailMessage;
    }
}
