package com.withtaxi.taxi.service;

import com.withtaxi.taxi.model.User;
import com.withtaxi.taxi.model.dto.UserRequestDto;


public interface JoinService {


    User registerUser(UserRequestDto user);
    boolean checkUserIdDuplicate(String userId);

    boolean checkNickNameDuplicate(String nickName);

    boolean checkEmailDuplicate(String email);
}
