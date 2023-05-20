package com.withtaxi.taxi.model.dto;

import com.withtaxi.taxi.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 아이디, 이름, 성별, 닉네임, 핸드폰 번호, 생년월일, 이메일, 대학교

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDto {

    private String userId;
    private String password;
    private String name;
    private String nickName;
    private String sex;
    private String mobile;
    private String birthday;
    private String email;
    private String university;
    private String role;

    @Builder
    public UserRequestDto(String userId, String password, String name, String nickName, String sex, String mobile, String birthday, String email, String university, String role) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.nickName = nickName;
        this.sex = sex;
        this.mobile = mobile;
        this.birthday = birthday;
        this.email = email;
        this.university = university;
        this.role = role;
    }

    public User toEntity() {
        return User.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .nickName(nickName)
                .sex(sex)
                .mobile(mobile)
                .birthday(birthday)
                .email(email)
                .university(university)
                .role(role)
                .build();
    }
}