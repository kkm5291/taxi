package com.withtaxi.taxi.model.dto;

import com.withtaxi.taxi.model.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private String userId;
    private String name;
    private String nickName;
    private String sex;
    private String mobile;
    private String birthday;
    private String email;
    private String university;

    public UserResponseDto(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.nickName = user.getNickName();
        this.sex = user.getSex();
        this.mobile = user.getMobile();
        this.birthday = user.getBirthday();
        this.email = user.getEmail();
        this.university = user.getUniversity();
    }
}
